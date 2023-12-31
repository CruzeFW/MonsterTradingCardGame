package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.PackageRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class PackageService {
    private final PackageRepositoryDatabase packageRepositoryDatabase;
    private final CardRepositoryDatabase cardRepositoryDatabase;

    public PackageService(PackageRepositoryDatabase packageRepositoryDatabase, CardRepositoryDatabase cardRepositoryDatabase) {
        this.packageRepositoryDatabase = packageRepositoryDatabase;
        this.cardRepositoryDatabase = cardRepositoryDatabase;
    }

    // add new package with all cards into DB; if one card already exists, it is skipped
    public Integer postMethodCalled(Request request){
        int i = checkToken(request);
        if( i == 1){                // no token
            return 1;
        } else if (i == 2) {        // not admin
            return 2;
        }

        // get values from request into one Card array
        ObjectMapper objectMapper = new ObjectMapper();
        Card[] cards;
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody());
            cards = objectMapper.treeToValue(jsonNode, Card[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // create a new Package
        Package pack = new Package();
        packageRepositoryDatabase.save(pack.getPackageId());

        // check if cards are already in db, otherwise add it to db
        Optional<Card> found;
        int counter = 0;
        for (Card card:cards){
            found = cardRepositoryDatabase.find(card);
            if(found.isEmpty()){
                cardRepositoryDatabase.save(card, pack.getPackageId());
            }else{
                ++counter;
            }
            found = Optional.empty();
        }

        // if all 5 are already in the db, delete the created package as it is empty
        if(counter == 5){
            packageRepositoryDatabase.delete(pack.getPackageId());
        }
        if (counter != 0){
            return 3;
        } else {
            return 0;
        }
    }

    // check if a token is given and if its the admin token
    //TODO QUESTION: maybe add get token for admin from DB? security risk?
    private Integer checkToken(Request request){
        if(request.getAuthorization() == null){
            return 1;           // no token
        } else if(!request.getAuthorization().split("-")[0].equals("admin")) {
            return 2;           // no header
        }
        return 0;               // correct header admin-mtcgToken
    }
}
