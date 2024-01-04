package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScoreboardRepositoryDatabase {

    Database database = new Database();

    private final String FIND_ALL_USERS_EXCEPT_ADMIN = "SELECT * FROM users WHERE username != ?";

    // gets the id/username/elo from users that are not the admin
    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_USERS_EXCEPT_ADMIN)
        ){
            pstmt.setString(1,"admin");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                User user = new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getInt("elo")
                );
                users.add(user);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
        return users;
    }
}
