package de.ecreators.ai.parent.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bjoern frohberg
 */
public class NetworkMemory {

  private final MetaData               metaData;
  private final List<List<NeuronData>> neuronData;

  public NetworkMemory() {
    this.metaData = new MetaData();
    this.neuronData = new ArrayList<>();
  }

  public void incrementGeneration(final boolean solvedNetwork) {
    this.metaData.incrementGeneration(solvedNetwork);
  }

  public MetaData getMetaData() {
    return this.metaData;
  }

  public List<List<NeuronData>> getNeuronData() {
    return this.neuronData;
  }

  public boolean isSolvedNetwork() {
    return this.metaData.isSolvedNetwork();
  }
}
