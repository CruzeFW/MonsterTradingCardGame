package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.Transaction;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.TransactionRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.ResponseParser;
import at.technikum.server.http.Request;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class TransactionService {
    public TransactionRepositoryDatabase transactionRepositoryDatabase = new TransactionRepositoryDatabase();

    public TransactionService(TransactionRepositoryDatabase transactionRepositoryDatabase){
        this.transactionRepositoryDatabase = transactionRepositoryDatabase;
    }

    // creates array with response regarding the outcome of the transaction
    public Object[] acquire(Request request) throws SQLException {
        Object[] arr = new Object[2];
        // check if user is in logged in/exists
        Optional<User> user = validateUser(request);
        if(user.isEmpty()){
            arr[0] = 1;                 // user not found
            return arr;
        }

        Optional<Package> pack = findAvailablePackage();
        if(pack.isEmpty()){
            arr[0] = 3;             // no package available/
            return arr;
        }

        User foundUser = user.get();
        // check if enough coins are available
        if(foundUser.getCoins() >= 5){
            Package foundPack = pack.get();
            // assign package/cards to user
            boolean success = transactionRepositoryDatabase.assignCardsToUser(foundUser, foundPack);
            if(success){
                transactionRepositoryDatabase.packageIsBought(foundPack);                               // set package bought to true
                transactionRepositoryDatabase.removeCoins(foundUser);                                   // deduct coins from user
                transactionRepositoryDatabase.saveTransaction(foundUser, foundPack.getPackageId());     // create transaction in DB
                // set response and cards in arr to return
                arr[0] = 0;
                ArrayList<Card> cards = getAllCardsFromOnePackage(foundUser, foundPack.getPackageId());
                //check if cards has elements
                if(!cards.isEmpty()){
                    ResponseParser responseParser = new ResponseParser();
                    arr[1] = responseParser.outro(responseParser.cardArrayToString(cards, responseParser.intro("Package of ", foundUser)), "--- end of package ---");
                    return arr;
                }
            }
        }
        arr[0] = 2;                     // not enough coins
        return arr;
    }

    // shows transaction history for requested user
    public Object[] getRequestCalled(Request request) throws SQLException {
        Object[] arr = new Object[2];
        // check if user is in logged in/exists
        Optional<User> user = validateUser(request);
        if(user.isEmpty()){
            arr[0] = 1;                 // user not found
            return arr;
        }
        User foundUser = user.get();

        Optional<ArrayList<Transaction>> transactions = transactionRepositoryDatabase.findTransactionHistory(foundUser);
        if(transactions.isEmpty()){
            arr[0] = 2;             // no package available/
            return arr;
        }
        ArrayList<Transaction> foundTransactions = transactions.get();
        arr[0] = 0;
        ResponseParser responseParser = new ResponseParser();
        arr[1] = responseParser.transactionsToString(foundTransactions, foundUser);

        return arr;
    }

    // search for User in userRepositoryDatabase and return Optional<User>
    private Optional<User> validateUser(Request request) throws SQLException {
        UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();
        Optional<User> foundUser;
        User user = new User();
        user.setAuthorization(request.getAuthorization());

        foundUser = userRepositoryDatabase.findWithToken(user);

        return foundUser;
    }

    // calls transactionRepositoryDatabase and returns Package
    private Optional <Package> findAvailablePackage() throws SQLException {
        return transactionRepositoryDatabase.findAvailablePackage();
    }

    // calls transactionRepositoryDatabase and returns Card[]
    private ArrayList<Card> getAllCardsFromOnePackage(User foundUser, String packageId) throws SQLException {
        return transactionRepositoryDatabase.getAllCardsFromOnePackage(foundUser, packageId);
    }
}
