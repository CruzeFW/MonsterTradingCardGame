package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class TradingRepositoryDatabase {

    Database database = new Database();

    private final String FIND_ALL_TRADES = "SELECT * FROM trading";
    private final String CHECK_ASSOCIATION = "SELECT * FROM cards WHERE id = ? AND owner = ? AND deckid IS NULL";
    private final String ADD_NEW_TRADE = "INSERT INTO trading(id, cardtotrade, type, mindamage, completed) VALUES (?, ?, ?, ?, ?)";
    private final String GET_TRADE = "SELECT * FROM trading WHERE id = ?";
    private final String DELETE_TRADE = "DELETE FROM trading WHERE id = ?";
    private final String SET_TRADE_TO_COMPLETED = "UPDATE trading SET completed = ? WHERE id = ?";

    // find all existing trades
    public Optional<ArrayList<Trade>> findAllTrades(){
        Optional<ArrayList<Trade>> trades = Optional.empty();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_TRADES)
        ){
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                ArrayList<Trade> foundTrades = new ArrayList<>();
                do {
                    Trade trade = new Trade(
                            rs.getString("id"),
                            rs.getString("cardtotrade"),
                            rs.getString("type"),
                            rs.getFloat("mindamage"),
                            rs.getBoolean("completed")

                    );
                    foundTrades.add(trade);
                }while (rs.next());
                return Optional.of(foundTrades);
            }
        }catch(SQLException e){
            e.getErrorCode();
        }
        return trades;
    }

    // checks if user owns the card and if the card is not in a deck
    public boolean checkUserCardAssociation(User tradingUser, Trade trade){
        boolean isAvailable = false;
            try(
                    Connection con = database.getConnection();
                    PreparedStatement pstmt = con.prepareStatement(CHECK_ASSOCIATION)
            ){
                pstmt.setString(1, trade.getCardToTrade());
                pstmt.setString(2, tradingUser.getId());
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()){
                    isAvailable = true;
                }
            }catch(SQLException e){
                e.getErrorCode();
            }
        return isAvailable;
    }

    // add new trade to db, throw error if id already exists
    public void addTrade(Trade trade){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ADD_NEW_TRADE)
        ){
                pstmt.setString(1, trade.getId());
                pstmt.setString(2, trade.getCardToTrade());
                pstmt.setString(3, trade.getType());
                pstmt.setFloat(4, trade.getMinDamage());
                pstmt.setBoolean(5, false);

                pstmt.execute();

        }catch(SQLException e){
            e.getErrorCode();
        }
    }

    // get trade from the DB when the id is given
    public Optional<Trade> getTradeWithId(Trade trade){
        Optional<Trade> tradeNotFound = Optional.empty();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_TRADE)
        ){
            pstmt.setString(1, trade.getId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Trade foundTrade = new Trade(
                        rs.getString("id"),
                        rs.getString("cardtotrade"),
                        rs.getString("type"),
                        rs.getFloat("mindamage"),
                        rs.getBoolean("completed")
                        );
                return Optional.of(foundTrade);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return tradeNotFound;
    }

    // delete a trade with the given id
    public void deleteTrade(Trade trade){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_TRADE)
        ){
            pstmt.setString(1, trade.getId());
            pstmt.execute();

        }catch (SQLException e){
            e.getErrorCode();
        }
    }

    // update boolean in trade to true
    public void setTradeToCompleted(Trade trade){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SET_TRADE_TO_COMPLETED)
        ){
            pstmt.setBoolean(1, trade.isCompleted());
            pstmt.setString(2, trade.getId());
            pstmt.execute();

        }catch (SQLException e){
            e.getErrorCode();
        }
    }

}
