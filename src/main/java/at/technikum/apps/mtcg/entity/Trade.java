package at.technikum.apps.mtcg.entity;

public class Trade {
    public Trade(){}
    public Trade(String id, String cardToTrade, String type, float minDamage, boolean completed){
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minDamage = minDamage;
        this.completed = completed;
    }

    public String id;
    public String cardToTrade;
    public String type;
    public float minDamage;
    public boolean completed;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCardToTrade() {
        return cardToTrade;
    }
    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getMinDamage() {
        return minDamage;
    }
    public void setMinDamage(float minDamage) {
        this.minDamage = minDamage;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
