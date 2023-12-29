package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.dto.TokenRequest;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.data.Database;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TokenRepositoryDatabase {
    private final String FIND_ALL_SQL = "SELECT * FROM users";

    private final String SAVE_SQL = "INSERT INTO users(id, username, password, elo, coins) VALUES(?, ?, ?, ?, ?)";

    private final String FIND_ONE = "SELECT * FROM users WHERE username = ? AND password = ?";

    private final Database database = new Database();

//    public List<User> findAll() {
//        List<User> users = new ArrayList<>();
//
//        try (
//                Connection con = database.getConnection();
//                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_SQL);
//                ResultSet rs = pstmt.executeQuery()
//        ) {
//            while (rs.next()) {
//                User user = new User(
//                        rs.getString("id"),
//                        rs.getString("username"),
//                        rs.getString("password")
//                );
//                users.add(user);
//            }
//
//            return users;
//        } catch (SQLException e) {
//            return users;
//        }
//    }

    //find one user given by its username
    public boolean find(TokenRequest tokenRequest) {
        boolean found = false;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE);
        ){
            pstmt.setString(1, tokenRequest.getUsername());
            pstmt.setString(2, tokenRequest.getPassword());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
//                System.out.print("in rs was gefunden");
//                System.out.println(rs.getString(1));
                found = true;
            }
            rs.close();

        } catch (SQLException e){
            e.getErrorCode();
        }
        return found;
    }

//    //creates a new user with custom values id, username and password, as well as default elo and default coins
//    public User save(User user) {
//        try (
//                Connection con = database.getConnection();
//                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
//        ) {
//            pstmt.setString(1, user.getId());
//            pstmt.setString(2, user.getUsername());
//            pstmt.setString(3, user.getPassword());
//            pstmt.setInt(4, 500);
//            pstmt.setInt(5, 20);
//
//
//            pstmt.execute();
//        } catch (SQLException e) {
//            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
//            e.getErrorCode();
//        }
//
//        return user;
//    }

//    public User delete(User user) {
//        return null;
//    }
}