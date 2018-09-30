package de.ecreators.ai.parent.model;

import java.util.*;

import javax.management.AttributeNotFoundException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.memory.NetworkMemory;
import de.ecreators.ai.parent.memory.NeuronData;

/**
 * @author bjoern frohberg
 */
public class NeuralLayer {

  private final Map<String, Neuron> neurons;

  public NeuralLayer() {
    this.neurons = new LinkedHashMap<>();
  }

  public void feedForward() {
    this.neurons.values().forEach(n -> n.updateValue());
  }

  public double getTotalError() {
    final Collection<Neuron> neurons = this.neurons.values();
    double sum = 0.0;
    for (final Neuron n : neurons) {
      sum += Math.pow(n.getErrorSignal(), 2) * 0.5;
    }
    return sum;
  }

  public void trainOutputLayer(final double eta, final Map<String, Double> expectedValues) {
    this.neurons.forEach((neuronName, neuron) -> neuron.learnOutputNeuron(eta, expectedValues.get(neuronName)));
  }

  public void trainHiddenLayer(final double eta) {
    this.neurons.forEach((k, v) -> v.learnHiddenNeuron(eta));
  }

  public void setValues(final Map<String, Double> values) {
    values.forEach((propertyName, normalizedValue) -> setValue(propertyName, normalizedValue));
  }

  public Map<String, Double> getValues() {
    final Map<String, Double> map = new LinkedHashMap<>();
    for (final Map.Entry<String, Neuron> e : this.neurons.entrySet()) {
      final Neuron neuron = e.getValue();
      map.put(e.getKey(), neuron.getValue());
    }
    return map;
  }

  public void clearNeurons() {
    this.neurons.clear();
  }

  public void addNeuron(final String name, final Neuron neuron) {
    this.neurons.put(name, neuron);
  }

  public void connectNeurons(final NeuralLayer parentLayer) {
    final Collection<Neuron> activeNeurons = this.neurons.values();
    for (final Neuron inputNeuron : parentLayer.neurons.values()) {
      for (final Neuron activeNeuron : activeNeurons) {
        activeNeuron.bindWithParentNeuron(inputNeuron);
      }
    }
  }

  public void setWeights(final LayerConfig config) {
    this.neurons.values().forEach(n -> n.setWeights(config));
  }

  public void saveMemory(final NetworkMemory memory) {
    final List<NeuronData> layerData = new ArrayList<>();

    final Set<Map.Entry<String, Neuron>> namedNeurons = this.neurons.entrySet();
    for (final Map.Entry<String, Neuron> neuronEntry : namedNeurons) {
      final Neuron neuron = neuronEntry.getValue();

      final NeuronData neuronData = new NeuronData();
      neuronData.setNeuronName(neuronEntry.getKey());
      neuron.saveMemory(neuronData);

      layerData.add(neuronData);
    }
    memory.getNeuronData().add(layerData);
  }

  public void loadMemory(final NetworkMemory memory, final int selfLayerIndex) {

    if (memory.isUnsaved()) {
      saveMemory(memory);
    }

    final Set<Map.Entry<String, Neuron>> neurons = this.neurons.entrySet();
    final List<NeuronData> neuronsData = memory.getNeuronData().get(selfLayerIndex);
    for (final Map.Entry<String, Neuron> e : neurons) {
      final String neuronName = e.getKey();

      boolean found = false;
      for (final NeuronData data : neuronsData) {
        if (Objects.equals(data.getNeuronName(), neuronName)) {
          final Neuron neuron = e.getValue();
          neuron.loadMemory(data);
          found = true;
          break;
        }
      }

      if (!found) {
        handleNotFoundDataMemory(neuronName);
      }
    }
  }

  private void handleNotFoundDataMemory(final String neuronName) {
    // exception or logging here
    final String message = "Could not find data in memory for neuron with name: " + neuronName;
    final AttributeNotFoundException exception = new AttributeNotFoundException(message);
    final String exceptionText = ExceptionUtils.getStackTrace(exception);
    System.err.println(exceptionText);
  }

  public void applyDeltas() {
    this.neurons.values().forEach(neuron -> neuron.applyDeltas());
  }

  public void setValue(final String propertyName, final double normalizedValue) {
    final Neuron neuron = this.neurons.get(propertyName);
    neuron.setValue(normalizedValue);
  }
}
