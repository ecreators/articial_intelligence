package de.ecreators.ai.parent.training;

/**
 * @author bjoern frohberg
 */
public interface ITotalErrorEventHandler {

  void onTotalErrorUpdated(double totalError, boolean solved);
}
