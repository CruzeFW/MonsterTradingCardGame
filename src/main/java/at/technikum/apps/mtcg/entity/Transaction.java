package at.technikum.apps.mtcg.entity;

import at.technikum.apps.mtcg.controller.TransactionController;

public class Transaction {
    public Transaction(){}
    public  Transaction(int id, String buyer, String packageid){
        this.id = id;
        this.buyer = buyer;
        this.packageid = packageid;
    }

    public int id;
    public String buyer;
    public String packageid;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBuyer() {
        return buyer;
    }
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    public String getPackageid() {
        return packageid;
    }
    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }

}
