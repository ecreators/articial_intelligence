package de.ecreators.ai.parent.xor;

/**
 * @author Bjoern Frohberg
 */
public class XorConfigArgs {

  private int    hiddenLayersCount;
  private int    hiddenLayerNeuronsCount;
  private double trainingEta;
  private double trainingErrorThreshold;

  public int getHiddenLayersCount() {
    return this.hiddenLayersCount;
  }

  public void setHiddenLayersCount(final int hiddenLayersCount) {
    this.hiddenLayersCount = hiddenLayersCount;
  }

  public int getHiddenLayerNeuronsCount() {
    return this.hiddenLayerNeuronsCount;
  }

  public void setHiddenLayerNeuronsCount(final int hiddenLayerNeuronsCount) {
    this.hiddenLayerNeuronsCount = hiddenLayerNeuronsCount;
  }

  public double getTrainingEta() {
    return this.trainingEta;
  }

  public void setTrainingEta(final double trainingEta) {
    this.trainingEta = trainingEta;
  }

  public double getTrainingErrorThreshold() {
    return this.trainingErrorThreshold;
  }

  public void setTrainingErrorThreshold(final int trainingErrorThreshold) {
    this.trainingErrorThreshold = trainingErrorThreshold;
  }
}
