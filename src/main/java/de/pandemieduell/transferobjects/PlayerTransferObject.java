package de.pandemieduell.transferobjects;

public class PlayerTransferObject {
    public String name;
    public String id;
    public String token;

    public PlayerTransferObject(String name, String id, String token) {
        this.name = name;
        this.id = id;
        this.token = token;
    }
}
