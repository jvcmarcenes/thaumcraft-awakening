package io.github.jvcmarcenes.tca.research;

import java.util.HashMap;
import java.util.Map;

public class PlayerKnowledge {
  
  private final Map<Knowledge, Integer> knowledge;
  
  public PlayerKnowledge() {
    knowledge = new HashMap<>();
  }
  
  public int getKnowledge(Knowledge type) {
    return knowledge.get(type);
  }
  
  public enum Knowledge {
    THEORY_FUNDAMENTALS,
    THEORY_ALCHEMY,
    THEORY_AUROMANCY,
    THEORY_INFUSION,
    
    OBSERVATION_FUNDAMENTALS,
    OBSERVATION_ALCHEMY,
    OBSERVATION_AUROMANCY,
    OBSERVATION_INFUSION,
  }
}
