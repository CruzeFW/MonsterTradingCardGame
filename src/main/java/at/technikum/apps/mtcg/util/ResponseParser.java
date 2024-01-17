package at.technikum.apps.mtcg.util;

import at.technikum.apps.mtcg.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class ResponseParser {

    public String userDataParser(User user){
        return "UserData:\n Name: " + user.getName() + "\n" +
                " Bio: " + user.getBio() + "\n" +
                " Image: " + user.getImage() + "\n";
    }

    public String cardDataParser(Card card){
        return "Card:\n Id: " + card.getId() + "\n" +
                " Name: " + card.getName() + "\n" +
                " Damage: " + card.getDamage() + "\n";
    }

    public StringBuilder intro(String header, User user){
        StringBuilder responseString = new StringBuilder();
        responseString.append(header).append(user.getUsername()).append(":\n");

        return responseString;
    }

    public String outro(StringBuilder responseString, String footer){
        responseString.append(footer).append("\n");

        return responseString.toString();
    }
    public StringBuilder cardArrayToString(ArrayList<Card> foundCards, StringBuilder responseString){

        for(Card card : foundCards){
            responseString.append(cardDataParser(card)).append("\n");
        }
        return responseString;
    }

    public String alternativeDeckDisplay(ArrayList<Card> foundDeck){
        StringBuilder responseString = new StringBuilder();

        responseString.append("Owner: ").append(foundDeck.getFirst().getOwner()).append("\r\n");
        for(Card card : foundDeck){
            responseString.append("  + CardId: ").append(card.getId()).append("\r\n");
            responseString.append("      - Name: ").append(card.getName()).append("\r\n");
            responseString.append("      - Damage: ").append(card.getDamage()).append("\r\n");
        }

        return responseString.toString();
    }

    public String statsToString(Stats stats){
        return "Stats:\n Name: " + stats.getUser() + "\n" +
                " Elo: " + stats.getElo() + "\n" +
                " Wins: " + stats.getWins() + "\n" +
                " Losses: " + stats.getLosses() + "\n";
    }

    public String statsListToString(ArrayList<Stats> scoreboard){
        StringBuilder responseString = new StringBuilder();
        responseString.append("Scoreboard: ").append("\n");
        for (Stats stat : scoreboard){
            responseString.append(statsToString(stat)).append("\n");
        }
        responseString.append("--- end of scoreboard ---");
        return responseString.toString();
    }

    public String tradesToString(Trade trade){
        return "Trade:\n Id: " + trade.getId() + "\n" +
                " CardToTrade: " + trade.getCardToTrade() + "\n" +
                " Type: " + trade.getType() + "\n" +
                " MinimumDamage: " + trade.getMinDamage() + "\n";
    }

    public String tradeListToString(ArrayList<Trade> trades){
        StringBuilder responseString = new StringBuilder();
        responseString.append("Available trades: ").append("\n");
        for (Trade trade : trades){
            responseString.append(tradesToString(trade)).append("\n");
        }
        responseString.append("--- end of available trades ---");
        return responseString.toString();
    }

    public String transactionsToString(ArrayList<Transaction> transactions, User foundUser){
        StringBuilder responseString = new StringBuilder();
        responseString.append("Transaction history for ").append(foundUser.getUsername()).append(":\n");
        responseString.append(" Packages bought:").append("\n");
        for (Transaction transaction : transactions){
            responseString.append("  - PackageId: ").append(transaction.getPackageid()).append("\n");
        }
        responseString.append("--- end of transaction history ---");
        return responseString.toString();
    }
}
