package de.ecreators.ai.parent.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bjoern frohberg
 */
public class NetworkMemory {

  private final NetworkMetaData        networkMetaData;
  private final List<List<NeuronData>> neuronData;

  public NetworkMemory() {
    this.networkMetaData = new NetworkMetaData();
    this.neuronData = new ArrayList<>();
  }

  public void incrementGeneration(final boolean solvedNetwork) {
    this.networkMetaData.incrementGeneration(solvedNetwork);
  }

  public NetworkMetaData getNetworkMetaData() {
    return this.networkMetaData;
  }

  public List<List<NeuronData>> getNeuronData() {
    return this.neuronData;
  }

  public boolean isSolvedNetwork() {
    return this.networkMetaData.isSolvedNetwork();
  }
}
