package de.ecreators.ai.parent.training;

import java.util.ArrayList;

/**
 * @author bjoern frohberg
 */
public class NetworkTraining extends ArrayList<NetworkUseCase> {

  private       double  etaLearningGradient;
  private       double  acceptedTotalErrorThreshold;
  private final boolean roundedOutput;

  public NetworkTraining() {
    this(0.35, 0.05, true);
  }

  public NetworkTraining(final double eta, final double acceptedTotalErrorThreshold, final boolean roundedOutput) {
    this.etaLearningGradient = eta;
    this.acceptedTotalErrorThreshold = acceptedTotalErrorThreshold;
    this.roundedOutput = roundedOutput;
  }

  public double getEtaLearningGradient() {
    return this.etaLearningGradient;
  }

  public double getAcceptedTotalErrorThreshold() {
    return this.acceptedTotalErrorThreshold;
  }

  public boolean roundedOutput() {
    return this.roundedOutput;
  }

  public void setEtaLearningGradient(final double eta) {
    this.etaLearningGradient = eta;
  }

  public void setAcceptedTotalErrorThreshold(final int maxError) {
    this.acceptedTotalErrorThreshold = maxError;
  }
}
