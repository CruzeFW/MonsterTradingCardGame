package at.technikum.apps.mtcg.entity;


public class Token {

    private String token;

    private boolean exists;

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
