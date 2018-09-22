package de.ecreators.ai.parent.config;

/**
 * @author bjoern frohberg
 */
public class NetworkMetaData {

  private String                     networkName;
  private long                       generation;
  private boolean                    solvedNetwork;
  private NetworkMetaData.ESolvation solvation;

  public NetworkMetaData() {
    this.solvation = NetworkMetaData.ESolvation.UNSOLVED;
  }

  public NetworkMetaData.ESolvation getSolvation() {
    return this.solvation;
  }

  public String getNetworkName() {
    return this.networkName;
  }

  public void setNetworkName(final String networkName) {
    this.networkName = networkName;
  }

  public long getGeneration() {
    return this.generation;
  }

  public void setGeneration(final long generation) {
    this.generation = generation;
  }

  public void incrementGeneration(final NetworkMetaData.ESolvation solvedNetwork) {
    this.generation++;
    setSolvedNetwork(solvedNetwork.isSolvedState());
    setSolvation(solvedNetwork);
  }

  public boolean getSolvedNetwork() {
    return this.solvedNetwork;
  }

  public void setSolvedNetwork(final boolean solvedNetwork) {
    this.solvedNetwork = solvedNetwork;
  }

  public void setSolvation(final NetworkMetaData.ESolvation solvation) {
    this.solvation = solvation;
  }

  public void clear() {
    this.generation = 0;
    this.solvedNetwork = false;
  }

  public enum ESolvation {
    UNSOLVED(false),
    SOLVED_BY_TOTAL_ERROR(true),
    SOLVED_BY_USE_CASES_VALID(true);

    private final boolean solvedState;

    ESolvation(final boolean solvedState) {
      this.solvedState = solvedState;
    }

    public boolean isSolvedState() {
      return this.solvedState;
    }
  }
}
