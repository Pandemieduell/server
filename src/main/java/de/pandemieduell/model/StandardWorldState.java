package de.pandemieduell.model;

import org.springframework.boot.actuate.endpoint.web.Link;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

  private Map<Integer, List<Card>> playedCardsPerRound;

  public StandardWorldState() {
    this.healthyPopulation = 7000000000L;
    this.infectedPopulation = 1L;
    this.immunePopulation = 0L;
    this.deadPopulation = 0L;
    this.healthSystemCapacity = 28000L;
    this.populationMorale = 100;
    this.stateAssets = 123456789L;
    this.caseFatalityRate = 0.2;
    this.infectionRate = 1.2;
    this.playedCardsPerRound = new HashMap<>();
  }

  @Override
  public void infectPeople(long amount) throws IllegalArgumentException {
    if (this.healthyPopulation - amount < 0) throw new IllegalArgumentException();
    this.healthyPopulation -= amount;
    this.infectedPopulation += amount;
  }

  @Override
  public void healInfectedPeople(long amount) throws IllegalArgumentException {
    if (this.infectedPopulation - amount < 0) throw new IllegalArgumentException();
    this.infectedPopulation -= amount;
    this.immunePopulation += amount;
  }

  @Override
  public void vaccinateHealthyPeople(long amount) throws IllegalArgumentException {
    if (this.healthyPopulation - amount < 0) throw new IllegalArgumentException();
    this.healthyPopulation -= amount;
    this.immunePopulation += amount;
  }

  @Override
  public void killPeople(long amount) throws IllegalArgumentException {
    if (this.infectedPopulation - amount < 0) throw new IllegalArgumentException();
    this.infectedPopulation -= amount;
    this.deadPopulation += amount;
  }

  @Override
  public long getHealthyPopulation() {
    return healthyPopulation;
  }

  @Override
  public long getInfectedPopulation() {
    return infectedPopulation;
  }

  @Override
  public long getImmunePopulation() {
    return immunePopulation;
  }

  @Override
  public long getDeadPopulation() {
    return deadPopulation;
  }

  @Override
  public long getHealthSystemCapacity() {
    return healthSystemCapacity;
  }

  @Override
  public int getPopulationMorale() {
    return populationMorale;
  }

  @Override
  public long getStateAssets() {
    return stateAssets;
  }

  @Override
  public double getCaseFatalityRate() {
    return caseFatalityRate;
  }

  @Override
  public double getInfectionRate() {
    return infectionRate;
  }

  @Override
  public void setHealthSystemCapacity(long healthSystemCapacity) {
    this.healthSystemCapacity = healthSystemCapacity;
  }

  @Override
  public void setPopulationMorale(int populationMorale) {
    this.populationMorale = populationMorale;
  }

  @Override
  public void setStateAssets(long stateAssets) {
    this.stateAssets = stateAssets;
  }

  @Override
  public void setCaseFatalityRate(double caseFatalityRate) {
    this.caseFatalityRate = caseFatalityRate;
  }

  @Override
  public void addPlayedCard(final Integer round, final Card card) {
    List<Card> playedCards = this.playedCardsPerRound.getOrDefault(round, new LinkedList<>());

    playedCards.add(card);
    this.playedCardsPerRound.put(round, playedCards);
  }

  @Override
  public Map<Integer, List<Card>> getPlayedCards() {
    return this.playedCardsPerRound;
  }

  @Override
  public void setInfectionRate(double infectionRate) {
    this.infectionRate = infectionRate;
  }
}
