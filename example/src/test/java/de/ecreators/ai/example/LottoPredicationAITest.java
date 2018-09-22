package de.ecreators.ai.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Duration;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.ecreators.ai.parent.config.NetworkMemory;
import de.ecreators.ai.parent.training.NetworkTraining;
import de.ecreators.ai.parent.training.NetworkUseCase;

/**
 * @author Bjoern Frohberg
 */
public class LottoPredicationAITest {

  private static final String DATE_PROPERTY           = "date";
  private static final String EXTRA_NUM_PREFIX        = "extra";
  private static final String TRAINING_DATA_FILE_NAME = "Lotto6aus49_mitZusatzZahl.txt";

  private static final int MIN_YEAR      = 1956;
  private static final int MAX_YEAR      = 2200;
  private static final int MAX_DAYS      = sumDaysOverTime();
  private static final int MIN_NUMBER    = 1;
  private static final int MAX_NUMBER    = 49;
  private static final int MIN_EXTRA_NUM = 1;
  private static final int MAX_EXTRA_NUM = 49;
  // parsing
  private static final int    COL_YEAR      = 0;
  private static final int    COL_MONTH     = 1;
  private static final int    COL_DAY       = 2;
  private static final int    COL_NUM_1     = 3;
  private static final int    COL_NUM_2     = 4;
  private static final int    COL_NUM_3     = 5;
  private static final int    COL_NUM_4     = 6;
  private static final int    COL_NUM_5     = 7;
  private static final int    COL_NUM_6     = 8;
  private static final int    COL_EXTRA_NUM = 9;
  private File                file;
  private SimplePredicationAI ai;

  @Before
  public void setUp() {
    this.file = new File(System.getProperty("user.home") + "/Desktop", "lottoki.json");
    this.ai = newLottoAI();
    try {
      load(this.ai);
    }
    catch (final IOException e) {
      System.out.println("Memory not yet available");
    }
    this.watch = new StopWatch();
  }

  @After
  public void tearDown() throws Exception {
    save(this.ai);
  }

  private static final double ETA                            = 0.65;
  private static final double ACCEPTED_TOTAL_ERROR_THRESHOLD = 0.0172;
  private static final int    YEAR                           = 2018;
  private static final int    MONTH                          = 9;
  private static final int    DAY                            = 22;

  @Test
  public void trainLotto() throws IOException {
    this.ai = newLottoAI();
    runTrainingAndLog(this.ai, ETA, ACCEPTED_TOTAL_ERROR_THRESHOLD);
  }

  @Test
  public void testLotto() {
    final Map<String, Double> output = this.ai.test(inputValues(relativeDayInRange(YEAR, MONTH, DAY)));

    final List<Map.Entry<String, Double>> orderedOutput = new ArrayList<>(output.entrySet());
    orderedOutput.sort(this::sortByNameAscending);

    final List<Map.Entry<String, Double>> _6of49 = new ArrayList<>();
    final List<Map.Entry<String, Double>> extraNumber = new ArrayList<>();

    for (final Map.Entry<String, Double> entry : orderedOutput) {
      // System.out.println(entry);
      final String oldName = entry.getKey();
      final String name = oldName.replace(EXTRA_NUM_PREFIX, "");
      final boolean isExtraNumber = name.length() < oldName.length();
      if (isExtraNumber) {
        extraNumber.add(entry);
      }
      else {
        _6of49.add(entry);
      }
    }

    _6of49.sort(this::sortByChanceDescending);
    final List<Map.Entry<String, Double>> best6Numbers = _6of49.subList(0, 6);
    for (final Map.Entry<String, Double> best6Number : best6Numbers) {
      extraNumber.removeIf(e -> e.getKey().replace(EXTRA_NUM_PREFIX, "").equals(best6Number.getKey()));
    }

    extraNumber.sort(this::sortByChanceDescending);
    final Map.Entry<String, Double> bestExtra = extraNumber.get(0);
    best6Numbers.sort(this::sortByNameAscending);

    System.out.println(String.format("== Lotto-Prognose %2d.%2d.%4d =========", DAY, MONTH, YEAR));

    for (final Map.Entry<String, Double> best6Number : best6Numbers) {
      final double percent = best6Number.getValue() * 100;
      final String text = String.format("Zahl: %s Chance: %.1f%%", best6Number.getKey(), percent);
      System.out.println(text);
    }

    System.out.println("==== Zusatzzahl ====");

    final double percent = bestExtra.getValue() * 100;
    final String number = bestExtra.getKey().replace(EXTRA_NUM_PREFIX, "");
    final String text = String.format("Zahl: %s Chance: %.1f%%", number, percent);
    System.out.println(text);

    System.out.println(String.format("== Lotto-Prognose %2d.%2d.%4d =========", DAY, MONTH, YEAR));
  }

