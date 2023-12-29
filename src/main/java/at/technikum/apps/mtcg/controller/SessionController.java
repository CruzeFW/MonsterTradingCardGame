package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.dto.TokenRequest;
import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.repository.TokenRepositoryDatabase;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionController implements Controller{

    private final SessionService sessionService;
    private final Token token;

    public SessionController(){
        this.sessionService = new SessionService(new TokenRepositoryDatabase());
        this.token = new Token();
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/sessions");
    }

    @Override
    public Response handle(Request request) {
        if(request.getRoute().equals("/sessions") && request.getMethod().equals("POST")){
            ObjectMapper objectMapper = new ObjectMapper();
            TokenRequest tokenRequest = null;
            try {
                tokenRequest = objectMapper.readValue(request.getBody(), TokenRequest.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            boolean found = sessionService.validateToken(tokenRequest);
            token.setExists(found);
        }
        if(token.getExists()){
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User login successful");

            return response;
        }else{
            Response response = new Response();
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Invalid username/password provided");

            return response;
        }
    }
}
