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

    private final String SAVE_SQL = "INSERT INTO users(id, username, password, elo, coins) VALUES(?, ?, ?, ?, ?)";

    private final String DELETE_TOKEN = "UPDATE users SET token = NULL";

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

    //find one user given by its username
    public Optional<User> find(User user) {
        User userToReturn = new User();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE);
        ){
            pstmt.setString(1, user.getUsername());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                userToReturn.setId(rs.getString("id"));
                userToReturn.setUsername(rs.getString("username"));
                userToReturn.setPassword(rs.getString("password"));
                return Optional.of(userToReturn);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }

        return Optional.empty();
    }

    //creates a new user with custom values id, username and password, as well as default elo and default coins
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

    public User delete(User user) {
        return null;
    }

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
}