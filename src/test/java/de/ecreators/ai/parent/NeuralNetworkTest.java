package de.ecreators.ai.parent;

import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.config.NetworkConfig;
import de.ecreators.ai.parent.config.NetworkMemory;
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
  public void smokeTestWithXor_a_hidden_a() throws IOException {
    final double eta = 0.65;
    final int acceptedTotalErrorThreshold = 0;
    final int neuronsCountHiddenLayer = 5;
    final int hiddenLayers = 1;
    assertXorSmokeTest(eta, acceptedTotalErrorThreshold, neuronsCountHiddenLayer, hiddenLayers);
  }

  @Test
  public void smokeTestWithXor_a_hidden_b() throws IOException {
    final double eta = 0.65;
    final int acceptedTotalErrorThreshold = 0;
    final int neuronsCountHiddenLayer = 10;
    final int hiddenLayers = 1;
    assertXorSmokeTest(eta, acceptedTotalErrorThreshold, neuronsCountHiddenLayer, hiddenLayers);
  }

  @Test
  public void smokeTestWithXor_b() throws IOException {
    final double eta = 0.35;
    final int acceptedTotalErrorThreshold = 0;
    final int neuronsCountHiddenLayer = 10;
    final int hiddenLayers = 1;
    assertXorSmokeTest(eta, acceptedTotalErrorThreshold, neuronsCountHiddenLayer, hiddenLayers);
  }

  @Test
  public void smokeTestWithXor_c() throws IOException {
    final double eta = 0.15;
    final int acceptedTotalErrorThreshold = 0;
    final int neuronsCountHiddenLayer = 10;
    final int hiddenLayers = 1;
    assertXorSmokeTest(eta, acceptedTotalErrorThreshold, neuronsCountHiddenLayer, hiddenLayers);
  }

  public void assertXorSmokeTest(final double eta,
                                 final int acceptedTotalErrorThreshold,
                                 final int neuronsCountHiddenLayer,
                                 final int hiddenLayers) throws IOException {
    final NetworkTraining xorTraining = newSmokeTestTraining(eta, acceptedTotalErrorThreshold);
    final NetworkConfig xorConfig = newSmokeTestConfig(neuronsCountHiddenLayer, hiddenLayers);

    final NeuralNetwork network = new NeuralNetwork(xorConfig);
    network.setName("XOR");
    network.registerTotalErrorListenerHandler((totalError, solved) -> {
      final double err = roundDigits(totalError, 8);
      final long generation = network.getMemory().getNetworkMetaData().getGeneration();

      System.out.println(MessageFormat.format("Total Error: {0} ( generation = {1} )",
                                              err,
                                              generation));
    });
    network.runTrainingSession(xorTraining, true);

    final double uc000 = Math.round(network.testValues(inputValues(0, 0)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc000, is(0d));

    final double uc011 = Math.round(network.testValues(inputValues(0, 1)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc011, is(1d));

    final double uc101 = Math.round(network.testValues(inputValues(1, 0)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc101, is(1d));

    final double uc110 = Math.round(network.testValues(inputValues(1, 1)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc110, is(0d));
  }

  // @Test
  // public void test() throws Exception {
  //
  // final NeuralNetwork network = new NeuralNetwork(new NetworkConfig());
  // final NetworkMemory loadedMemory = printJson(network.updateMemory());
  //
  // }

  private NetworkMemory printJson(final NetworkMemory memory) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    final String jsonMemory = mapper.writeValueAsString(mapper);
    final String ln = System.lineSeparator();
    System.out.println("Network XOR" + ln + jsonMemory);
    return mapper.readValue(jsonMemory, NetworkMemory.class);
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

  private NetworkTraining newSmokeTestTraining(final double eta, final int acceptedTotalErrorThreshold) {
    final NetworkTraining training = new NetworkTraining(eta, acceptedTotalErrorThreshold, true);
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
