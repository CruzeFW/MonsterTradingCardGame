package at.technikum.apps.mtcg.dto;

public class TokenRequest {

    public TokenRequest(){ }
    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;

    private String password;

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
