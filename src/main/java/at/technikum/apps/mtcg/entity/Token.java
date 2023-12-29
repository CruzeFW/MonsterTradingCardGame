package at.technikum.apps.mtcg.entity;

import java.util.UUID;

public class Token {

    private String token;

    private boolean exists;

    //TODO change string into string from the .bat file
    public Token(){
        this.token = null;
        this.exists = false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

}
