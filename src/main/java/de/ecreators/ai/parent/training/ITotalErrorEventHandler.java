package de.ecreators.ai.parent.training;

/**
 * @author bjoern frohberg
 */
public interface ITotalErrorEventHandler {

  boolean onTotalErrorUpdated(double totalError, boolean solved);
}
