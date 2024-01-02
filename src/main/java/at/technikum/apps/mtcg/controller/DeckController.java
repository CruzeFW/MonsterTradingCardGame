package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.GregorianCalendar;

public class DeckController extends Controller{

    private final DeckService deckService;

    public DeckController(){
        this.deckService = new DeckService(new DeckRepositoryDatabase());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/deck");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")){
            return showDeck(request);
        }else if(request.getMethod().equals("PUT")){
            return updateDeck(request);
        }
        return null;
    }

    //TODO needs text/plain response, maybe add new input to arr to determine?
    public Response showDeck(Request request){
        Response response = new Response();
        Object[] arr = deckService.getDeck(request);

        if(arr[0].equals(0)){
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody((String) arr[1]); // prints cards as the response body
        }else if(arr[0].equals(1)){
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request.");
        } else {
            response.setStatus(HttpStatus.NO_CONTENT);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Deck has no cards.");
        }

        return response;
    }

    public Response updateDeck(Request request){
        int responseType = deckService.updateDeck(request);

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
