package de.ecreators.ai.parent.xor;

import java.util.HashMap;
import java.util.Map;

import de.ecreators.ai.parent.training.NetworkTraining;
import de.ecreators.ai.parent.training.NetworkUseCase;

/**
 * @author Bjoern Frohberg
 */
public class XorNetworkTraining extends NetworkTraining {

  private static final long serialVersionUID = 6860677541682488651L;
  private final String      propertyBName;
  private final String      outputXorName;
  private final String      propertyAName;

  public XorNetworkTraining(final double eta,
                            final double acceptedTotalErrorThreshold,
                            final String propertyAName,
                            final String propertyBName,
                            final String outputXorName) {
    super(eta, acceptedTotalErrorThreshold, true);
    this.propertyAName = propertyAName;
    this.propertyBName = propertyBName;
    this.outputXorName = outputXorName;
    add(newXorUseCase(0, 0, 0));
    add(newXorUseCase(0, 1, 1));
    add(newXorUseCase(1, 0, 1));
    add(newXorUseCase(1, 1, 0));
  }

  private NetworkUseCase newXorUseCase(final double a, final double b, final double expectedOutput) {
    final NetworkUseCase useCase = new NetworkUseCase();
    useCase.setInputValues(inputValues(this.propertyAName, a, this.propertyBName, b));
    useCase.setExpectedOutputValues(expectedValues(this.outputXorName, expectedOutput));
    return useCase;
  }

  private static Map<String, Double> expectedValues(final String outputXorName, final double expectedOutput) {
    final Map<String, Double> outputProperties = new HashMap<>();
    outputProperties.put(outputXorName, expectedOutput);
    return outputProperties;
  }

  public static Map<String, Double> inputValues(final String propertyAName,
                                                final double a,
                                                final String propertyBName,
                                                final double b) {
    final Map<String, Double> properties = new HashMap<>();
    properties.put(propertyAName, a);
    properties.put(propertyBName, b);
    return properties;
  }
}
