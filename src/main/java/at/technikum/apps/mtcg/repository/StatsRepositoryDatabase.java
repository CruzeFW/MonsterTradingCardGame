package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRepositoryDatabase {
    private final Database database = new Database();

    private final String GET_NAME_AND_ELO = "SELECT username, elo FROM users WHERE id = ?";
    private final String GET_ALL_WINS = "SELECT count(winner) AS wins FROM battles WHERE winner = ?";
    private final String GET_ALL_LOSSES = "SELECT count(loser) AS losses FROM battles WHERE loser = ?";


    // returns stats entity with username and elo for the provided user
    public Stats getNameAndElo(User user){
        Stats stats = new Stats();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_NAME_AND_ELO)
        ){
                pstmt.setString(1, user.getId());
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    stats.setUser(rs.getString("username"));
                    stats.setElo(rs.getInt("elo"));
                }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return stats;
    }

    // gets the sum of all wins for the provided user
    public Integer calculateWins(User user){
        int wins = 0;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_ALL_WINS)
        ){
                pstmt.setString(1, user.getId());
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    wins = rs.getInt("wins");
                }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return wins;
    }

    // gets the sum of all losses for the provided user
    public Integer calculateLosses(User user){
        int losses = 0;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_ALL_LOSSES)
        ){
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                losses = rs.getInt("losses");
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return losses;
    }

}
