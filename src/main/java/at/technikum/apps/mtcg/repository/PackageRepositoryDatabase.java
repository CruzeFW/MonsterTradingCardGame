package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PackageRepositoryDatabase {

    private final Database database = new Database();

    private final String FIND_ONE = "SELECT * FROM cards WHERE id = ?";

    private final String SAVE = "INSERT INTO cards(id, name, damage, packageid) VALUES(?, ?, ?, ?)";

    // find card by its id
    public Optional<Card> find(Card card){
        Card cardToReturn = new Card();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE)
        ){
            pstmt.setString(1, card.getId());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                cardToReturn.setId(rs.getString("id"));
                cardToReturn.setName(rs.getString("name"));
                cardToReturn.setDamage(rs.getFloat("damage"));
                cardToReturn.setOwner(rs.getString("owner"));
                cardToReturn.setPackageid(rs.getString("packageid"));
                return Optional.of(cardToReturn);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return Optional.of(cardToReturn);
    }

    // creates a new card in the DB with card id, name, damage, and packageid
    public void save(Card card, String packageId){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE);
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
//TODO CHECK OB DAS GEHT AMK



}
