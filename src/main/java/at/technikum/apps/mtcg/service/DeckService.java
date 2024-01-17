package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.ResponseParser;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class DeckService {
    public DeckRepositoryDatabase deckRepositoryDatabase = new DeckRepositoryDatabase();
    public UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();

    public DeckService(DeckRepositoryDatabase deckRepositoryDatabase, UserRepositoryDatabase userRepositoryDatabase){
        this.deckRepositoryDatabase = deckRepositoryDatabase;
        this.userRepositoryDatabase = userRepositoryDatabase;
    }

    // get the deck for a user if it exists
    public Object[] getDeck(Request request) throws SQLException {
        Object[] arr = new Object[3];
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            arr[0] = 1;
            return arr;
        }
        User foundUser = user.get();
        Optional<ArrayList<Card>> deck = deckRepositoryDatabase.findDeck(foundUser);
        if(deck.isEmpty()){
            arr[0] = 2;
            return arr;
        }

        ArrayList<Card> foundDeck = deck.get();
        arr[0] = 0;
        ResponseParser responseParser = new ResponseParser();
        if(!request.getRoute().equals("/deck")){
            arr[1] = responseParser.alternativeDeckDisplay(foundDeck);
        }else{
            arr[1] = responseParser.outro(responseParser.cardArrayToString(foundDeck, responseParser.intro("Deck of ", foundUser)), "--- end of deck ---");
        }

        return arr;
    }

    // create the deck and add cards to it
    public Integer createDeck(Request request) throws SQLException {
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            return 3;
        }
        User foundUser = user.get();

        // get values from request into one Card array
        ObjectMapper objectMapper = new ObjectMapper();
        Card[] cards;
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody());
            cards = objectMapper.treeToValue(jsonNode, Card[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // check if deck is the correct size
        if(cards.length != 4){
            return 1;                       // deck doesn't have 4 cards
        }

        // check if the card belongs to the user
        Optional<Card> checkedCard = Optional.empty();
        for(Card checkForAssociation : cards){
            checkedCard = deckRepositoryDatabase.checkCardAssociation(checkForAssociation, foundUser);
            if (checkedCard.isEmpty()){
                return 2;                   // card is not available for that user
            }
        }
        // TODO QUESTION: currently no way to change your deck? needed? not needed?

        // TODO table Deck or not, int hardcode is disgusting and only in our current use case okay-ish
        // add to deck
        for(Card addCard: cards){
            deckRepositoryDatabase.addToDeck(addCard, foundUser);
        }
        return 0;                           // successfully added cards to deck
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? quintet in CardService + StatsService + ScoreboardService + BattleService
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
