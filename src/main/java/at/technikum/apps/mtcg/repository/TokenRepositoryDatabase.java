package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.dto.TokenRequest;
import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenRepositoryDatabase {

    private final String FIND_ONE = "SELECT * FROM users WHERE username = ? AND password = ?";

    private final String ADD_TOKEN = "UPDATE users SET token = ? WHERE username = ? AND password = ?";

    private final Database database = new Database();


    //find one user given by its username
    public boolean find(TokenRequest tokenRequest) throws SQLException {
        boolean found = false;
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ONE);
        ){
            pstmt.setString(1, tokenRequest.getUsername());
            pstmt.setString(2, tokenRequest.getPassword());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                found = true;
            }
            rs.close();

        } catch (SQLException e){
            throw new SQLException();
        }
        return found;
    }

    public void addToken(Token token, TokenRequest tokenRequest) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ADD_TOKEN);
        ){
            pstmt.setString(1, token.getToken());
            pstmt.setString(2, tokenRequest.getUsername());
            pstmt.setString(3, tokenRequest.getPassword());

            pstmt.execute();
        } catch(SQLException e){
            throw new SQLException();
        }
    }
}