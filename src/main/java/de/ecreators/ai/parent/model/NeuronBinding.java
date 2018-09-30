package de.ecreators.ai.parent.model;

import de.ecreators.ai.parent.memory.InputBindingData;

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

  // region getters, setters
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

  // endregion

  // region memory: load, save
  public void loadMemory(final InputBindingData data) {
    this.weight = data.getWeight();
    this.weightDelta = data.getWeightDelta();
  }

  public void saveMemory(final InputBindingData bindingData) {
    bindingData.setWeight(this.weight);
    bindingData.setWeightDelta(this.weightDelta);
  }
  // endregion

  public double getOutErrorSignal() {
    return this.outputNeuron.getErrorSignal();
  }

  public void applyDelta() {
    this.weight += this.weightDelta;
  }

  public double getInputValue() {
    return this.inputNeuron.getValue();
  }

}
