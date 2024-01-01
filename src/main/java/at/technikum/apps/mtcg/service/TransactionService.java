package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.TransactionRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class TransactionService {
    public TransactionRepositoryDatabase transactionRepositoryDatabase = new TransactionRepositoryDatabase();

    public TransactionService(TransactionRepositoryDatabase transactionRepositoryDatabase){
        this.transactionRepositoryDatabase = transactionRepositoryDatabase;
    }

    public Object[] acquire(Request request){
        Object[] arr = new Object[2];
        Optional<User> user = validateUser(request);
        if(user.isEmpty()){
            arr[0] = 1;                 // user not found
            return arr;
        }

        User foundUser = user.get();
        if(foundUser.getCoins() >= 5){
            Optional<Package> pack = findAvailablePackage();
            if(pack.isEmpty()){
                arr[0] = 1;             // no package available/
                return arr;
            }
            Package foundPack = pack.get();
            boolean success = transactionRepositoryDatabase.assignPackageToUser(foundUser, foundPack);
            if(success){
                transactionRepositoryDatabase.packageIsBought(foundPack);
                transactionRepositoryDatabase.removeCoins(foundUser);
                transactionRepositoryDatabase.saveTransaction(foundUser, foundPack.getPackageId());
                arr[0] = 0;
                Card[] cards = getAllCardsFromOnePackage(foundUser, foundPack.getPackageId());
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.valueToTree(cards);
                    String jsonString = objectMapper.writeValueAsString(jsonNode);
                    arr[1]= jsonString;
                }catch(JsonProcessingException e){
                    throw new RuntimeException(e);
                }
                return arr;
            }

        }

        arr[0] = 2;                     // not enough coins
        return arr;
    }

    private Optional<User> validateUser(Request request){
        UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();
        Optional<User> foundUser;
        User user = new User();
        user.setAuthorization(request.getAuthorization());

        foundUser = userRepositoryDatabase.findWithToken(user);

        return foundUser;
    }

    private Optional <Package> findAvailablePackage(){
        return transactionRepositoryDatabase.findAvailablePackage();
    }

    private Card[] getAllCardsFromOnePackage(User foundUser, String packageId){
        return transactionRepositoryDatabase.getAllCardsFromOnePackage(foundUser, packageId);
    }
}
