package de.pandemieduell.api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class UserCredentials {
  @Id
  @Field("id")
  private String id;

  @Field("token")
  private String token;

  public UserCredentials(String id, String token) {
    this.id = id;
    this.token = token;
  }

  public String getId() {
    return id;
  }

  public String getToken() {
    return token;
  }
}