  private int sortByChanceDescending(final Map.Entry<String, Double> a, final Map.Entry<String, Double> b) {
    return -a.getValue().compareTo(b.getValue());
  }

  private int sortByNameAscending(final Map.Entry<String, Double> a, final Map.Entry<String, Double> b) {
    final String nameA = clearName(a.getKey());
    final String nameB = clearName(b.getKey());
    return nameA.compareToIgnoreCase(nameB);
  }

  private String clearName(final String name) {

    // 1
    // 11
    // bla1
    // bla11

    String numberText = name.replace(EXTRA_NUM_PREFIX, "");
    final boolean extraNum = name.length() > numberText.length();
    if (numberText.length() == 1) {
      numberText = "0" + numberText;
    }
    if (extraNum) {
      numberText = EXTRA_NUM_PREFIX + numberText;
    }

    return numberText;
  }

  private void save(final SimplePredicationAI ai) throws IOException {
    System.out.println("Save to " + this.file.toString());
    saveMemory(this.file, toJson(ai.getMemoryUpdated()));
  }

  private void load(final SimplePredicationAI ai) throws IOException {
    System.out.println("Load from " + this.file.toString());
    ai.loadMemory(loadMemory(this.file));
  }

  private void
          runTrainingAndLog(final SimplePredicationAI ai, final double eta, final double acceptedTotalErrorThreshold) throws IOException {
    final NetworkTraining training = newLottoTraining(eta, acceptedTotalErrorThreshold);
    ai.setTrainingListener((totalError, solved) -> onTraining(totalError, ai));
    ai.train(training, false);
  }

  private NetworkTraining newLottoTraining(final double eta, final double acceptedTotalErrorThreshold) throws IOException {
    final NetworkTraining training = new NetworkTraining(eta, acceptedTotalErrorThreshold, false);
    addTrainingUseCase(training);
    return training;
  }

  private SimplePredicationAI newLottoAI() {
    final SimplePredicationAI.InputArgs input = new SimplePredicationAI.InputArgs();
    input.addName(DATE_PROPERTY);

    final int inPropertiesCount = 4;
    final SimplePredicationAI.HiddenArgs hidden = new SimplePredicationAI.HiddenArgs() {

      private final int[] bestNeuronCount = {
                                              7 * 2,
                                              7 * 2,
                                              7 * 2,
                                              7 * 2
      };

      @Override
      public int getHiddenNeuronsForLayer(final int hiddenLayerIndex) {
        return this.bestNeuronCount[hiddenLayerIndex];
      }
    };
    hidden.setHiddenLayerCount(inPropertiesCount);

    final SimplePredicationAI.OutputArgs output = new SimplePredicationAI.OutputArgs();
    for (int num = MIN_NUMBER; num <= MAX_NUMBER; num++) {
      output.addName(String.valueOf(num));
    }
    for (int num = MIN_EXTRA_NUM; num <= MAX_EXTRA_NUM; num++) {
      output.addName(EXTRA_NUM_PREFIX + num);
    }
    return new SimplePredicationAI(input, hidden, output);
  }

