package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.TransactionRepositoryDatabase;
import at.technikum.server.http.Request;


import java.util.Optional;

public class TransactionService {
    public TransactionRepositoryDatabase transactionRepositoryDatabase = new TransactionRepositoryDatabase();

    public TransactionService(TransactionRepositoryDatabase transactionRepositoryDatabase){
        this.transactionRepositoryDatabase = transactionRepositoryDatabase;
    }

    public Object[] acquire(Request request){
        Optional<User> user = validateUser(request);

        Object[] arr = new Object[2];

        //TODO hier weiter machen



        return arr;
    }

    private Optional<User> validateUser(Request request){
        Optional <User> foundUser = Optional.empty();

        //TODO hier auch weiter machen


        return foundUser;
    }
}
