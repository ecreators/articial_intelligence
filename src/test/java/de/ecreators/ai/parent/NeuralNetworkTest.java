package de.ecreators.ai.parent;

import static org.hamcrest.CoreMatchers.is;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.config.NetworkConfig;
import de.ecreators.ai.parent.strategy.INeuronActivationFunction;
import de.ecreators.ai.parent.training.NetworkTraining;
import de.ecreators.ai.parent.training.NetworkUseCase;

/**
 * @author bjoern frohberg
 */
public class NeuralNetworkTest {

  private static final String PROPERTY_A_NAME = "a";
  private static final String PROPERTY_B_NAME = "b";
  private static final String OUTPUT_XOR_NAME = "xor";

  @Test
  public void smokeTestWithXor() {
    final NetworkTraining xorTraining = newSmokeTestTraining();
    final NetworkConfig xorConfig = newSmokeTestConfig(10, 1);

    final NeuralNetwork network = new NeuralNetwork(xorConfig);
    network.registerTotalErrorListenerHandler((totalError, solved) -> {
      final double err = roundDigits(totalError, 4);
      final long generation = network.getMemory().getMetaData().getGeneration();

      System.out.println(MessageFormat.format("Total Error: {0} ( generation = {1} )",
                                              err,
                                              generation));
    });
    network.runTrainingSession(xorTraining);

    final double uc000 = roundDigits(network.testValues(inputValues(0, 0)).get(OUTPUT_XOR_NAME), 0);
    Assert.assertThat(uc000, is(0d));

    final double uc011 = roundDigits(network.testValues(inputValues(0, 1)).get(OUTPUT_XOR_NAME), 0);
    Assert.assertThat(uc011, is(1d));

    final double uc101 = roundDigits(network.testValues(inputValues(1, 0)).get(OUTPUT_XOR_NAME), 0);
    Assert.assertThat(uc101, is(1d));

    final double uc110 = roundDigits(network.testValues(inputValues(1, 1)).get(OUTPUT_XOR_NAME), 0);
    Assert.assertThat(uc110, is(0d));
  }

  public static double roundDigits(final double totalError, final int digits) {
    final double digitRounding = Math.pow(10, digits);
    return Math.round(totalError * digitRounding) / digitRounding;
  }

  private NetworkConfig newSmokeTestConfig(final int neuronsCountHiddenLayer, final int hiddenLayers) {
    final LayerConfig input = new LayerConfig(INeuronActivationFunction.IDENTITY,
                                              2,
                                              Arrays.asList(PROPERTY_A_NAME, PROPERTY_B_NAME),
                                              null);
    final List<LayerConfig> hidden = new ArrayList<>();
    for (int i = 0; i < hiddenLayers; i++) {
      hidden.add(new LayerConfig(INeuronActivationFunction.SIGMOID,
                                 neuronsCountHiddenLayer,
                                 null));
    }

    final LayerConfig output = new LayerConfig(INeuronActivationFunction.SIGMOID,
                                               1,
                                               Arrays.asList(OUTPUT_XOR_NAME),
                                               null);
    return new NetworkConfig(input, hidden, output);
  }

  private NetworkTraining newSmokeTestTraining() {
    final NetworkTraining training = new NetworkTraining(0.35, 0);
    training.add(newXorUseCase(0, 0, 0));
    training.add(newXorUseCase(0, 1, 1));
    training.add(newXorUseCase(1, 0, 1));
    training.add(newXorUseCase(1, 1, 0));
    return training;
  }

  private NetworkUseCase newXorUseCase(final double a, final double b, final double expectedOutput) {
    final NetworkUseCase useCase = new NetworkUseCase();
    useCase.setInputValues(inputValues(a, b));
    useCase.setExpectedOutputValues(expectedValues(expectedOutput));
    return useCase;
  }

  private Map<String, Double> expectedValues(final double expectedOutput) {
    final Map<String, Double> outputProperties = new HashMap<>();
    outputProperties.put(OUTPUT_XOR_NAME, expectedOutput);
    return outputProperties;
  }

  private Map<String, Double> inputValues(final double a, final double b) {
    final Map<String, Double> properties = new HashMap<>();
    properties.put(PROPERTY_A_NAME, a);
    properties.put(PROPERTY_B_NAME, b);
    return properties;
  }
}
