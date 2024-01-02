package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CardRepositoryDatabase {

    private final Database database = new Database();

    private final String FIND_ONE = "SELECT * FROM cards WHERE id = ?";
    private final String SAVE_CARD = "INSERT INTO cards(id, name, damage, packageid) VALUES(?, ?, ?, ?)";


    // find card by its id, returns found one or a optional.empty()
    public Optional<Card> find(Card card){
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
            e.getErrorCode();
        }
        return optionalCardToReturn;
    }

    // creates a new card in the DB with card id, name, damage, and packageid
    public void save(Card card, String packageId){
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
            e.getErrorCode();
        }
    }
}
