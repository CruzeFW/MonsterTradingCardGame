package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class BattleRepositoryDatabase {
    Database database = new Database();

    private final String FIND_OPEN_BATTLE = "SELECT * FROM battles WHERE user1 != ? AND user2 IS NULL LIMIT 1";
    private final String START_BATTLE = "INSERT INTO battles (user1) VALUES (?)";
    private final String JOIN_BATTLE = "UPDATE battles SET user2 = ? WHERE id = ?";

    // search for a battle in the DB and returns an Optional
    public Optional<Battle> findOpenBattle(User user){
        Optional<Battle> battle = Optional.empty();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_OPEN_BATTLE)
        ){
                pstmt.setString(1, user.getId());
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()){
                    Battle foundBattle = new Battle();
                    foundBattle.setId(rs.getInt("id"));
                    foundBattle.setUser1(rs.getString("user1"));
                    foundBattle.setUser2(rs.getString("user2"));
                    return Optional.of(foundBattle);
                }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return battle;
    }

    // insert a new battle into the DB with the given user
    public void startNewBattle(User user){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(START_BATTLE)
        ){
            pstmt.setString(1, user.getId());

            pstmt.execute();
        }catch (SQLException e){
            e.getErrorCode();
        }
    }

    // join a already found battle with the new user
    public void joinOpenBattle(User user, Battle foundBattle){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(JOIN_BATTLE)
        ){
            pstmt.setString(1, user.getId());
            pstmt.setInt(2, foundBattle.getId());

            pstmt.execute();
        }catch (SQLException e){
            e.getErrorCode();
        }
    }
}
