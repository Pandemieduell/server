package de.pandemieduell.model;

import de.pandemieduell.cards.ExecutableCard;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

public class Card {
  @Field("cardClass")
  private String cardClass;

  @Field("roundPlayed")
  private int roundPlayed;

  @Id
  @Field("id")
  private int id;

  public Card(final String cardClass, final int id) {
    this.cardClass = cardClass;
    this.roundPlayed = -1;
    this.id = id;
  }

  @PersistenceConstructor
  public Card(final String cardClass, final int id, final int roundPlayed) {
    this.cardClass = cardClass;
    this.roundPlayed = roundPlayed;
    this.id = id;
  }

  public void play(int roundPlayed) {
    this.roundPlayed = roundPlayed;
  }

  public Class<? extends ExecutableCard> getCardClass() throws ClassNotFoundException {
    Class entityClass = Class.forName(this.cardClass);
    return (Class<ExecutableCard>) entityClass;
  }

  public int getRoundPlayed() {
    return roundPlayed;
  }

  public int getId() {
    return id;
  }
}
