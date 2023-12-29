package at.technikum.apps.mtcg.entity;

import java.util.UUID;

public class Token {

    private final String token;

    private boolean exists;

    //TODO change string into string from the .bat file
    public Token(){
        this.token = UUID.randomUUID().toString();
        this.exists = false;
    }

    public boolean getExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

}
