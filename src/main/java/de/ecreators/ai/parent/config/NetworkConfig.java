package de.ecreators.ai.parent.config;

import java.util.List;

/**
 * @author bjoern frohberg
 */
public class NetworkConfig {

  private final LayerConfig       inputLayerConfig;
  private final List<LayerConfig> hiddenLayerConfigs;
  private final LayerConfig       outputLayerConfig;

  public NetworkConfig(final LayerConfig input, final List<LayerConfig> hidden, final LayerConfig output) {
    this.inputLayerConfig = input;
    this.hiddenLayerConfigs = hidden;
    this.outputLayerConfig = output;
  }

  public LayerConfig getInputLayerConfig() {
    return this.inputLayerConfig;
  }

  public List<LayerConfig> getHiddenLayerConfigs() {
    return this.hiddenLayerConfigs;
  }

  public LayerConfig getOutputLayerConfig() {
    return this.outputLayerConfig;
  }
}
