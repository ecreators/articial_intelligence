package de.ecreators.ai.parent.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author bjoern frohberg
 */
public class NetworkMemory {

  private final NetworkMetaData        networkMetaData = new NetworkMetaData();
  private final List<List<NeuronData>> neuronData      = new ArrayList<>();

  public NetworkMemory() {
  }

  public void incrementGeneration(final NetworkMetaData.ESolvation solvedNetwork) {
    this.networkMetaData.incrementGeneration(solvedNetwork);
  }

  public NetworkMetaData getNetworkMetaData() {
    return this.networkMetaData;
  }

  public List<List<NeuronData>> getNeuronData() {
    return this.neuronData;
  }

  @JsonIgnore
  public boolean isSolvedNetwork() {
    return this.networkMetaData.getSolvedNetwork();
  }

  public void forget() {
    this.neuronData.clear();
    this.networkMetaData.clear();
  }

  @JsonIgnore
  public boolean isUnsaved() {
    return this.neuronData.isEmpty() && !this.networkMetaData.getSolvedNetwork();
  }
}
