package de.pandemieduell.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Player {
  @Field("username")
  private String username;

  @Id
  @Field("id")
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
