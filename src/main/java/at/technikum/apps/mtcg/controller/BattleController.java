package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class BattleController extends Controller{

    private final BattleService battleService;

    public BattleController(){
        this.battleService = new BattleService(new UserRepositoryDatabase(), new BattleRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/battles");
    }

    // handle POST request on /battles
    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("POST")){
            Object[] arr = battleService.startBattle(request);

            Response response = new Response();
            if(arr[0].equals(0)){
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.TEXT_PLAIN);
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
