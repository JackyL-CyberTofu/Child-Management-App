package ca.sfu.cmpt276.be.parentapp.model;

/**
 * Child represents a child in the app.
 */
public class Child {
    private final String name;

    public Child(String givenName) {
        this.name = givenName;
    }

    public String getName() {
        return name;
    }
}
