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

public class CardRepositoryDatabase {

    private final Database database = new Database();

    private final String FIND_ONE = "SELECT * FROM cards WHERE id = ?";
    private final String SAVE_CARD = "INSERT INTO cards(id, name, damage, packageid) VALUES(?, ?, ?, ?)";
    private final String FIND_ALL_CARDS = "SELECT * FROM cards WHERE owner = ?";
    private final String UPDATE_OWNER = "UPDATE cards SET owner = ? WHERE id = ?";


    // find card by its id, returns found one or a optional.empty()
    public Optional<Card> find(Card card) throws SQLException {
        Optional<Card> optionalCardToReturn = Optional.empty();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE)
        ){
            pstmt.setString(1, card.getId());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Card cardToReturn = new Card();
                    cardToReturn.setId(rs.getString("id"));
                    cardToReturn.setName(rs.getString("name"));
                    cardToReturn.setDamage(rs.getFloat("damage"));
                    cardToReturn.setOwner(rs.getString("owner"));
                    cardToReturn.setPackageid(rs.getString("packageid"));
                    cardToReturn.setDeckid(rs.getInt("deckid"));
                return Optional.of(cardToReturn);
            }
        }catch (SQLException e){
            throw new SQLException();
        }
        return optionalCardToReturn;
    }

    // creates a new card in the DB with card id, name, damage, and packageid
    public void save(Card card, String packageId) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_CARD);
        ) {
            pstmt.setString(1, card.getId());
            pstmt.setString(2, card.getName());
            pstmt.setFloat(3, card.getDamage());
            pstmt.setString(4, packageId);

            pstmt.execute();
        }catch (SQLException e){
            throw new SQLException();
        }
    }

    // search for cards in DB, returns Optional<ArrayList<Card>>
    public Optional<ArrayList<Card>> findAllCards(User user) throws SQLException {
        Optional<ArrayList<Card>> cardList = Optional.empty();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_CARDS)
        ) {
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                ArrayList<Card> foundCards = new ArrayList<>();
                do {
                    Card card = new Card(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getFloat("damage"),
                            rs.getString("owner"),
                            rs.getString("packageid"),
                            rs.getInt("deckid")
                    );
                    foundCards.add(card);
                }while (rs.next());
                return Optional.of(foundCards);
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return cardList;
    }

    // update owner after trade is completed
    public void updateOwner(Card card) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE_OWNER);
        ) {
            pstmt.setString(1, card.getOwner());
            pstmt.setString(2, card.getId());

            pstmt.execute();
        }catch (SQLException e){
            throw new SQLException();
        }
    }
}
