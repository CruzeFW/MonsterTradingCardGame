package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.PackageRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Optional;

public class PackageService {
    private final PackageRepositoryDatabase packageRepositoryDatabase;
    private final CardRepositoryDatabase cardRepositoryDatabase;

    public PackageService(PackageRepositoryDatabase packageRepositoryDatabase, CardRepositoryDatabase cardRepositoryDatabase) {
        this.packageRepositoryDatabase = packageRepositoryDatabase;
        this.cardRepositoryDatabase = cardRepositoryDatabase;
    }

    // add new package with all cards into DB; if one card already exists, it is skipped
    public Integer postMethodCalled(Request request) throws SQLException {
        int i = checkToken(request);
        if( i == 1){                // no token
            return 1;
        } else if (i == 2) {        // user not found/not logged in
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
        pack.setPackageId(pack.createPackageId());
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

    // check if a token is given and if it's the admin token
    private Integer checkToken(Request request) throws SQLException {
        UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();
        if (request.getAuthorization() == null) {
            return 1;           // no token
        } else {
            User findUser = new User();
            findUser.setAuthorization(request.getAuthorization());
            Optional <User> user = userRepositoryDatabase.findWithToken(findUser);

            if(user.isEmpty()){
                return 2;       // no user with that token exists
            }
            return 0;           // correct authentication
        }
    }
}
