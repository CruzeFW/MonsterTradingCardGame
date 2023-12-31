package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.data.Database;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryDatabase {
    private final String FIND_ALL_SQL = "SELECT * FROM users";
    private final String FIND_ONE = "SELECT * FROM users WHERE username = ?";
    private final String FIND_ONE_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final String SAVE_SQL = "INSERT INTO users(id, username, password, elo, coins) VALUES(?, ?, ?, ?, ?)";
    private final String DELETE_TOKEN = "UPDATE users SET token = NULL";
    private final String UPDATE = "UPDATE users SET username = ?, password = ?, bio = ?, image = ? WHERE token = ?";


    private final Database database = new Database();

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_SQL);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            return users;
        }
    }

    // find user by its username
    public Optional<User> find(User user) {
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
                return Optional.of(userToReturn);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }

        return Optional.empty();
    }

    // find user by its token
    public Optional<User> findWithToken(User user) {
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
                userToReturn.setBio(rs.getString("bio"));
                userToReturn.setImage(rs.getString("image"));
                return Optional.of(userToReturn);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return Optional.empty();
    }

    // creates a new user with custom values id, username and password, as well as default elo and default coins
    public User save(User user) {
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
            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
            e.getErrorCode();
        }

        return user;
    }

    //NOT IN USE
    public User delete(User user) {
        return null;
    }

    //deletes token in database for all users
    public void deleteToken(){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_TOKEN)
        ) {
            pstmt.execute();

        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    // update user in DB
    //TODO maybe change return type, see other todo in usage
    public User update(User user){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE)
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getBio());
            pstmt.setString(4, user.getImage());
            pstmt.setString(5, user.getAuthorization());

            pstmt.execute();

        } catch (SQLException e) {
            e.getErrorCode();
        }
        return user;
    }
}