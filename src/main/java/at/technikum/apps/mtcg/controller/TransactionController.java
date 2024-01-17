package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.TransactionRepositoryDatabase;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class TransactionController extends Controller{

    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/transactions/packages") || route.equals("/transactions");
    }

    // handles POST request on /transactions/packages and GET requests on /transactions
    @Override
    public Response handle(Request request) throws SQLException {
        if (request.getRoute().equals("/transactions/packages")) {
            if (request.getMethod().equals("POST")) {
                Object[] arr = transactionService.acquire(request);
                if (arr[0].equals(0)) {
                    // prints cards as the response body
                    return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
                } else if (arr[0].equals(1)) {
                    return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request");
                } else if (arr[0].equals(2)) {
                    return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "Not enough money for buying a card package.");
                } else {
                    return responseCreator.createResponse(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, "No card package available for buying.");
                }
            }
        } else if (request.getRoute().equals("/transactions")) {      // OPTIONAL FEATURE TRANSACTION HISTORY
            if (request.getMethod().equals("GET")) {
                Object[] arr = transactionService.getRequestCalled(request);

                if (arr[0].equals(0)) {
                    // prints history as the response body
                    return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
                } else if (arr[0].equals(1)) {
                    return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request");
                } else {
                    return responseCreator.createResponse(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, "No transaction history available.");
                }
            }
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }


}
