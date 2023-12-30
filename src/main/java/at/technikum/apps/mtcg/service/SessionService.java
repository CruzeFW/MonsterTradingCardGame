package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.dto.TokenRequest;
import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.repository.TokenRepositoryDatabase;


public class SessionService {

    private final TokenRepositoryDatabase tokenRepositoryDatabase;

    public SessionService(TokenRepositoryDatabase tokenRepositoryDatabase) {
        this.tokenRepositoryDatabase = tokenRepositoryDatabase;
    }

    public boolean validateToken(TokenRequest tokenRequest){
        return tokenRepositoryDatabase.find(tokenRequest);
    }

    public void addTokenToUser(Token token, TokenRequest tokenRequest){
        tokenRepositoryDatabase.addToken(token, tokenRequest);
    }


}