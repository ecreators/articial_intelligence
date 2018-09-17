package de.ecreators.ai.parent.training;

import java.util.ArrayList;

/**
 * @author bjoern frohberg
 */
public class NetworkTraining extends ArrayList<NetworkUseCase> {

  private final double etaLearningGradient;
  private final double acceptedTotalErrorThreshold;

  public NetworkTraining() {
    this(0.35, 0.05);
  }

  public NetworkTraining(final double eta, final double acceptedTotalErrorThreshold) {
    this.etaLearningGradient = eta;
    this.acceptedTotalErrorThreshold = acceptedTotalErrorThreshold;
  }

  public double getEtaLearningGradient() {
    return this.etaLearningGradient;
  }

  public double getAcceptedTotalErrorThreshold() {
    return this.acceptedTotalErrorThreshold;
  }
}
