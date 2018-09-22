package de.ecreators.ai.parent;

import static de.ecreators.ai.parent.xor.XorNetworkTraining.inputValues;
import static de.ecreators.ai.parent.xor.XorNeuralNetwork.*;
import static org.hamcrest.CoreMatchers.is;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Test;

import de.ecreators.ai.parent.xor.XorConfigArgs;
import de.ecreators.ai.parent.xor.XorNeuralNetwork;

/**
 * @author bjoern frohberg
 */
public class NeuralNetworkTest {

  @Test
  public void smokeTestWithXor_multipleHiddenLayer() {

    final XorConfigArgs xorConfigArgs = new XorConfigArgs();
    xorConfigArgs.setHiddenLayersCount(2);
    xorConfigArgs.setHiddenLayerNeuronsCount(5);
    xorConfigArgs.setTrainingEta(0.65);
    xorConfigArgs.setTrainingErrorThreshold(0);

    assertXorSmokeTest(xorConfigArgs);
  }

  @Test
  public void smokeTestWithXor_a_hidden_a() {

    final XorConfigArgs xorConfigArgs = new XorConfigArgs();
    xorConfigArgs.setHiddenLayersCount(1);
    xorConfigArgs.setHiddenLayerNeuronsCount(5);
    xorConfigArgs.setTrainingEta(0.65);
    xorConfigArgs.setTrainingErrorThreshold(0);

    assertXorSmokeTest(xorConfigArgs);
  }

  @Test
  public void smokeTestWithXor_a_hidden_b() {

    final XorConfigArgs xorConfigArgs = new XorConfigArgs();
    xorConfigArgs.setHiddenLayersCount(1);
    xorConfigArgs.setHiddenLayerNeuronsCount(10);
    xorConfigArgs.setTrainingEta(0.65);
    xorConfigArgs.setTrainingErrorThreshold(0);

    assertXorSmokeTest(xorConfigArgs);
  }

  @Test
  public void smokeTestWithXor_b() {

    final XorConfigArgs xorConfigArgs = new XorConfigArgs();
    xorConfigArgs.setHiddenLayersCount(1);
    xorConfigArgs.setHiddenLayerNeuronsCount(10);
    xorConfigArgs.setTrainingEta(0.35);
    xorConfigArgs.setTrainingErrorThreshold(0);

    assertXorSmokeTest(xorConfigArgs);
  }

  @Test
  public void smokeTestWithXor_c() {

    final XorConfigArgs xorConfigArgs = new XorConfigArgs();
    xorConfigArgs.setHiddenLayersCount(1);
    xorConfigArgs.setHiddenLayerNeuronsCount(10);
    xorConfigArgs.setTrainingEta(0.15);
    xorConfigArgs.setTrainingErrorThreshold(0);

    assertXorSmokeTest(xorConfigArgs);
  }

  private void assertXorSmokeTest(final XorConfigArgs xorConfigArgs) {
    final XorNeuralNetwork network = new XorNeuralNetwork(xorConfigArgs);
    network.registerTotalErrorListenerHandler((totalError, solved) -> printToLog(network, totalError));
    network.runTraining();

    final double uc000 = Math.round(network.testValues(inputValues(INPUT_A_NAME, 0, INPUT_B_NAME, 0)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc000, is(0d));

    final double uc011 = Math.round(network.testValues(inputValues(INPUT_A_NAME, 0, INPUT_B_NAME, 1)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc011, is(1d));

    final double uc101 = Math.round(network.testValues(inputValues(INPUT_A_NAME, 1, INPUT_B_NAME, 0)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc101, is(1d));

    final double uc110 = Math.round(network.testValues(inputValues(INPUT_A_NAME, 1, INPUT_B_NAME, 1)).get(OUTPUT_XOR_NAME));
    Assert.assertThat(uc110, is(0d));
  }

  private static boolean printToLog(final NeuralNetwork network, final double totalError) {
    final double digitRounding = Math.pow(10, 8);
    final double err = Math.round(totalError * digitRounding) / digitRounding;
    final long generation = network.getOrCreateUpdatedMemory().getNetworkMetaData().getGeneration();
    System.out.println(MessageFormat.format("Total Error: {0} ( generation = {1} )", err, generation));
    return false;
  }
}
