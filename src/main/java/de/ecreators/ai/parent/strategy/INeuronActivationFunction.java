package de.ecreators.ai.parent.strategy;

/**
 * @author bjoern frohberg
 */
public interface INeuronActivationFunction {

  INeuronActivationFunction SIGMOID         = new INeuronActivationFunction.Sigmoid(false);
  INeuronActivationFunction IDENTITY        = new INeuronActivationFunction.Identity();
  INeuronActivationFunction SIGMOID_ROUNDED = new INeuronActivationFunction.Sigmoid(true);

  double activate(double x);

  double derivate(double y);

  class Sigmoid implements INeuronActivationFunction {

    private final boolean rounded;

    public Sigmoid(final boolean rounded) {
      this.rounded = rounded;
    }

    @Override
    public double activate(final double x) {
      final double output = 1 / (1 + Math.exp(-x));
      return this.rounded ? Math.round(output) : output;
    }

    @Override
    public double derivate(final double y) {
      return y * (1 - y);
    }
  }

  class Identity implements INeuronActivationFunction {

    @Override
    public double activate(final double x) {
      return x;
    }

    @Override
    public double derivate(final double y) {
      return 1 - y;
    }
  }
}
