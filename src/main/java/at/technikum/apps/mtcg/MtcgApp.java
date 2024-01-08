package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.ServerApplication;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.apps.mtcg.util.ResponseCreator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MtcgApp implements ServerApplication {

    private List<Controller> controllers = new ArrayList<>();
    private ResponseCreator responseCreator;

    public MtcgApp() {
        controllers.add(new UserController());
        controllers.add(new SessionController());
        controllers.add(new PackageController());
        controllers.add(new TransactionController());
        controllers.add(new CardController());
        controllers.add(new DeckController());
        controllers.add(new StatsController());
        controllers.add(new ScoreboardController());
        controllers.add(new BattleController());
        controllers.add(new TradingController());

        this.responseCreator = new ResponseCreator();

        //TODO decide if this is the correct place for that
        UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();
        try{
            userRepositoryDatabase.deleteToken();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Response handle(Request request) {

        for (Controller controller: controllers) {
            if (!controller.supports(request.getRoute())) {
                continue;
            }
            try{
                return controller.handle(request);
            }catch(SQLException e){
                e.printStackTrace();
                return responseCreator.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpContentType.TEXT_PLAIN, "Internal Server Error.");
            }
        }

        return responseCreator.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpContentType.TEXT_PLAIN, "Route " + request.getRoute() + " not found in app!");
    }
}
