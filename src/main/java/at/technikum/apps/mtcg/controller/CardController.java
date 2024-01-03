package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class CardController extends Controller{

    private final CardService cardService;

    public CardController(){
        this.cardService = new CardService(new UserRepositoryDatabase(), new CardRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/cards");
    }

    // show all cards a user has
    @Override
    public Response handle(Request request) {
        Response response = new Response();
        if(request.getMethod().equals("GET")){
            Object[] arr = cardService.showAllAcquiredCards(request);

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
                response.setBody("User has no cards.");
            }
        }else{
            //TODO delete this response, code should never come here
            response.setStatus(HttpStatus.NOT_ACCEPTABLE);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("End of CardController response handle reached");
        }
        return response;
    }
}
