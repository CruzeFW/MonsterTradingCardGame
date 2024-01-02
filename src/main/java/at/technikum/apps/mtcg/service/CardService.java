package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Optional;

public class CardService {

    private final UserRepositoryDatabase userRepositoryDatabase;

    public CardService(UserRepositoryDatabase userRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
    }

    // checks for user credentials and then for all cards connected to that user
    public Object[] showAllAcquiredCards(Request request){
        Object[] arr = new Object[2];
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            arr[0] = 1;
            return arr;
        }
        User foundUser = user.get();
        Optional<ArrayList<Card>> cards;
        cards = userRepositoryDatabase.findAllCards(foundUser);
        if(cards.isEmpty()){
            arr[0] = 2;
            return arr;
        }

        ArrayList<Card> foundCards = cards.get();
        arr[0] = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(foundCards);
            String jsonString = objectMapper.writeValueAsString(jsonNode);
            arr[1]= jsonString;
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return arr;
    }

    // check if a given token is connected to a user
    private Optional<User> checkToken(Request request) {
        Optional<User> foundUser = Optional.empty();
        if (request.getAuthorization() == null) {
            return foundUser;           // no token
        } else {
            User findUser = new User();
            findUser.setAuthorization(request.getAuthorization());
            Optional<User> user = userRepositoryDatabase.findWithToken(findUser);

            if(user.isEmpty()){
                return foundUser;       // no user with that token exists
            }
            return user;           // correct authentication
        }
    }
}