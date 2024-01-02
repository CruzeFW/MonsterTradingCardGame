package at.technikum.apps.mtcg.entity;

import java.util.UUID;

public class Package {

    public String packageId;
    public boolean bought;
    public Package(){
        packageId = null;
        bought = false;
    };

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String createPackageId(){
        return UUID.randomUUID().toString();
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
