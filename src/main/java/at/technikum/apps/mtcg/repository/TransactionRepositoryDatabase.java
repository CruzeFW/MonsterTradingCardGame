package at.technikum.apps.mtcg.repository;
import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionRepositoryDatabase {

    private final Database database = new Database();
    private final String FIND_ALL_AVAILABLE = "SELECT * FROM packages WHERE bought = false LIMIT 1";
    private final String ASSIGN_CARDS_TO_USER = "UPDATE cards SET owner = ? WHERE packageid = ?";
    private final String SET_BOUGHT_TO_TRUE = "UPDATE packages SET bought = true WHERE id = ?";
    private final String SET_COINS = "UPDATE users SET coins = ? WHERE id = ?";
    private final String FIND_ALL_CARDS_IN_ONE_PACKAGE = "SELECT * FROM cards WHERE packageid = ? AND owner = ?";
    private final String SET_TRANSACTION = "INSERT INTO transactions(buyer, packageid) VALUES (?,?)";

    // finds first Package where bought = false
    //TODO QUESTION: how do i return a real optional, because here every time i return foundPack is the bool = false and so it is not optional
    public Optional<Package> findAvailablePackage(){
        Package foundPack = new Package();
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_AVAILABLE);
                ResultSet rs = pstmt.executeQuery()
            ){
                while(rs.next()){
                    foundPack.setPackageId(rs.getString("id"));
                    foundPack.setBought(rs.getBoolean("bought"));
                }
            }catch(SQLException e){
                e.getErrorCode();
            }
        return Optional.of(foundPack);
    }

    // set user id and package id in cards DB
    public boolean assignCardsToUser(User foundUser, Package foundPack){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ASSIGN_CARDS_TO_USER)
        ){
            pstmt.setString(1, foundUser.getId());
            pstmt.setString(2, foundPack.getPackageId());
            pstmt.execute();

        }catch(SQLException e){
            e.getErrorCode();
            return false;
        }
        return true;
    }

    // set boolean to true
    public void packageIsBought(Package foundPack){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SET_BOUGHT_TO_TRUE)
        ){
            pstmt.setString(1, foundPack.getPackageId());
            pstmt.execute();

        }catch(SQLException e){
            e.getErrorCode();
        }
    }

    // deduct coins of user for transaction
    public void removeCoins(User foundUser){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SET_COINS)
        ){
            pstmt.setInt(1, foundUser.getCoins()-5);
            pstmt.setString(2, foundUser.getId());
            pstmt.execute();

        }catch(SQLException e){
            e.getErrorCode();
        }
    }

    // find all cards in one package, returns an Card[]
    public Card[] getAllCardsFromOnePackage(User foundUser, String packageId) {
        Card[] cards = new Card[5];

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_CARDS_IN_ONE_PACKAGE);

        ) {
            pstmt.setString(1, packageId);
            pstmt.setString(2, foundUser.getId());

            ResultSet rs = pstmt.executeQuery();

            int counter = 0;
            while (rs.next()) {
                Card card = new Card(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getFloat("damage"),
                        rs.getString("owner"),
                        rs.getString("packageid")
                );
                cards[counter] = card;
                counter++;
            }
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return cards;
    }

    // add transaction into transaction table
    public void saveTransaction(User foundUser, String packageid){
        try(
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SET_TRANSACTION)
        ){
            pstmt.setString(1, foundUser.getId());
            pstmt.setString(2, packageid);
            pstmt.execute();

        }catch(SQLException e){
            e.getErrorCode();
        }
    }

}
