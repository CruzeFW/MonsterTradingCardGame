package at.technikum.apps.mtcg.entity;

public class User {
    private String id;
    private String username;
    private String password;
    private String name;
    private String authorization;
    private String bio;
    private String image;
    private int elo;
    private int coins;


    public User(){ }
    public User(String id, String username, Integer elo){
        this.id = id;
        this.username = username;
        this.elo = elo;
    }
//    public User(String id, String username, String password) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAuthorization() {
        return authorization;
    }
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getElo() {
        return elo;
    }
    public void setElo(int elo) {
        this.elo = elo;
    }
    public Integer getCoins() {
        return coins;
    }
    public void setCoins(Integer coins) {
        this.coins = coins;
    }
}
