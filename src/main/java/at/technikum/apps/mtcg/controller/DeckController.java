package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.GregorianCalendar;

public class DeckController extends Controller{

    private final DeckService deckService;

    public DeckController(){
        this.deckService = new DeckService(new DeckRepositoryDatabase(), new UserRepositoryDatabase());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/deck") || route.matches("/deck\\?format=plain");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")){
            return showDeck(request);
        }else if(request.getMethod().equals("PUT")) {
            return updateDeck(request);
        }

        //TODO delete this response, code should never come here
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("End of DeckController response handle reached");
        return response;
    }

    // creates response to GET call on /deck or /deck?format=plain
    public Response showDeck(Request request){
        Object[] arr = deckService.getDeck(request);

        Response response = new Response();
        if(arr[0].equals(0)){
            if(request.getRoute().equals("/deck")) {
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody((String) arr[1]); // prints deck as the response body
            }else{
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.TEXT_PLAIN);
                response.setBody(deckService.parseBody(arr)); // prints deck in plain format as the response body
            }
        }else if(arr[0].equals(1)){
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request.");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST);             //TODO QUESTION: if I use NO_CONTENT then the printed response is empty...
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("The request was fine, but the deck doesn't have any cards.");
        }
        return response;
    }

    // creates response for PUT on /deck
    public Response updateDeck(Request request){
        int responseType = deckService.createDeck(request);

        Response response = new Response();
        if(responseType == 0){
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("The deck has been successfully configured.");
        } else if (responseType == 1) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("The provided deck did not include the required amount of cards.");
        } else if (responseType == 2) {
            response.setStatus(HttpStatus.FORBIDDEN);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("At least one of the provided cards does not belong to the user or is not available.");
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request.");
        }
        return response;
    }

}
