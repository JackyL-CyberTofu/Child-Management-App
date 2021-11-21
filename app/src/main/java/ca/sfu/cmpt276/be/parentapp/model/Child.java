package ca.sfu.cmpt276.be.parentapp.model;

import java.util.UUID;

/**
 * Child represents a child in the app.
 */
public class Child {
    private String name;
    private final String id = UUID.randomUUID().toString();

    public Child(String givenName) {
        this.name = givenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getId() {
        return id;
    }
}
