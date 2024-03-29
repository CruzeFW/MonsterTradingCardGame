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
    private final String FIND_BATTLE_WITH_ID = "SELECT * FROM battles WHERE id = ?";
    private final String UPDATE_AFTER_BATTLE = "UPDATE battles SET winner = ?, loser = ?, log = ? WHERE id = ?";

    // search for a battle in the DB and returns an Optional
    public Optional<Battle> findOpenBattle(User user) throws SQLException {
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
            throw new SQLException();
        }
        return battle;
    }

    // insert a new battle into the DB with the given user
    public void startNewBattle(User user) throws SQLException {
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(START_BATTLE)
        ){
            pstmt.setString(1, user.getId());

            pstmt.execute();
        }catch (SQLException e){
            throw new SQLException();
        }
    }

    // join an already found battle with the new user
    public void joinOpenBattle(User user, Battle foundBattle) throws SQLException {
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(JOIN_BATTLE)
        ){
            pstmt.setString(1, user.getId());
            pstmt.setInt(2, foundBattle.getId());

            pstmt.execute();
        }catch (SQLException e){
            throw new SQLException();
        }
    }

    // get all values from a started battle
    public Battle findBattleWithId(Integer id) throws SQLException {
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_BATTLE_WITH_ID)
        ){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                 Battle battle = new Battle(
                         rs.getInt("id"),
                         rs.getString("user1"),
                         rs.getString("user2"),
                         rs.getString("winner"),
                         rs.getString("loser"),
                         rs.getString("log")
                 );
                 return battle;
            }
        }catch (SQLException e){
            throw new SQLException();
        }
        return null;
    }

    // update winner, loser, log after battle
    public void updateAfterBattle(Battle battle, String winner, String loser, String log) throws SQLException {
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE_AFTER_BATTLE)
        ){
            pstmt.setString(1, winner);
            pstmt.setString(2, loser);
            pstmt.setString(3, log);
            pstmt.setInt(4, battle.getId());

            pstmt.execute();
        }catch (SQLException e){
            throw new SQLException();
        }
    }

}
