package de.ecreators.ai.property.xor;

import java.util.Arrays;
import java.util.List;

import com.sun.istack.internal.Nullable;

import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.network.NeuralNetworkMemory;
import de.ecreators.ai.parent.strategy.INeuronActivationFunction;
import de.ecreators.ai.parent.strategy.NeuralActivators;
import de.ecreators.ai.parent.strategy.PropagationStrategy;
import de.ecreators.ai.parent.utils.MapBuilder;

/**
 * @author Bjoern Frohberg
 */
public class XorAiProperty extends AiProperty {

  public XorAiProperty(@Nullable final NeuralNetworkMemory xorMemory) {
    super(xorMemory, IN_CONFIG, OUT_CONFIG, HIDDEN_CONFIGS, new XorTrainingSet(IN_CONFIG.getNamesArray(), OUT_CONFIG.getNamesArray()));
  }

  private static final String            PROPERTY_NAME_A      = "xor_a";
  private static final String            PROPERTY_NAME_B      = "xor_b";
  private static final String            OUT_NAME_XOR         = "xor";
  private static final LayerConfig       IN_CONFIG            = new LayerConfig(2,
                                                                                NeuralActivators.SIGMOID_ACTIVATOR,
                                                                                1,
                                                                                false,
                                                                                PROPERTY_NAME_A,
                                                                                PROPERTY_NAME_B, INeuronActivationFunction.SIGMOID);
  private static final LayerConfig       OUT_CONFIG           = new LayerConfig(1,
                                                                                NeuralActivators.SIGMOID_ACTIVATOR,
                                                                                1,
                                                                                false,
                                                                                OUT_NAME_XOR, INeuronActivationFunction.SIGMOID);
  private static final int               COUNT_HIDDEN_NEURONS = PropagationStrategy.calculateBestHiddenNeurons(IN_CONFIG, OUT_CONFIG);
  private static final List<LayerConfig> HIDDEN_CONFIGS       = Arrays.asList(new LayerConfig(COUNT_HIDDEN_NEURONS,
                                                                                              NeuralActivators.SIGMOID_ACTIVATOR,
                                                                                              1,
                                                                                              false, INeuronActivationFunction.SIGMOID),
                                                                              new LayerConfig(COUNT_HIDDEN_NEURONS,
                                                                                              NeuralActivators.SIGMOID_ACTIVATOR,
                                                                                              1,
                                                                                              false, INeuronActivationFunction.SIGMOID));

  public boolean isXor(final boolean stateA, final boolean stateB) {
    final MapBuilder<String, Double> mapBuilder = MapBuilder.<String, Double>build()
                                                            .put(PROPERTY_NAME_A, AiProperty.booleanToDouble(stateA))
                                                            .put(PROPERTY_NAME_B, AiProperty.booleanToDouble(stateB));
    return AiProperty.isTrue(getAI().testInput(mapBuilder.toMap()).get(OUT_NAME_XOR));
  }

}
