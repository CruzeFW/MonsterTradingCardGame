package at.technikum.apps.mtcg.entity;

public class Card {

    public String id;
    public String name;
    public float damage;
    public String owner;
    public String packageid;

    public Card(){};

    public Card(String id, String name, float damage, String owner, String packageid){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.owner = owner;
        this.packageid = packageid;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getDamage() {
        return damage;
    }
    public void setDamage(float damage) {
        this.damage = damage;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getPackageid() {
        return packageid;
    }
    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }
}
