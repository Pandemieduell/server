package de.pandemieduell.model;

import java.util.UUID;

public class Player {
  private String username;
  private String id;

  public Player(String username) {
    this.username = username;
    this.id = UUID.randomUUID().toString();
  }

  public String getUsername() {
    return username;
  }

  public String getId() {
    return id;
  }
}
