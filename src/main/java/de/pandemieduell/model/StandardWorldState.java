package de.pandemieduell.model;

public class StandardWorldState implements MutableWorldState {
  // Zustandseigenschaften
  private long healthyPopulation;
  private long infectedPopulation;
  private long immunePopulation;
  private long deadPopulation;

  private long healthSystemCapacity;
  private int populationMorale;
  private long stateAssets;

  // Zustandsübergangseigenschaften
  private double caseFatalityRate;
  private double infectionRate;
}
