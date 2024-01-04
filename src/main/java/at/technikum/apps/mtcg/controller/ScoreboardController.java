package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.ScoreboardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.StatsRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;

import at.technikum.apps.mtcg.service.ScoreboardService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class ScoreboardController extends Controller{

    private final ScoreboardService scoreboardService;
    public ScoreboardController(){
        this.scoreboardService = new ScoreboardService(new StatsRepositoryDatabase(), new UserRepositoryDatabase(), new ScoreboardRepositoryDatabase());
    };

    @Override
    public boolean supports(String route) {
        return route.equals("/scoreboard");
    }

    // handle GET requests on /scoreboard
    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")){
            Object[] arr = scoreboardService.getMethodCalled(request);
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
        response.setBody("End of ScoreboardController response handle reached");
        return response;
    }
}