  private static NetworkMemory loadMemory(final File file) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    final NetworkMemory memory = mapper.readValue(file, NetworkMemory.class);
    return memory;
  }

  private void saveMemory(final File file, final String content) throws IOException {
    final byte[] data = content.getBytes(Charset.forName("UTF-8"));
    Files.write(file.toPath(), data);
  }

  private static String toJson(final NetworkMemory memory) throws JsonProcessingException {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    final String json = mapper.writeValueAsString(memory);
    return json;
  }

  private StopWatch watch;

  private boolean onTraining(final double totalError, final SimplePredicationAI ai) {
    if (!this.watch.isStarted()) {
      this.watch.start();
    }
    final long generation = ai.getMemoryUpdated().getNetworkMetaData().getGeneration();
    final String msg = String.format("Total Error: %.4f (G: %d)", totalError, generation);
    System.out.println(msg);
    boolean cancel = false;
    if (this.watch.getTime() > Duration.ofMinutes(2).toMillis()) {
      this.watch.stop();
      cancel = true;
    }
    return cancel;
  }

  private void addTrainingUseCase(final NetworkTraining training) throws IOException {
    final URL resource = getClass().getClassLoader().getResource(TRAINING_DATA_FILE_NAME);
    final File file = new File(resource.getFile());

    // Read File Content
    final String content = new String(Files.readAllBytes(file.toPath()));
    final String[] split = content.split(System.lineSeparator());
    for (int i = 1; i < split.length; i++) {
      final String line = split[i];

      final String[] columns = line.split(";");

      final int year = Integer.parseInt(columns[COL_YEAR]);
      final int month = Integer.parseInt(columns[COL_MONTH]);
      final int day = Integer.parseInt(columns[COL_DAY]);
      final int num1 = Integer.parseInt(columns[COL_NUM_1]);
      final int num2 = Integer.parseInt(columns[COL_NUM_2]);
      final int num3 = Integer.parseInt(columns[COL_NUM_3]);
      final int num4 = Integer.parseInt(columns[COL_NUM_4]);
      final int num5 = Integer.parseInt(columns[COL_NUM_5]);
      final int num6 = Integer.parseInt(columns[COL_NUM_6]);
      final int extraNumber = Integer.parseInt(columns[COL_EXTRA_NUM]);

      final NetworkUseCase useCase = newUseCase(year, month, day, num1, num2, num3, num4, num5, num6, extraNumber);
      training.add(useCase);

    }
  }

  private NetworkUseCase newUseCase(final int year,
                                    final int month,
                                    final int day,
                                    final int num1,
                                    final int num2,
                                    final int num3,
                                    final int num4,
                                    final int num5,
                                    final int num6,
                                    final int extraNumber) {
    return newUseCase(year, month, day, new int[] { num1, num2, num3, num4, num5, num6 }, extraNumber);
  }

  private NetworkUseCase newUseCase(final int year, final int month, final int day, final int[] solvedNumbers, final int extraNumber) {
    final NetworkUseCase useCase = new NetworkUseCase();
    useCase.setInputValues(inputValues(relativeDayInRange(year, month, day)));
    useCase.setExpectedOutputValues(expectedValues(solvedNumbers, extraNumber));
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
    for (int num = MIN_EXTRA_NUM; num <= MAX_EXTRA_NUM; num++) {
      expected.put(EXTRA_NUM_PREFIX + num, 0d);
    }

    for (final int number : numbers) {
      final String name = String.valueOf(number);
      expected.put(name, 1d);
    }

    final String name = EXTRA_NUM_PREFIX + superNumber;
    expected.put(name, 1d);

    return expected;
  }

  private static int sumDaysOverTime() {
    return sumDaysOverTime(null, null, null);
  }

  private static int sumDaysOverTime(final Integer stopYear, final Integer stopMonth, final Integer stopDay) {

    int days = 0;

    for (int year = MIN_YEAR; year <= MAX_YEAR; year++) {
      if (stopYear != null && stopYear == year) {
        for (int month = 1; month <= 12; month++) {
          final YearMonth yMonth = YearMonth.of(year, month);
          final int daysInMonth = yMonth.lengthOfMonth();
          if (stopMonth != null && stopMonth == month) {
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
