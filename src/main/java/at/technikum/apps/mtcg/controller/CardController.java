package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class CardController extends Controller{

    private final CardService cardService;

    public CardController(CardService cardService){
        this.cardService = cardService;
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/cards");
    }

    // show all cards a user has
    @Override
    public Response handle(Request request) throws SQLException {
        if(request.getMethod().equals("GET")){
            Object[] arr = cardService.showAllAcquiredCards(request);

            if(arr[0].equals(0)){
                return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON,(String) arr[1]);
            }else if(arr[0].equals(1)){
                return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
            } else {
                return responseCreator.createResponse(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, "User has no cards.");
            }
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }
}
