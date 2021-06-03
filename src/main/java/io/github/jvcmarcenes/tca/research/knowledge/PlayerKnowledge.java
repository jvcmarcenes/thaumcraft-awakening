package io.github.jvcmarcenes.tca.research.knowledge;

import java.util.HashMap;
import java.util.Map;

public class PlayerKnowledge {
  
  private final Map<String, Integer> research;
  private final Map<Knowledge, Integer> knowledge;
  
  public PlayerKnowledge() {
    knowledge = new HashMap<>();
    research = new HashMap<>();
  }
  
  public int getKnowledge(Knowledge type) {
    return knowledge.get(type);
  }

  public void incKnowledge(Knowledge type, int amount) {
    int curr = getKnowledge(type);
    int next = curr + amount > 0 ? curr + amount : 0;
    knowledge.put(type, next);
  }
}
