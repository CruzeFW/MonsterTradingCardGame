package at.technikum.apps.mtcg.entity;

public class Battle {

    public Battle(){}
    public Battle(int id, String user1, String user2, String winner, String loser, String log){
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
        this.loser = loser;
        this.log = log;
    }

    public Integer id;
    public String user1;
    public String user2;
    public String winner;
    public String loser;
    public String log;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUser1() {
        return user1;
    }
    public void setUser1(String user1) {
        this.user1 = user1;
    }
    public String getUser2() {
        return user2;
    }
    public void setUser2(String user2) {
        this.user2 = user2;
    }
    public String getWinner() {
        return winner;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }
    public String getLoser() {
        return loser;
    }
    public void setLoser(String loser) {
        this.loser = loser;
    }
    public String getLog() {
        return log;
    }
    public void setLog(String log) {
        this.log = log;
    }
}
