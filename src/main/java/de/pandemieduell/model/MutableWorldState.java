package de.pandemieduell.model;

public interface MutableWorldState extends WorldState {
  void infectPeople(long amount) throws IllegalArgumentException;

  void healInfectedPeople(long amount) throws IllegalArgumentException;

  void vaccinateHealthyPeople(long amount) throws IllegalArgumentException;

  void killPeople(long amount) throws IllegalArgumentException;

  void setHealthSystemCapacity(long healthSystemCapacity);

  void setPopulationMorale(int populationMorale);

  void setStateAssets(long stateAssets);

  void setCaseFatalityRate(double caseFatalityRate);

  void setInfectionRate(double infectionRate);
}
