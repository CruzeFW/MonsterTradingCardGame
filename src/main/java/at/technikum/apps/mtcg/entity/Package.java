package at.technikum.apps.mtcg.entity;

import java.util.UUID;

public class Package {

    public String packageId;
    public Package(){
        packageId = createPackageId();
    };

    public String getPackageId() {
        return packageId;
    }

    private String createPackageId(){
        return UUID.randomUUID().toString();
    }
}
