package de.ecreators.ai.parent.xor;

import de.ecreators.ai.parent.NeuralNetwork;
import de.ecreators.ai.parent.training.NetworkTraining;

/**
 * @author Bjoern Frohberg
 */
public class XorNeuralNetwork extends NeuralNetwork {

  public static final String INPUT_A_NAME    = "a";
  public static final String INPUT_B_NAME    = "b";
  public static final String OUTPUT_XOR_NAME = "xor";

  private final NetworkTraining xorTraining;

  public XorNeuralNetwork(final XorConfigArgs xorConfigArgs) {
    this(xorConfigArgs.getHiddenLayersCount(),
         xorConfigArgs.getHiddenLayerNeuronsCount(),
         xorConfigArgs.getTrainingEta(),
         xorConfigArgs.getTrainingErrorThreshold());
  }

  public XorNeuralNetwork(final int hiddenLayers, final int neuronsCountHiddenLayer, final double eta, final double errorThreshold) {
    super(new XorNetworkConfig(hiddenLayers, neuronsCountHiddenLayer, INPUT_A_NAME, INPUT_B_NAME, OUTPUT_XOR_NAME));
    this.xorTraining = new XorNetworkTraining(eta, errorThreshold, INPUT_A_NAME, INPUT_B_NAME, OUTPUT_XOR_NAME);
    setName("XOR");
  }

  public NetworkTraining getXorTraining() {
    return this.xorTraining;
  }

  public void runTraining() {
    runTrainingSession(this.xorTraining, true);
  }
}
