package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.ResponseParser;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CardService {

    private final UserRepositoryDatabase userRepositoryDatabase;
    private final CardRepositoryDatabase cardRepositoryDatabase;

    public CardService(UserRepositoryDatabase userRepositoryDatabase, CardRepositoryDatabase cardRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.cardRepositoryDatabase = cardRepositoryDatabase;
    }

    // checks for user credentials and then for all cards connected to that user
    public Object[] showAllAcquiredCards(Request request) throws SQLException {
        Object[] arr = new Object[2];
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            arr[0] = 1;
            return arr;
        }
        User foundUser = user.get();
        Optional<ArrayList<Card>> cards;
        cards = cardRepositoryDatabase.findAllCards(foundUser);
        if(cards.isEmpty()){
            arr[0] = 2;
            return arr;
        }

        ArrayList<Card> foundCards = cards.get();
        arr[0] = 0;
        ResponseParser responseParser = new ResponseParser();
        // ja sorry, don't know why
        arr[1] = responseParser.outro(responseParser.cardArrayToString(foundCards, responseParser.intro("All cards of ", foundUser)), "--- end of cards for user ---");

        // remove this or centralize TODO centralize
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.valueToTree(foundCards);
//            String jsonString = objectMapper.writeValueAsString(jsonNode);
//            arr[1]= jsonString;
//        }catch(JsonProcessingException e){
//            throw new RuntimeException(e);
//        }
        return arr;
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? quintet in DeckService + StatsService + ScoreboardService + BattleService
    private Optional<User> checkToken(Request request) throws SQLException {
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
