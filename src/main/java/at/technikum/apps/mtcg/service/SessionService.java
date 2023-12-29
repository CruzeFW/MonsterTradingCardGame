package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.dto.TokenRequest;
import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.repository.TokenRepositoryDatabase;


public class SessionService {

    private final TokenRepositoryDatabase tokenRepositoryDatabase;

    public SessionService(TokenRepositoryDatabase tokenRepositoryDatabase) {
        this.tokenRepositoryDatabase = tokenRepositoryDatabase;
    }

    public boolean checkPassword(String username, String password){
        return true;
    }

    public boolean findByUsername(String username){
        //TODO Optional<User> implementieren um die möglichen nicht gefunden user zu finden
        //im entity ordner den User anlegen
        return false;
    }

    public boolean validateToken(TokenRequest tokenRequest){
        return tokenRepositoryDatabase.find(tokenRequest);
    }

    public void addTokenToUser(Token token, TokenRequest tokenRequest){
        tokenRepositoryDatabase.addToken(token, tokenRequest);
    }


}