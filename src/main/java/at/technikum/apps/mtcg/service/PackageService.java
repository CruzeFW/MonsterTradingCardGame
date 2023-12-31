package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.PackageRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

public class PackageService {
    private final PackageRepositoryDatabase packageRepositoryDatabase;

    public PackageService(PackageRepositoryDatabase packageRepositoryDatabase) {
        this.packageRepositoryDatabase = packageRepositoryDatabase;
    }

    public Integer postMethodCalled(Request request){
        int i = checkToken(request);
        if( i == 1){                // no token
            return 1;
        } else if (i == 2) {        // not admin
            return 2;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Card[] cards;
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody());
            cards = objectMapper.treeToValue(jsonNode, Card[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<Card> found;
        int counter = 0;
        String packageId = createPackageId();
        for (Card card:cards){
            found = packageRepositoryDatabase.find(card);
            if(found.isEmpty()){
                packageRepositoryDatabase.save(card, packageId);
            }else{
                ++counter;
            }
            found = Optional.empty();
        }

        if (counter != 0){
            return 3;
        } else {
            return 0;
        }
    }

    //TODO QEUSTION: maybe add get token for admin from DB? security risk?
    private Integer checkToken(Request request){
        if(request.getAuthorization() == null){
            return 1;           // no token
        } else if(!request.getAuthorization().split("-")[0].equals("admin")) {
            return 2;           // no header
        }
        return 0;               // correct header admin-mtcgToken
    }

    private String createPackageId(){
        return UUID.randomUUID().toString();
    }
}
