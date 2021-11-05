package ca.sfu.cmpt276.be.parentapp.model;

/**
 * Child represents a child in the app.
 */
public class Child {
    private  String name;

    public Child(String givenName) {
        this.name = givenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}
