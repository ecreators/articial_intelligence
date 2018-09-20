package de.ecreators.ai.example;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.ecreators.ai.parent.training.NetworkTraining;
import de.ecreators.ai.parent.training.NetworkUseCase;

/**
 * @author Bjoern Frohberg
 */
public class LottoPredicationAITest {

  private static final String DATE_PROPERTY = "date";
  private static final String SUPER_PREFIX  = "super";

  private static final int MIN_YEAR   = 1955;
  private static final int MAX_YEAR   = 2200;
  private static final int MAX_DAYS   = sumDaysOverTime();
  private static final int MIN_NUMBER = 1;
  private static final int MAX_NUMBER = 49;
  private static final int MIN_SUPER  = 0;
  private static final int MAX_SUPER  = 9;

  @Test
  public void lotto() {
    final SimplePredicationAI.InputArgs input = new SimplePredicationAI.InputArgs();
    input.getNames().add(DATE_PROPERTY);

    final int bestNeuronCount = MIN_NUMBER + MAX_NUMBER;
    final SimplePredicationAI.HiddenArgs hidden = new SimplePredicationAI.HiddenArgs() {

      @Override
      public int getHiddenNeuronsForLayer(final int hiddenLayerIndex) {
        return bestNeuronCount;
      }
    };
    final int inPropertiesCount = 2;
    hidden.setHiddenLayerCount(inPropertiesCount);

    final SimplePredicationAI.OutputArgs output = new SimplePredicationAI.OutputArgs();
    for (int num = MIN_NUMBER; num <= MAX_NUMBER; num++) {
      output.getNames().add(String.valueOf(num));
    }
    for (int num = MIN_SUPER; num <= MAX_SUPER; num++) {
      output.getNames().add(SUPER_PREFIX + num);
    }
    final SimplePredicationAI ai = new SimplePredicationAI(input, hidden, output);

    final NetworkTraining training = new NetworkTraining(0.65, 0.09, false);
    addTrainingUseCase(training);

    ai.train(training, false);
  }

  private void addTrainingUseCase(final NetworkTraining training) {
    training.add(newUseCase(2018, 12, 31, new int[] { 1, 2, 3, 4, 5, 6 }, 9));
  }

  private NetworkUseCase newUseCase(final int year, final int month, final int day, final int[] solvedNumbers, final int solvedSuper) {
    final NetworkUseCase useCase = new NetworkUseCase();
    useCase.setInputValues(inputValues(relativeDayInRange(year, month, day)));
    useCase.setExpectedOutputValues(expectedValues(solvedNumbers, solvedSuper));
    return useCase;
  }

  private static double relativeDayInRange(final int year, final int month, final int day) {
    return sumDaysOverTime(year, month, day) / (double) MAX_DAYS;
  }

  private Map<String, Double> expectedValues(final int[] numbers, final int superNumber) {
    final Map<String, Double> expected = new HashMap<>();
    for (int num = MIN_NUMBER; num <= MAX_NUMBER; num++) {
      expected.put(String.valueOf(num), 0d);
    }
    for (int num = MIN_SUPER; num <= MAX_SUPER; num++) {
      expected.put(SUPER_PREFIX + num, 0d);
    }

    for (final int number : numbers) {
      final String name = String.valueOf(number);
      expected.put(name, 1d);
    }

    final String name = SUPER_PREFIX + superNumber;
    expected.put(name, 1d);

    return expected;
  }

  private static int sumDaysOverTime() {
    return sumDaysOverTime(null, null, null);
  }

  private static int sumDaysOverTime(final Integer stopYear, final Integer stopMonth, final Integer stopDay) {

    int days = 0;

    for (int year = MIN_YEAR; year <= MAX_YEAR; year++) {
      if (stopYear == year) {
        for (int month = 1; month <= 12; month++) {
          final YearMonth yMonth = YearMonth.of(year, month);
          final int daysInMonth = yMonth.lengthOfMonth();
          if (stopMonth == month) {
            if (stopDay == null) {
              days += daysInMonth;
            }
            else {
              days += stopDay;
              break;
            }
          }
          else {
            days += daysInMonth;
          }
        }
        break;
      }
      else {
        days += YearMonth.of(year, 1).lengthOfYear();
      }
    }

    return days;
  }

  private static Map<String, Double> inputValues(final double dateValue) {
    final Map<String, Double> values = new HashMap<>();
    values.put(DATE_PROPERTY, dateValue);
    return values;
  }
}
