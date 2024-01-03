package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.StatsRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.StatsService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

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
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")) {
            Object[] arr = statsService.getMethodCalled(request);

            Response response = new Response();
            if(arr[0].equals(0)){
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody((String) arr[1]);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setContentType(HttpContentType.TEXT_PLAIN);
                response.setBody("Unauthorized request.");
            }
            return response;
        }

        //TODO delete this response, code should never come here
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("End of StatsController response handle reached");
        return response;
    }
}
