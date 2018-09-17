package de.ecreators.ai.parent.config;

/**
 * @author bjoern frohberg
 */
public class MetaData {

  private String  networkName;
  private long    generation;
  private boolean solvedNetwork;

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

  public void incrementGeneration(final boolean solvedNetwork) {
    this.generation++;
    this.solvedNetwork = solvedNetwork;
  }

  public boolean isSolvedNetwork() {
    return this.solvedNetwork;
  }

  public void setSolvedNetwork(final boolean solvedNetwork) {
    this.solvedNetwork = solvedNetwork;
  }
}
