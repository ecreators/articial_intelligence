package de.ecreators.ai.parent.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bjoern frohberg
 */
public class NeuronData {

  private String                 neuronName;
  private double                 biasValue;
  private double                 biasDeltaValue;
  private List<InputBindingData> inputBindingData;

  public NeuronData() {
    this.inputBindingData = new ArrayList<>();
  }

  public String getNeuronName() {
    return this.neuronName;
  }

  public void setNeuronName(final String neuronName) {
    this.neuronName = neuronName;
  }

  public double getBiasValue() {
    return this.biasValue;
  }

  public void setBiasValue(final double biasValue) {
    this.biasValue = biasValue;
  }

  public double getBiasDeltaValue() {
    return this.biasDeltaValue;
  }

  public void setBiasDeltaValue(final double biasDeltaValue) {
    this.biasDeltaValue = biasDeltaValue;
  }

  public List<InputBindingData> getInputBindingData() {
    return this.inputBindingData;
  }

  public void setInputBindingData(final List<InputBindingData> inputBindingData) {
    this.inputBindingData = inputBindingData;
  }
}
