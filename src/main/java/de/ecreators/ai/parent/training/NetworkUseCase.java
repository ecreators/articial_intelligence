package de.ecreators.ai.parent.training;

import java.util.Map;

/**
 * @author bjoern frohberg
 */
public class NetworkUseCase {

  private Map<String, Double> inputValues;
  private Map<String, Double> expectedOutputValues;

  public Map<String, Double> getInputValues() {
    return this.inputValues;
  }

  public void setInputValues(final Map<String, Double> inputValues) {
    this.inputValues = inputValues;
  }

  public Map<String, Double> getExpectedOutputValues() {
    return this.expectedOutputValues;
  }

  public void setExpectedOutputValues(final Map<String, Double> expectedOutputValues) {
    this.expectedOutputValues = expectedOutputValues;
  }
}
