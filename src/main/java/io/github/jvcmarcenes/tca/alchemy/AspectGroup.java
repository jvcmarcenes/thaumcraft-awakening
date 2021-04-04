package io.github.jvcmarcenes.tca.alchemy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.nbt.CompoundNBT;

public class AspectGroup {

  // Default instance of an empty aspect group, do not mutate
  public static final AspectGroup EMPTY = new AspectGroup();

  private final Hashtable<String, Integer> aspects;

  public AspectGroup() {
    aspects = new Hashtable<>();
  }

  // gets the amount of the given aspect, 0 if the aspect is not present
  public int getAspectAmount(String aspect) {
    return aspects.getOrDefault(aspect, 0);
  }

  // return true if the given aspect is present
  public boolean has(String aspect) {
    return aspects.containsKey(aspect);
  }

  // returns true when the given aspect exists, and it's amount is higher than or equal to the requested
  public boolean hasEnough(String aspect, int amount) {
    return has(aspect) && getAspectAmount(aspect) >= amount;
  }

  // adds an amount of aspect on top of an already existing amount, or creates a new entry in the hashtable if the aspect isn't present
  public void add(String aspect, int amount) {
    aspects.put(aspect, getAspectAmount(aspect) + amount);
  }

  // merges with an aspect group, adding all aspects of the given group into this
  public void addAll(AspectGroup newAspects) {
    newAspects.forEach((aspect, amount) -> {
      add(aspect, amount);
    });
  }

  // builder method, adds the given amount to the aspect, and returns itself for method chaining
  public AspectGroup with(String aspect, int amount) {
    add(aspect, amount);
    return this;
  }

  // returns a random aspect that is present, returns NONE if no aspect is present
  public String getRandomAspect() {
    if (aspects.size() == 0) return Aspects.NONE;
    int i = TCA.RANDOM.nextInt(aspects.size());
    return aspects.keySet().stream().collect(Collectors.toList()).get(i);
  }

  // returns a random aspect that is present, and has a minimum amount
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

  // tries to drain the given aspect, and return the given amount. if the aspect is not present, returns 0
  // if there isn't enough it checks the force flag: if it is a forcefull drain, drain all, else returns 0
  public int drain(String aspect, int amount, boolean force) {
    if (!has(aspect)) return 0;

    if (!hasEnough(aspect, amount)) {
      if (force) amount = getAspectAmount(aspect);
      else return 0;
    }

    aspects.put(aspect, getAspectAmount(aspect) - amount);
    if (getAspectAmount(aspect) <= 0) aspects.remove(aspect);

    return amount;
  }

  // drains a whole aspect group from this, used when crafting alchemy recipes
  public void drainAll(AspectGroup toDrain) {
    toDrain.forEach((aspect, amount) -> {
      drain(aspect, amount, true);
    });
  }

  // clear all aspects from the group
  public void clear() {
    aspects.clear();
  }

  // return true if the aspect group has no aspect
  public boolean hasNone() { return aspects.size() == 0; }

  // return the quantity of aspects stored
  public int size() { return aspects.size(); }

  // performs a BiConsumer over all Aspect-Amount pairs present
  public void forEach(BiConsumer<String, Integer> fun) {
    aspects.forEach(fun);
  }

  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();

    nbt.putInt("size", size());

    AtomicInteger i = new AtomicInteger(0);
    forEach((aspect, amount) -> {
      nbt.putString("aspect" + i, aspect);
      nbt.putInt("amount" + i, amount);
      i.incrementAndGet();
    });

    return nbt;
  }

  public void deserializeNBT(CompoundNBT nbt) {
    clear();

    int size = nbt.getInt("size");
    for (int i = 0; i < size; i++) {
      String aspect = nbt.getString("aspect" + i);
      int amount = nbt.getInt("amount" + i);
      aspects.put(aspect, amount);
    }
  }

  @Override
  protected AspectGroup clone() {
    AspectGroup cloned = new AspectGroup();
    aspects.forEach((aspect, amount) -> {
      cloned.add(aspect, amount);
    });
    return cloned;
  }

  @Override
  public String toString() {
    if (hasNone()) return "[NO ASPECTS]";

    AtomicReference<String> msg = new AtomicReference<>("");
    aspects.forEach((aspect, amount) -> msg.set(msg.get() + "[" + amount + " " + aspect + "]"));
    return msg.get();
  }
}
