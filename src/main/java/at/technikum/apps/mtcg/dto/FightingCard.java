package at.technikum.apps.mtcg.dto;

public class FightingCard {
    public String name;
    public String ownerId;
    public String ownerName;
    public float damage;
    public float currentDamage;
    public String type;
    public String element;
    public String special;

    public FightingCard(String name, String ownerId, String ownerName, float damage, float currentDamage, String type, String element, String special) {
        this.name = name;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.damage = damage;
        this.currentDamage = currentDamage;
        this.type = type;
        this.element = element;
        this.special = special;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public float getDamage() {
        return damage;
    }
    public void setDamage(float damage) {
        this.damage = damage;
    }
    public float getCurrentDamage() {
        return currentDamage;
    }
    public void setCurrentDamage(float currentDamage) {
        this.currentDamage = currentDamage;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getElement() {
        return element;
    }
    public void setElement(String element) {
        this.element = element;
    }
    public String getSpecial() {
        return special;
    }
    public void setSpecial(String special) {
        this.special = special;
    }
}
