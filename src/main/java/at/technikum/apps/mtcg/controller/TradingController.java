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
        if(request.getRoute().equals("/tradings")) {
            if (request.getMethod().equals("GET")) {
                Object[] arr = tradingService.getCurrentTrades(request);
                if(arr[0].equals(0)){
                    // prints trades as the response body
                    return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
                }else if(arr[0].equals(1)){
                    return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
                }else {
                    // TODO should be NO_CONTENT
                    return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "The request was fine, but there are no trading deals available");
                }
            } else if (request.getMethod().equals("POST")) {
                int responseType = tradingService.createNewTrade(request);
                if(responseType == 0){
                    return responseCreator.createResponse(HttpStatus.CREATED, HttpContentType.TEXT_PLAIN, "Trading deal successfully created.");
                }else if(responseType == 1){
                    return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
                }else if(responseType == 2){
                    return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "The deal contains a card that is not owned by the user or locked in the deck.");
                }else {
                    return responseCreator.createResponse(HttpStatus.CONFLICT, HttpContentType.TEXT_PLAIN, "A deal with this deal ID already exists.");
                }
            }
        }else{
            if(request.getMethod().equals("DELETE")){
                int responseType = tradingService.deleteTrade(request);
                if(responseType == 0){
                    return responseCreator.createResponse(HttpStatus.OK, HttpContentType.TEXT_PLAIN, "Trading deal successfully deleted.");
                }else if(responseType == 1){
                    return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
                }else if(responseType == 2){
                    return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "The deal contains a card that is not owned by the user.");
                }else if(responseType == 3){
                    return responseCreator.createResponse(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, "The provided deal ID was not found.");
                }else {
                    return responseCreator.createResponse(HttpStatus.CONFLICT, HttpContentType.TEXT_PLAIN, "A deal with this deal ID has already been finished.");
                }
            }else if (request.getMethod().equals("POST")) {
                int responseType = tradingService.carryOutTrade(request);
                if(responseType == 0){
                    return responseCreator.createResponse(HttpStatus.OK, HttpContentType.TEXT_PLAIN, "Trading deal successfully executed.");
                }else if(responseType == 1){
                    return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
                }else if(responseType == 2){
                    return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "The offered card is not owned by the user, or the requirements are not met (Type, MinimumDamage), or the offered card is locked in the deck.");
                }else {
                    return responseCreator.createResponse(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, "The provided deal ID was not found.");
                }
            }
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }
}
