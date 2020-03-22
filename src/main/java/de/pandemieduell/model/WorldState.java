package de.pandemieduell.model;

import java.util.List;
import java.util.Map;

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

  Map<Integer, List<Card>> getPlayedCards();
}
