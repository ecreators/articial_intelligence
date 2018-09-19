package de.ecreators.ai.parent.xor;

import java.util.ArrayList;
import java.util.Arrays;

import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.config.NetworkConfig;
import de.ecreators.ai.parent.strategy.INeuronActivationFunction;

/**
 * @author Bjoern Frohberg
 */
public class XorNetworkConfig extends NetworkConfig {

  public XorNetworkConfig(final int hiddenLayers,
                          final int neuronsCountHiddenLayer,
                          final String propertyAName,
                          final String propertyBName,
                          final String outputXorName) {
    super(new XorInputLayerConfig(propertyAName, propertyBName),
          new XorHiddenLayerConfigList(hiddenLayers, neuronsCountHiddenLayer),
          new XorOutputLayerConfig(outputXorName));
  }

  private static class XorInputLayerConfig extends LayerConfig {

    public XorInputLayerConfig(final String propertyAName, final String propertyBName) {
      super(INeuronActivationFunction.IDENTITY,
            2,
            Arrays.asList(propertyAName, propertyBName),
            null);
    }
  }

  private static class XorHiddenLayerConfig extends LayerConfig {

    public XorHiddenLayerConfig(final int neuronsCountHiddenLayer) {
      super(INeuronActivationFunction.SIGMOID, neuronsCountHiddenLayer, null);
    }
  }

  private static class XorOutputLayerConfig extends LayerConfig {

    public XorOutputLayerConfig(final String outputXorName) {
      super(INeuronActivationFunction.SIGMOID, 1, Arrays.asList(outputXorName), null);
    }
  }

  private static class XorHiddenLayerConfigList extends ArrayList<LayerConfig> {

    public XorHiddenLayerConfigList(final int hiddenLayers, final int neuronsCountHiddenLayer) {
      for (int i = 0; i < hiddenLayers; i++) {
        add(new XorHiddenLayerConfig(neuronsCountHiddenLayer));
      }
    }
  }
}
