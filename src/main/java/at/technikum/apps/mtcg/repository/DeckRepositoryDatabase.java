package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class DeckRepositoryDatabase {

    private final Database database = new Database();
    private final String GET_DECK = "SELECT * FROM cards WHERE owner = ? AND deckid IS NOT NULL";
    private final String FIND_ONE_CARD_WITH_USER = "SELECT * FROM cards WHERE owner = ? AND id = ?";
    private final String ADD_CARD_TO_DECK= "UPDATE cards SET deckid = ? WHERE owner = ? AND id = ?";

    // finds deck for user and returns it (or an optional empty list)
    public Optional<ArrayList<Card>> findDeck(User user){
        Optional<ArrayList<Card>> posDeck = Optional.empty();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_DECK)
        ){
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                ArrayList<Card> deck = new ArrayList<>();
                do {
                    Card card = new Card(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getFloat("damage"),
                            rs.getString("owner"),
                            rs.getString("packageid"),
                            rs.getInt("deckid")
                    );
                    deck.add(card);
                }while (rs.next());
                return Optional.of(deck);
            }
        }catch(SQLException e){
            e.getErrorCode();
        }
        return posDeck;
    }

    // checks if the card is linked to the given user
    public Optional<Card> checkCardAssociation(Card checkForAssociation, User foundUser){
        Optional<Card> card = Optional.empty();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE_CARD_WITH_USER)
        ){
            pstmt.setString(1, foundUser.getId());
            pstmt.setString(2, checkForAssociation.getId());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Card foundCard = new Card(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getFloat("damage"),
                        rs.getString("owner"),
                        rs.getString("packageid"),
                        rs.getInt("deckid")
                );
                return Optional.of(foundCard);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return card;
    }

    // adds the card to the user
    public void addToDeck(Card addCard, User foundUser){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ADD_CARD_TO_DECK)
        ){
                pstmt.setInt(1, 1);
                pstmt.setString(2, foundUser.getId());
                pstmt.setString(3, addCard.getId());

                pstmt.execute();
        }catch(SQLException e){
            e.getErrorCode();
        }
    }
}
