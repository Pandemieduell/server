package de.pandemieduell.model;

public interface WorldState {
  long getHealthyPopulation();

  long getInfectedPopulation();

  long getImmunePopulation();

  long getDeadPopulation();

  long getHealthSystemCapacity();

  int getPopulationMorale();

  long getStateAssets();

  double getCaseFatalityRate();

  double getInfectionRate();
}
