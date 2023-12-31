package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PackageRepositoryDatabase {

    private final Database database = new Database();

    private final String SAVE_PACKAGE = "INSERT INTO packages(id, bought) VALUES(?, ?)";

    private final String DELETE_PACKAGE = "DELETE FROM packages WHERE id = ?";

    // save package with its id
    public void save(String packageId){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_PACKAGE);
        ) {
            pstmt.setString(1, packageId);
            pstmt.setBoolean(2, false);

            pstmt.execute();
        }catch (SQLException e){
            e.getErrorCode();
        }
    }

    // delete package with id
    public void delete(String packageId){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_PACKAGE);
        ) {
            pstmt.setString(1, packageId);

            pstmt.execute();
        }catch (SQLException e){
            e.getErrorCode();
        }
    }
}
