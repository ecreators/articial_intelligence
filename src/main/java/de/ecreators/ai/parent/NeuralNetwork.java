package de.ecreators.ai.parent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.istack.internal.NotNull;

import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.config.NetworkConfig;
import de.ecreators.ai.parent.config.NetworkMemory;
import de.ecreators.ai.parent.model.NeuralLayer;
import de.ecreators.ai.parent.training.ITotalErrorEventHandler;
import de.ecreators.ai.parent.training.NetworkTraining;
import de.ecreators.ai.parent.training.NetworkUseCase;

/**
 * @author bjoern frohberg
 */
public class NeuralNetwork {

  private final List<NeuralLayer> layers;

  @NotNull
  private NetworkMemory memory;

  private final Set<ITotalErrorEventHandler> totalErrorListeners;

  public NeuralNetwork(final NetworkConfig config) {
    this.layers = new ArrayList<>();
    this.memory = new NetworkMemory();
    this.totalErrorListeners = new LinkedHashSet<>();
    loadConfig(config);
  }

  public void registerTotalErrorListenerHandler(final ITotalErrorEventHandler eventHandler) {
    this.totalErrorListeners.add(eventHandler);
  }

  public void unregisterTotalErrorListenerHandler(final ITotalErrorEventHandler eventHandler) {
    this.totalErrorListeners.remove(eventHandler);
  }

  public void runTrainingSession(final NetworkTraining training, final boolean roundedOutputs) {
    if (this.memory.isSolvedNetwork()) {
      return;
    }

    final double eta = training.getEtaLearningGradient();
    do {
      final double totalError = trainGeneration(training, eta);
      final boolean solvedNetwork = this.memory.isSolvedNetwork();
      notifyTotalErrorUpdated(totalError, solvedNetwork);

    }
    while (!this.memory.isSolvedNetwork());
  }

  private void notifyTotalErrorUpdated(final double totalError, final boolean solved) {
    this.totalErrorListeners.forEach(handler -> handler.onTotalErrorUpdated(totalError, solved));
  }

  public double trainGeneration(final NetworkTraining training, final double eta) {
    final NeuralLayer outputLayer = getOutputLayer();
    for (final NetworkUseCase useCase : training) {
      testValues(useCase.getInputValues());

      outputLayer.trainOutputLayer(eta, useCase.getExpectedOutputValues());

      for (int i = this.layers.size() - 2; i >= 1; i--) {
        final NeuralLayer hiddenLayer = this.layers.get(i);
        hiddenLayer.trainHiddenLayer(eta);
      }

      final List<NeuralLayer> layers = this.layers;
      for (int i = layers.size() - 1; i >= 0; i--) {
        final NeuralLayer layer = layers.get(i);
        layer.applyDeltas();
      }
    }

    final double totalError = outputLayer.getTotalError();
    boolean solved = totalError <= training.getAcceptedTotalErrorThreshold();
    if (!solved && testUseCases(training)) {
      solved = true;
    }
    this.memory.incrementGeneration(solved);

    if (solved) {
      updateMemory();
    }

    return totalError;
  }

  public boolean testUseCases(final NetworkTraining training) {
    boolean validAll = true;
    outerFor:
    for (final NetworkUseCase useCase : training) {
      final Map<String, Double> solvedValues = testValues(useCase.getInputValues());
      for (final Map.Entry<String, Double> output : solvedValues.entrySet()) {
        final String neuronOutputName = output.getKey();
        final double expected = useCase.getExpectedOutputValues().get(neuronOutputName);
        final double value = output.getValue();

        final double missmatch;
        if (training.roundedOutput()) {
          missmatch = Math.abs(Math.round(expected) - Math.round(value));
        }
        else {
          missmatch = Math.abs(expected - value);
        }
        if (missmatch > 0d) {
          validAll = false;
          break outerFor;
        }
      }
    }
    return validAll;
  }

  public Map<String, Double> testValues(final Map<String, Double> inputValues) {
    getInputLayer().setValues(inputValues);
    this.layers.stream().skip(1).forEach(layer -> layer.feedForward());
    return getOutputLayer().getValues();
  }

  private NeuralLayer getInputLayer() {
    return this.layers.get(0);
  }

  private NeuralLayer getOutputLayer() {
    return this.layers.get(this.layers.size() - 1);
  }

  public NetworkMemory updateMemory() {
    this.memory.getNeuronData().clear();
    for (final NeuralLayer layer : this.layers) {
      layer.saveMemory(this.memory);
    }
    return this.memory;
  }

  public void loadMemory(final NetworkMemory memory) {
    this.memory = memory;
    final List<NeuralLayer> layers = this.layers;
    for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
      final NeuralLayer layer = layers.get(layerIndex);
      layer.loadMemory(memory, layerIndex);
    }
  }

  private void loadConfig(final NetworkConfig config) {
    createLayers(config);
    createNeuronBindings();
    loadBindingsByConfig(config);
  }

  private void loadBindingsByConfig(final NetworkConfig networkConfig) {
    for (int layerIndex = 0; layerIndex < this.layers.size(); layerIndex++) {

      final LayerConfig layerConfig = identifyLayerConfigByLayerIndex(networkConfig, layerIndex);

      final NeuralLayer layer = this.layers.get(layerIndex);
      layer.setWeights(layerConfig);
    }
  }

  private LayerConfig identifyLayerConfigByLayerIndex(final NetworkConfig config, final int layerIndex) {
    final LayerConfig layerConfig;
    if (isInputLayer(layerIndex)) {
      layerConfig = config.getInputLayerConfig();
    }
    else if (isOutputLayer(layerIndex)) {
      layerConfig = config.getOutputLayerConfig();
    }
    // hidden layer
    else {
      final int hiddenLayerIndex = layerIndex - 1;
      layerConfig = config.getHiddenLayerConfigs().get(hiddenLayerIndex);
    }
    return layerConfig;
  }

  private boolean isOutputLayer(final int layerIndex) {
    return layerIndex == this.layers.size() - 1;
  }

  private boolean isInputLayer(final int layerIndex) {
    return layerIndex == 0;
  }

  private void createLayers(final NetworkConfig config) {
    this.layers.add(createLayer(config.getInputLayerConfig()));

    final List<LayerConfig> hiddenLayerConfigs = config.getHiddenLayerConfigs();
    for (final LayerConfig hiddenConfig : hiddenLayerConfigs) {
      this.layers.add(createLayer(hiddenConfig));
    }

    this.layers.add(createLayer(config.getOutputLayerConfig()));
  }

  private void createNeuronBindings() {
    final List<NeuralLayer> layers = this.layers;
    for (int layerIndex = 1; layerIndex < layers.size(); layerIndex++) {
      final NeuralLayer parentLayer = layers.get(layerIndex - 1);
      final NeuralLayer activeLayer = layers.get(layerIndex);
      activeLayer.connectNeurons(parentLayer);
    }
  }

  private NeuralLayer createLayer(final LayerConfig config) {
    final NeuralLayer layer = new NeuralLayer();

    final int count = config.getNeuronsCount();
    layer.clearNeurons();
    for (int i = 0; i < count; i++) {
      final String neuronName = config.getNeuronName(i);
      layer.addNeuron(neuronName, config.createNeuron());
    }

    return layer;
  }

  public NetworkMemory getMemory() {
    return this.memory;
  }
}
