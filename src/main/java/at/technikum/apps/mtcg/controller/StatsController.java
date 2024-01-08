package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.StatsRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.StatsService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class StatsController extends Controller{

    private final StatsService statsService;
    public StatsController(){
        this.statsService = new StatsService(new StatsRepositoryDatabase(), new UserRepositoryDatabase());
    };
    @Override
    public boolean supports(String route) {
        return route.equals("/stats");
    }

    // handle GET requests on /stats
    @Override
    public Response handle(Request request) throws SQLException {
        if(request.getMethod().equals("GET")) {
            Object[] arr = statsService.getMethodCalled(request);

            if(arr[0].equals(0)){
                return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
            } else {
                return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
            }
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }
}
