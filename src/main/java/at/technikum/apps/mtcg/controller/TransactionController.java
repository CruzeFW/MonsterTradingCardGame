package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.TransactionRepositoryDatabase;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TransactionController extends Controller{

    private final TransactionService transactionService;
    public TransactionController(){
        this.transactionService = new TransactionService(new TransactionRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/transactions/packages");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("POST")){
            return acquire(request);
        }

        //TODO delete this response, code should never come here
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("End of UserController response handle reached");
        return response;
    }

    public Response acquire(Request request){
        Object[] arr =  transactionService.acquire(request);

        Response response = new Response();
        if(arr[0].equals(0)){
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody((String) arr[1]); // ADD PACKAGE AS RESPONSE
        }else if(arr[0].equals(1)){
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request");
        } else if (arr[0].equals(2)) {
            response.setStatus(HttpStatus.FORBIDDEN);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Not enough money for buying a card package.");
        }else {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("No card package available for buying.");
        }
        return response;

    }


}
