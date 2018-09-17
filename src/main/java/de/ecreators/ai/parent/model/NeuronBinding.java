package de.ecreators.ai.parent.model;

import de.ecreators.ai.parent.config.InputBindingData;

/**
 * @author bjoern frohberg
 */
public class NeuronBinding {

  private final Neuron inputNeuron;
  private final Neuron outputNeuron;
  private double       weight;
  private double       weightDelta;

  public NeuronBinding(final Neuron inputNeuron, final Neuron outputNeuron) {

    this.inputNeuron = inputNeuron;
    this.outputNeuron = outputNeuron;
  }

  public double getWeight() {
    return this.weight;
  }

  public void setWeight(final double weight) {
    this.weight = weight;
  }

  public double getWeightDelta() {
    return this.weightDelta;
  }

  public void setWeightDelta(final double weightDelta) {
    this.weightDelta = weightDelta;
  }

  public void updateWeightDeltaByEta(final double eta) {
    final double inputValue = this.inputNeuron.getValue();
    // TODO unsure "inputValue" and "derivate" -> from output or input neuron or whenever
    this.weightDelta = eta * this.outputNeuron.derivate(inputValue) * this.outputNeuron.getErrorSignal();
  }

  public void applyDelta() {
    this.weight += this.weightDelta;
  }

  public double calculateNetInput() {
    return this.weight * this.inputNeuron.getValue();
  }

  public double calculateErrorSignal() {
    // TODO unsure only error signal or multiplied with something other
    return this.outputNeuron.getErrorSignal();
  }

  public void loadMemory(final InputBindingData data) {
    this.weight = data.getWeight();
    this.weightDelta = data.getWeightDelta();
  }

  public void saveMemory(final InputBindingData bindingData) {
    bindingData.setWeight(this.weight);
    bindingData.setWeightDelta(this.weightDelta);
  }
}
