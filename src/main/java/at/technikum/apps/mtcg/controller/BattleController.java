package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class BattleController extends Controller{

    private final BattleService battleService;

    public BattleController(BattleService battleService){
        this.battleService = battleService;
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/battles");
    }

    // handle POST request on /battles
    @Override
    public Response handle(Request request) throws SQLException {
        if(request.getMethod().equals("POST")){
            Object[] arr = battleService.startBattle(request);
            if(arr[0].equals(0)){
                return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
            } else {
                return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
            }
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }
}
