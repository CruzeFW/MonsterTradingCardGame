package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.data.Database;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryDatabase {

    private final Database database = new Database();

    private final String FIND_ONE = "SELECT * FROM users WHERE username = ?";
    private final String FIND_ONE_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final String SAVE_SQL = "INSERT INTO users(id, username, password, elo, coins) VALUES(?, ?, ?, ?, ?)";
    private final String DELETE_TOKEN = "UPDATE users SET token = NULL";
    private final String UPDATE = "UPDATE users SET name = ?, bio = ?, image = ? WHERE token = ?";
    private final String GET_USERNAME_AND_ELO = "SELECT username, elo FROM users WHERE id = ?";
    private final String UPDATE_ELO = "UPDATE users SET elo = ? WHERE id = ?";

    // find user by its username
    public Optional<User> find(User user) throws SQLException {
        User userToReturn = new User();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE)
        ){
            pstmt.setString(1, user.getUsername());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                userToReturn.setId(rs.getString("id"));
                userToReturn.setUsername(rs.getString("username"));
                userToReturn.setPassword(rs.getString("password"));
                userToReturn.setAuthorization(rs.getString("token"));
                userToReturn.setBio(rs.getString("bio"));
                userToReturn.setImage(rs.getString("image"));
                userToReturn.setElo((rs.getInt("elo")));
                userToReturn.setCoins((rs.getInt("coins")));
                return Optional.of(userToReturn);
            }
        }catch (SQLException e){
            throw new SQLException();
        }
        return Optional.empty();
    }

    // find user by its token
    public Optional<User> findWithToken(User user) throws SQLException {
        User userToReturn = new User();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE_TOKEN)
        ){
            pstmt.setString(1, user.getAuthorization());

            // save returned value into rs
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                userToReturn.setId(rs.getString("id"));
                userToReturn.setUsername(rs.getString("username"));
                userToReturn.setPassword(rs.getString("password"));
                userToReturn.setAuthorization(rs.getString("token"));
                userToReturn.setName((rs.getString("name")));
                userToReturn.setBio(rs.getString("bio"));
                userToReturn.setImage(rs.getString("image"));
                userToReturn.setElo((rs.getInt("elo")));
                userToReturn.setCoins((rs.getInt("coins")));
                return Optional.of(userToReturn);
            }
        }catch (SQLException e){
            throw new SQLException();
        }
        return Optional.empty();
    }

    // creates a new user with custom values id, username and password, as well as default elo and default coins
    public User save(User user) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, 500);
            pstmt.setInt(5, 20);

            pstmt.execute();
        } catch (SQLException e) {
            throw new SQLException();
        }
        return user;
    }

    // deletes token in database for all users
    public void deleteToken() throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_TOKEN)
        ) {
            pstmt.execute();

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    // update user in DB
    public User update(User user) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE)
        ) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getBio());
            pstmt.setString(3, user.getImage());
            pstmt.setString(4, user.getAuthorization());

            pstmt.execute();

        } catch (SQLException e) {
            throw new SQLException();
        }
        return user;
    }

    // returns username and elo of given user
    public User getUserAndEloWithId(User user) throws SQLException{
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_USERNAME_AND_ELO)
        ) {
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                user.setUsername(rs.getString("username"));
                user.setElo(rs.getInt("elo"));
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return user;
    }

    // update elo of given user
    public void updateElo(User user) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE_ELO)
        ) {
            pstmt.setInt(1, user.getElo());
            pstmt.setString(2, user.getId());

            pstmt.execute();

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

}