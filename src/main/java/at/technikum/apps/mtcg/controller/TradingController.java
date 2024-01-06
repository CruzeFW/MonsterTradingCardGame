package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.TradingRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TradingController extends Controller {

    private final TradingService tradingService;

    public TradingController(){
        this.tradingService = new TradingService(new UserRepositoryDatabase(), new TradingRepositoryDatabase(), new CardRepositoryDatabase());
    }

    // supports /tradings or /tradings/<UUID>
    @Override
    public boolean supports(String route) {
        return route.equals("/tradings") || route.matches("^/tradings/[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$");
    }

    // handle requests on /tradings or /tradings/<UUID>
    @Override
    public Response handle(Request request) {
        Response response = new Response();
        if(request.getRoute().equals("/tradings")) {
            if (request.getMethod().equals("GET")) {
                Object[] arr = tradingService.getCurrentTrades(request);
                if(arr[0].equals(0)){
                    response.setStatus(HttpStatus.OK);
                    response.setContentType(HttpContentType.APPLICATION_JSON);
                    response.setBody((String) arr[1]); // prints trades as the response body
                }else if(arr[0].equals(1)){
                    response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Unauthorized request.");
                }else {
                    response.setStatus(HttpStatus.FORBIDDEN);                   // TODO should be NO_CONTENT
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("The request was fine, but there are no trading deals available");
                }
            } else if (request.getMethod().equals("POST")) {
                int responseType = tradingService.createNewTrade(request);
                if(responseType == 0){
                    response.setStatus(HttpStatus.CREATED);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Trading deal successfully created.");
                }else if(responseType == 1){
                    response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Unauthorized request.");
                }else if(responseType == 2){
                    response.setStatus(HttpStatus.FORBIDDEN);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("The deal contains a card that is not owned by the user or locked in the deck.");
                }else {
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("A deal with this deal ID already exists.");
                }
            }
        }else{
            if(request.getMethod().equals("DELETE")){
                int responseType = tradingService.deleteTrade(request);
                if(responseType == 0){
                    response.setStatus(HttpStatus.OK);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Trading deal successfully deleted.");
                }else if(responseType == 1){
                    response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Unauthorized request.");
                }else if(responseType == 2){
                    response.setStatus(HttpStatus.FORBIDDEN);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("The deal contains a card that is not owned by the user.");
                }else if(responseType == 3){
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("The provided deal ID was not found.");
                }else {
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("A deal with this deal ID has already been finished.");
                }
            }else if (request.getMethod().equals("POST")) {
                int responseType = tradingService.carryOutTrade(request);
                if(responseType == 0){
                    response.setStatus(HttpStatus.OK);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Trading deal successfully executed.");
                }else if(responseType == 1){
                    response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("Unauthorized request.");
                }else if(responseType == 2){
                    response.setStatus(HttpStatus.FORBIDDEN);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("The offered card is not owned by the user, or the requirements are not met (Type, MinimumDamage), or the offered card is locked in the deck.");
                }else {
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setContentType(HttpContentType.TEXT_PLAIN);
                    response.setBody("The provided deal ID was not found.");
                }
            }
        }
        return response;
    }
}
