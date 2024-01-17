package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class DeckController extends Controller{

    private final DeckService deckService;

    public DeckController(DeckService deckService){
        this.deckService = deckService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/deck") || route.matches("/deck\\?format=plain");
    }

    @Override
    public Response handle(Request request) throws SQLException {
        if(request.getMethod().equals("GET")){
            return showDeck(request);
        }else if(request.getMethod().equals("PUT")) {
            return updateDeck(request);
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }

    // creates response to GET call on /deck or /deck?format=plain
    public Response showDeck(Request request) throws SQLException {
        Object[] arr = deckService.getDeck(request);

        if(arr[0].equals(0)){
            if(request.getRoute().equals("/deck")) {
                return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
            }else{
                // prints deck in plain format as the response body
                return responseCreator.createResponse(HttpStatus.OK, HttpContentType.TEXT_PLAIN, (String) arr[1]);
            }
        }else if(arr[0].equals(1)){
            return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
        } else {
            // HttpStatus should be NO_CONTENT, but I wanted to return the body
            return responseCreator.createResponse(HttpStatus.BAD_REQUEST, HttpContentType.TEXT_PLAIN, "The request was fine, but the deck doesn't have any cards.");
        }
    }

    // creates response for PUT on /deck
    public Response updateDeck(Request request) throws SQLException {
        int responseType = deckService.createDeck(request);

        if(responseType == 0){
            return responseCreator.createResponse(HttpStatus.OK, HttpContentType.TEXT_PLAIN, "The deck has been successfully configured.");
        } else if (responseType == 1) {
            // HttpStatus should be NO_CONTENT, but I wanted to return the body
            return responseCreator.createResponse(HttpStatus.BAD_REQUEST, HttpContentType.TEXT_PLAIN, "The provided deck did not include the required amount of cards.");
        } else if (responseType == 2) {
            return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "At least one of the provided cards does not belong to the user or is not available.");
        } else {
            return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
        }
    }

}
