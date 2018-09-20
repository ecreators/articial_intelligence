package de.ecreators.ai.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.ecreators.ai.parent.NeuralNetwork;
import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.config.NetworkConfig;
import de.ecreators.ai.parent.config.NetworkMemory;
import de.ecreators.ai.parent.strategy.INeuronActivationFunction;
import de.ecreators.ai.parent.training.NetworkTraining;

/**
 * @author Bjoern Frohberg
 */
public class SimplePredicationAI {

  private final NeuralNetwork neuralNetwork;

  public SimplePredicationAI(final SimplePredicationAI.InputArgs input,
                             final SimplePredicationAI.HiddenArgs hidden,
                             final SimplePredicationAI.OutputArgs output) {
    final LayerConfig inputs = new LayerConfig(INeuronActivationFunction.IDENTITY, input.getNames().size(), input.getNames(), null);
    final LayerConfig outputs = new LayerConfig(INeuronActivationFunction.SIGMOID, output.getNames().size(), output.getNames(), null);
    final NetworkConfig config = new NetworkConfig(inputs, createHiddenLayers(hidden), outputs);
    this.neuralNetwork = new NeuralNetwork(config);
  }

  public NetworkMemory getMemoryUpdated() {
    this.neuralNetwork.getOrCreateUpdatedMemory();
    return this.neuralNetwork.updateMemory();
  }

  public void loadMemory(final NetworkMemory memory) {
    this.neuralNetwork.loadMemory(memory);
  }

  public void train(final NetworkTraining training, final boolean roundedOutputs) {
    this.neuralNetwork.runTrainingSession(training, roundedOutputs);
  }

  public Map<String, Double> test(final Map<String, Double> values) {
    return this.neuralNetwork.testValues(values);
  }

  private List<LayerConfig> createHiddenLayers(final SimplePredicationAI.HiddenArgs hidden) {
    final List<LayerConfig> hiddens = new ArrayList<>();
    final int count = hidden.getHiddenLayerCount();
    for (int hiddenLayerIndex = 0; hiddenLayerIndex < count; hiddenLayerIndex++) {
      final int neurons = hidden.getHiddenNeuronsForLayer(hiddenLayerIndex);
      hiddens.add(new LayerConfig(INeuronActivationFunction.SIGMOID,
                                  neurons,
                                  null));
    }
    return hiddens;
  }

  public static class InputArgs {

    private final List<String> names;

    public InputArgs() {
      this.names = new ArrayList<>();
    }

    public List<String> getNames() {
      return new ArrayList<>(new HashSet<>(this.names));
    }
  }

  public static abstract class HiddenArgs {

    private int hiddenLayerCount;

    public int getHiddenLayerCount() {
      return this.hiddenLayerCount;
    }

    public void setHiddenLayerCount(final int hiddenLayerCount) {
      this.hiddenLayerCount = hiddenLayerCount;
    }

    public abstract int getHiddenNeuronsForLayer(final int hiddenLayerIndex);
  }

  public static class OutputArgs {

    private final List<String> names;

    public OutputArgs() {
      this.names = new ArrayList<>();
    }

    public List<String> getNames() {
      return this.names;
    }
  }
}
