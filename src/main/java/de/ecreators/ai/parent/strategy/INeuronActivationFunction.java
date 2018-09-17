package de.ecreators.ai.parent.strategy;

/**
 * @author bjoern frohberg
 */
public interface INeuronActivationFunction {

  INeuronActivationFunction SIGMOID  = new INeuronActivationFunction.Sigmoid();
  INeuronActivationFunction IDENTITY = new INeuronActivationFunction.Identity();

  double activate(double x);

  double derivate(double y);

  class Sigmoid implements INeuronActivationFunction {

    @Override
    public double activate(final double x) {
      return 1 / (1 + Math.exp(-x));
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
