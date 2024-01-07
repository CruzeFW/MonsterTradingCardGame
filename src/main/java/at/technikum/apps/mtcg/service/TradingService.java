package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.TradingRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.CardTypeParser;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Optional;

public class TradingService {

    private final UserRepositoryDatabase userRepositoryDatabase;
    private final TradingRepositoryDatabase tradingRepositoryDatabase;
    private final CardRepositoryDatabase cardRepositoryDatabase;
    public TradingService(UserRepositoryDatabase userRepositoryDatabase, TradingRepositoryDatabase tradingRepositoryDatabase, CardRepositoryDatabase cardRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.tradingRepositoryDatabase = tradingRepositoryDatabase;
        this.cardRepositoryDatabase = cardRepositoryDatabase;
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
        if(!tradingRepositoryDatabase.checkUserCardAssociation(tradingUser, trade)){
            return 2;                        // user not owner of card or card in deck
        }

        // check if trade exists
        Optional<Trade> foundTrade = tradingRepositoryDatabase.getTradeWithId(trade);
        if(foundTrade.isPresent()){
            return 3;                       // trade already exists
        }else{
            // add trade to DB, if it doesn't exist
            tradingRepositoryDatabase.addTrade(trade);
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

        Trade tradeOnlyId = new Trade();
        tradeOnlyId.setId(request.getRoute().split("/")[2]);
        Optional<Trade> trade = tradingRepositoryDatabase.getTradeWithId(tradeOnlyId);
        if(trade.isEmpty()){
            return 3;                   // id/trade not found
        }

        Trade foundTrade = trade.get();
        if(foundTrade.isCompleted()){
            return 4;                   // trade is already finished
        }
        if(!tradingRepositoryDatabase.checkUserCardAssociation(foundUser, foundTrade)){
            return 2;                   // card doesn't belong to user (or is in deck)
        }

        tradingRepositoryDatabase.deleteTrade(foundTrade);
        return 0;                       // successfully deleted
    }

    // fulfills all needed checks and trades card in the end
    public Integer carryOutTrade(Request request){
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){
            return 1;                    // user not logged in/doesn't exist
        }
        User foundUser = user.get();
        Trade tradeWithId = new Trade();
        tradeWithId.setId(request.getRoute().split("/")[2]);
        Optional<Trade> trade = tradingRepositoryDatabase.getTradeWithId(tradeWithId);
        if(trade.isEmpty()){
            return 3;                   // id/trade not found
        }

        Trade foundTrade = trade.get();
        if(tradingRepositoryDatabase.checkUserCardAssociation(foundUser, foundTrade)){
            return 2;                   // card in trade belongs to user who wants to trade (or is in a deck) - can't trade with yourself
        }

        // TODO QUESTION: effective method to export those if's ? Problem: need both cards + the return value at the end for the trade - maybe Object[] ?
        Optional<Card> cardInBody = getCardFromDB(request);
        if(cardInBody.isEmpty()){
            return 2;                   // check if offered card exist
        }
        Card offeredCard = cardInBody.get();
        if(!cardBelongsToUser(foundUser, offeredCard)){
            return 2;                   // offered card doesn't belong to user who wants to trade
        }
        if(offeredCard.getDeckid() == 1){
            return 2;                   // card is in deck
        }

        Optional<Card> foundCardInTrade = getCardFromTradeInDB(foundTrade);
        if(foundCardInTrade.isEmpty()){
            return 2;                   // check if card in trade exist
        }
        Card cardFromTrade = foundCardInTrade.get();

        if(!checkTradingRequirements(offeredCard, foundTrade)){
            return 2;                   // requirements don't allow deal
        }

        tradeCards(offeredCard, cardFromTrade);                 // change owner of cards in DB as well as in the local cards

        foundTrade = setTradeToComplete(foundTrade);            // update trade to be completed

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

    // maps id to card and calls cardRepositoryDatabase and gets the optional card
    private Optional<Card> getCardFromDB(Request request){
        Card card;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            card = objectMapper.readValue(request.getBody(), Card.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return cardRepositoryDatabase.find(card);
    }

    // checks if user id and card owner are equal
    private boolean cardBelongsToUser(User user, Card card){
        return user.getId().equals(card.getOwner());
    }

    // gets optional card from cardRepositoryDatabase
    private Optional<Card> getCardFromTradeInDB(Trade trade){
        Card card = new Card();
        card.setId(trade.getCardToTrade());
        return cardRepositoryDatabase.find(card);
    }

    // compares the required trade values (minDamage and type)
    private boolean checkTradingRequirements(Card offeredCard, Trade trade){
        CardTypeParser cardTypeParser = new CardTypeParser();
        String cardType = cardTypeParser.getTypeFromCard(offeredCard);
        if(offeredCard.getDamage() < trade.getMinDamage()){
            return false;
        }else{
            return cardType.equals(trade.getType());
        }
    }

    // change local variables and call cardRepositoryDatabase to change owners in the DB
    private void tradeCards(Card offeredCard, Card cardFromTrade){
        String temp = offeredCard.getOwner();
        offeredCard.setOwner(cardFromTrade.getOwner());
        cardFromTrade.setOwner(temp);
        cardRepositoryDatabase.updateOwner(offeredCard);
        cardRepositoryDatabase.updateOwner(cardFromTrade);
    }

    // change boolean in trade and calls cardRepositoryDatabase to change it there aswell
    private Trade setTradeToComplete(Trade trade){
        trade.setCompleted(true);
        tradingRepositoryDatabase.setTradeToCompleted(trade);

        return trade;
    }
}
