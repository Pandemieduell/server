package de.pandemieduell.api;

public class UserCredentials {
  private String id;
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
