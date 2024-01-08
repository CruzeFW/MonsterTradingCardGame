package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.TokenRepositoryDatabase;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class SessionController extends Controller{

    private final SessionService sessionService;

    public SessionController(){
        this.sessionService = new SessionService(new TokenRepositoryDatabase());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/sessions");
    }

    // handle POST requests on /sessions
    @Override
    public Response handle(Request request) throws SQLException {
        if(request.getMethod().equals("POST")) {
            int responseType = sessionService.postMethodCalled(request);
            if(responseType == 0){
                return responseCreator.createResponse(HttpStatus.OK, HttpContentType.TEXT_PLAIN, "User login successful.");
            }else{
                return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Invalid username/password provided.");
            }
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }
}
