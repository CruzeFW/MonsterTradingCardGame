package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.TradingRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Optional;

public class TradingService {

    private final UserRepositoryDatabase userRepositoryDatabase;
    private final TradingRepositoryDatabase tradingRepositoryDatabase;
    public TradingService(UserRepositoryDatabase userRepositoryDatabase, TradingRepositoryDatabase tradingRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.tradingRepositoryDatabase = tradingRepositoryDatabase;
    }

    // find all trades in the trading table
    public Object[] getCurrentTrades(Request request){
        Object[] arr = new Object[2];
        if(checkToken(request).isEmpty()){
            arr[0] = 1;
            return arr;
        }
        Optional<ArrayList<Trade>> trades = tradingRepositoryDatabase.findAllTrades();
        if(trades.isEmpty()){
            arr[0] = 2;
            return arr;
        }
        ArrayList<Trade> foundTrades = trades.get();

        arr[0] = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(foundTrades);
            String jsonString = objectMapper.writeValueAsString(jsonNode);
            arr[1] = jsonString;
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return arr;
    }

    // create a new Trade if the user is allowed to, the trade doesn't already exist and the user owns the card to trade
    public Integer createNewTrade(Request request){
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){
            return 1;                    // user not logged in/doesn't exist
        }

        User tradingUser = user.get();
        Trade trade;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            trade = objectMapper.readValue(request.getBody(), Trade.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // check if tradingUser owns the card he wants to trade and if it's not in a deck
        boolean isAvailable = tradingRepositoryDatabase.checkUserCardAssociation(tradingUser, trade);
        if(!isAvailable){
            return 2;                        // user not owner of card or card in deck
        }

        // add trade to DB, if it doesn't exist
        //TODO teste das!
        boolean success = tradingRepositoryDatabase.addTrade(trade);
        if(!success){
            return 3;                       // id already exists
        }

        return 0;                           // added new trade to trading table
    }

    // checks if the deal exists, if the card belongs to the user or if the deal is already finished
    public Integer deleteTrade(Request request){
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){
            return 1;                    // user not logged in/doesn't exist
        }
        User foundUser = user.get();
        Trade trade = new Trade();
        trade.setId(request.getRoute().split("/")[2]); // TODO check if the split works correct
        if(!tradingRepositoryDatabase.findTradeWithId(trade)){
            return 3;                   // id not found
        }
        trade = tradingRepositoryDatabase.getTradeWithId(trade);
        if(!tradingRepositoryDatabase.checkUserCardAssociation(foundUser, trade)){
            return 2;                   // card doesn't belong to user (or is in deck)
        }
        if(trade.isCompleted()){
            return 4;                   // trade is already finished
        }
        tradingRepositoryDatabase.deleteTrade(trade);
        return 0;                       // successfully deleted
    }


    //TODO HIER WEITER MACHEN clean up this mess
    public Integer carryOutTrade(Request request){
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){
            return 1;                    // user not logged in/doesn't exist
        }
        User foundUser = user.get();
        Trade trade = new Trade();
        trade.setId(request.getRoute().split("/")[2]); // TODO check if the split works correct
        if(!tradingRepositoryDatabase.findTradeWithId(trade)){
            return 3;                   // id not found
        }

        // check if card in trade belongs to user
        trade = tradingRepositoryDatabase.getTradeWithId(trade);
        if(!tradingRepositoryDatabase.checkUserCardAssociation(foundUser, trade)){
            return 2;                   // card doesn't belong to user (or is in deck)
        }

        // create Card from the UUID in the body
        Card offeredCard;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            offeredCard = objectMapper.readValue(request.getBody(), Card.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        CardRepositoryDatabase cardRepositoryDatabase = new CardRepositoryDatabase();
        Optional<Card> foundCard = cardRepositoryDatabase.find(offeredCard);
        if(foundCard.isEmpty()){
            return 2;                   // card is not in db (shouldn't happen?)
        }
        offeredCard = foundCard.get();

        // create Card that is inside the trade
        Card cardInsideTrade = new Card(trade.getCardToTrade());
        Optional<Card> foundCard2 = cardRepositoryDatabase.find();       // card from trade
        if(foundCard2.isEmpty()){
            return 2;                   // card is not in db (shouldn't happen?)
        }
        Card cardFromTrade = foundCard2.get();
        if(foundUser.getId().equals(cardFromTrade.getOwner())){
            return 2;                   // card is property of trader
        }



        compareValues(offeredCard, cardFromTrade);



        return 0;
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? sextet in CardService + StatsService + DeckService + BattleService + ScoreboardService
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
