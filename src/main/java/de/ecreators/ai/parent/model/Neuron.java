package de.ecreators.ai.parent.model;

import java.util.ArrayList;
import java.util.List;

import de.ecreators.ai.parent.config.InputBindingData;
import de.ecreators.ai.parent.config.LayerConfig;
import de.ecreators.ai.parent.config.NeuronData;
import de.ecreators.ai.parent.strategy.INeuronActivationFunction;

/**
 * @author bjoern frohberg
 */
public class Neuron {

  private double                          value;
  private double                          bias;
  private double                          biasDelta;
  private double                          errorSignal;
  private final List<NeuronBinding>       inputBindings  = new ArrayList<>();
  private final List<NeuronBinding>       outputBindings = new ArrayList<>();
  private final INeuronActivationFunction activator;

  public Neuron(final INeuronActivationFunction activator) {
    this.activator = activator == null ? INeuronActivationFunction.SIGMOID : activator;
  }


  public void learnOutputNeuron(final double eta, final double expectedValue) {
    this.errorSignal = (expectedValue - this.value) * derivate(this.value);
    learn(eta);
  }

  public void learnHiddenNeuron(final double eta) {
    final double sum = this.outputBindings.stream().mapToDouble(b -> b.calculateErrorSignal()).sum();
    this.errorSignal = sum * derivate(this.value);
    learn(eta);
  }

  private void learn(final double eta) {
    this.biasDelta = this.errorSignal * eta;
    for (final NeuronBinding inputBinding : this.inputBindings) {
      inputBinding.updateWeightDeltaByEta(eta);
    }
  }

  public void applyDeltas() {
    this.bias += this.biasDelta;
    this.inputBindings.forEach(b -> b.applyDelta());
  }

  public void updateValue() {
    final double sum = this.inputBindings.stream().mapToDouble(binding -> binding.calculateNetInput()).sum();
    this.value = activate(sum);
  }

  private double activate(final double sum) {
    return this.activator.activate(sum + this.bias);
  }

  public double derivate(final double inputValue) {
    return this.activator.derivate(inputValue);
  }

  public double getValue() {
    return this.value;
  }

  public void setValue(final double value) {
    this.value = value;
  }

  public double getErrorSignal() {
    return this.errorSignal;
  }

  public void setBias(final double bias) {
    this.bias = bias;
  }

  public void setBiasDelta(final double biasDelta) {
    this.biasDelta = biasDelta;
  }

  public void bindWithParentNeuron(final Neuron inputNeuron) {
    final Neuron activeNeuron = this;
    final NeuronBinding activeBinding = new NeuronBinding(inputNeuron, activeNeuron);
    activeNeuron.inputBindings.add(activeBinding);
    inputNeuron.outputBindings.add(activeBinding);
  }

  public void setWeights(final LayerConfig config) {
    this.inputBindings.forEach(b -> b.setWeight(config.getNextWeight()));
  }

  public void loadMemory(final NeuronData data) {
    this.bias = data.getBiasValue();
    this.biasDelta = data.getBiasDeltaValue();

    final List<NeuronBinding> inputBindings = this.inputBindings;
    for (int bindingIndex = 0; bindingIndex < inputBindings.size(); bindingIndex++) {
      final NeuronBinding inputBinding = inputBindings.get(bindingIndex);
      final InputBindingData bindingData = data.getInputBindingData().get(bindingIndex);
      inputBinding.loadMemory(bindingData);
    }
  }

  public void saveMemory(final NeuronData neuronData) {
    neuronData.getInputBindingData().clear();
    for (final NeuronBinding inputBinding : this.inputBindings) {
      final InputBindingData bindingData = new InputBindingData();
      inputBinding.saveMemory(bindingData);
      neuronData.getInputBindingData().add(bindingData);
    }
  }
}
