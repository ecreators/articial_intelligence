package de.ecreators.ai.parent.config;

/**
 * @author bjoern frohberg
 */
public class InputBindingData {

  private double weight;
  private double weightDelta;

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
}
