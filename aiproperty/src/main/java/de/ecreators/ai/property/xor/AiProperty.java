package de.ecreators.ai.property.xor;

import java.util.List;

import com.sun.istack.internal.Nullable;

import de.ecreators.ai.parent.NetworkStructure;
import de.ecreators.ai.parent.NeuralNetwork;
import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.network.NeuralNetworkMemory;
import de.ecreators.ai.parent.training.TrainingSet;

/**
 * @author Bjoern Frohberg
 */
public class AiProperty {

  private final NeuralNetwork       ai;
  private final NeuralNetworkMemory memory;

  public AiProperty(@Nullable final NeuralNetworkMemory memory,
                    final LayerConfig inputConfig,
                    final LayerConfig outputConfig,
                    final List<LayerConfig> hiddenConfigs,
                    final TrainingSet trainingSet) {

    this.ai = new NeuralNetwork(new NetworkStructure(inputConfig, hiddenConfigs, outputConfig));

    // training
    if (memory == null) {
      this.memory = new NeuralNetworkMemory();
      this.ai.trainUntil(trainingSet);
    }
    else {
      // load
      this.memory = memory;
      this.ai.loadMemory(this.memory);
    }
  }

  protected static boolean isTrue(final double value) {
    return Math.round(value) == 1L;
  }

  public static double booleanToDouble(final boolean state) {
    return state ? 1 : 0;
  }

  public static int booleanToInt(final boolean state) {
    return state ? 1 : 0;
  }

  public static long booleanToLong(final boolean state) {
    return state ? 1 : 0;
  }

  public static double[] doubles(final double... values) {
    return values;
  }

  public static double normalize(final double lowBound, final double maxBound, final double value) {
    return (value - lowBound) / (maxBound - lowBound);
  }

  protected final NeuralNetwork getAI() {
    return this.ai;
  }
}
