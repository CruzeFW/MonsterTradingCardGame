package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.dto.TokenRequest;
import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.repository.TokenRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;


public class SessionService {

    private final TokenRepositoryDatabase tokenRepositoryDatabase;
    private final Token token;

    public SessionService(TokenRepositoryDatabase tokenRepositoryDatabase) {
        this.tokenRepositoryDatabase = tokenRepositoryDatabase;
        this.token = new Token();
    }

    public Integer postMethodCalled(Request request) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        TokenRequest tokenRequest = null;
        try {
            tokenRequest = objectMapper.readValue(request.getBody(), TokenRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        token.setExists(validateToken(tokenRequest));

        if (token.getExists()) {
            token.setToken(tokenRequest.getUsername() + "-mtcgToken");
            addTokenToUser(token, tokenRequest);
            return 0;
        }
        return 1;
    }

    public boolean validateToken(TokenRequest tokenRequest) throws SQLException {
        return tokenRepositoryDatabase.find(tokenRequest);
    }

    public void addTokenToUser(Token token, TokenRequest tokenRequest) throws SQLException {
        tokenRepositoryDatabase.addToken(token, tokenRequest);
    }



}