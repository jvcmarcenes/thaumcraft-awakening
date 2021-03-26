package io.github.jvcmarcenes.tca.alchemy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import io.github.jvcmarcenes.tca.TCA;

public class AspectGroup {

  public static final AspectGroup EMPTY = new AspectGroup();

  private final Hashtable<String, Integer> aspects;

  public AspectGroup() {
    aspects = new Hashtable<>();
  }

  public int getAspectAmount(String aspect) {
    return aspects.getOrDefault(aspect, 0);
  }

  public boolean hasEnough(String aspect, int amount) {
    return getAspectAmount(aspect) >= amount;
  }

  public void add(String aspect, int amount) {
    aspects.put(aspect, getAspectAmount(aspect) + amount);
  }

  public void addAll(AspectGroup newAspects) {
    newAspects.forEach((aspect, amount) -> {
      add(aspect, amount);
    });
  }

  public AspectGroup with(String aspect, int amount) {
    add(aspect, amount);
    return this;
  }

  public String getRandomAspect() {
    if (aspects.size() == 0) return Aspects.NONE;
    int i = TCA.RANDOM.nextInt(aspects.size());
    return aspects.keySet().stream().collect(Collectors.toList()).get(i);
  }

  public String getRandomAspectMin(int min) {
    ArrayList<String> filteredAspects = new ArrayList<>();
    aspects.keySet().stream()
      .filter(aspect -> hasEnough(aspect, min))
      .collect(Collectors.toList())
      .forEach(aspect -> filteredAspects.add(aspect));

    if (filteredAspects.size() == 0) return Aspects.NONE;
    int i = TCA.RANDOM.nextInt(filteredAspects.size());
    return filteredAspects.get(i);
  }

  public int drain(String aspect, int amount) {
    if (!hasEnough(aspect, amount)) return 0;

    aspects.put(aspect, getAspectAmount(aspect) - amount);
    if (aspects.get(aspect) <= 0) aspects.remove(aspect);
    return amount;
  }

  public void drainAll(AspectGroup toDrain) {
    toDrain.forEach((aspect, amount) -> {
      drain(aspect, amount);
    });
  }

  public void clear() {
    aspects.clear();
  }

  public boolean hasNone() { return aspects.size() == 0; }

  public int size() { return aspects.size(); }

  @Override
  protected AspectGroup clone() {
    AspectGroup cloned = new AspectGroup();
    aspects.forEach((aspect, amount) -> {
      cloned.add(aspect, amount);
    });
    return cloned;
  }

  public void forEach(BiConsumer<String, Integer> fun) {
    aspects.forEach(fun);
  }

  @Override
  public String toString() {
    if (hasNone())
      return "[NO ASPECTS]";
    else {
      AtomicReference<String> msg = new AtomicReference<>("");
      aspects.forEach((aspect, amount) -> {
        msg.set(msg.get() + "[" + aspect + " " + amount + "]");
      });
      return msg.get();
    }
  }
}
