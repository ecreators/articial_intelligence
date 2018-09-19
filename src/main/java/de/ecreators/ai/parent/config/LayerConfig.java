package de.ecreators.ai.parent.config;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.ecreators.ai.parent.model.Neuron;
import de.ecreators.ai.parent.strategy.INeuronActivationFunction;

/**
 * @author bjoern frohberg
 */
public class LayerConfig {

  private final INeuronActivationFunction activator;
  private final double                    bias;
  private int                             neuronsCount;
  private final List<String>              neuronNames;
  private final Double                    weights;
  private static final Random             random = new SecureRandom();

  public LayerConfig(final INeuronActivationFunction activator, final int neuronsCount, final Double weights) {
    this(activator, neuronsCount, Collections.emptyList(), weights);
  }

  public LayerConfig(final INeuronActivationFunction activator,
                     final int neuronsCount,
                     final List<String> neuronNames,
                     final Double weights) {
    this.neuronsCount = neuronsCount;
    this.neuronNames = new ArrayList<>();
    this.neuronNames.addAll(neuronNames);
    this.weights = weights;
    this.bias = 1d;
    this.activator = activator;
  }

  public void defineNeuron(final String name) {
    this.neuronsCount++;
    if (name != null) {
      this.neuronNames.add(name);
    }
  }

  public Neuron createNeuron() {
    final Neuron neuron = new Neuron(this.activator);
    neuron.setBias(this.bias);
    return neuron;
  }

  public int getNeuronsCount() {
    return this.neuronsCount;
  }

  public String getNeuronName(final int neuronIndex) {
    return this.neuronNames.isEmpty() ? String.valueOf(neuronIndex) : this.neuronNames.get(neuronIndex);
  }

  public double getNextWeight() {
    return this.weights == null ? random.nextDouble() : this.weights;
  }

  public List<String> getNames() {
    return this.neuronNames;
  }
}
