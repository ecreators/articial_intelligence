package de.ecreators.ai.property.xor;

import static de.ecreators.ai.property.xor.AiProperty.doubles;

import de.ecreators.ai.parent.training.TrainingSet;

/**
 * @author Bjoern Frohberg
 */
public class XorTrainingSet extends TrainingSet {

  public XorTrainingSet(final String[] inNames, final String[] outNames) {
    super(inNames, outNames, TrainingSet.LEARNING_GRADIENT_DEFAULT);

    addCase(doubles(0, 0), doubles(0));
    addCase(doubles(0, 1), doubles(1));
    addCase(doubles(1, 0), doubles(1));
    addCase(doubles(1, 1), doubles(0));

    setStopCondition(stopOnlyWhenValid());
  }
}
