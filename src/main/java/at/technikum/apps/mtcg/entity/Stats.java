package at.technikum.apps.mtcg.entity;

public class Stats {

    public Stats(){};

    public Stats(String user, int elo, int wins, int losses){
        this.user = user;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    public String user;
    public int elo;
    public int wins;
    public int losses;

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public int getElo() {
        return elo;
    }
    public void setElo(int elo) {
        this.elo = elo;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
}
