package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.ServerApplication;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.ArrayList;
import java.util.List;

public class MtcgApp implements ServerApplication {

    private List<Controller> controllers = new ArrayList<>();

    public MtcgApp() {
        controllers.add(new UserController());
        controllers.add(new SessionController());
        controllers.add(new PackageController());
        controllers.add(new TransactionController());
        controllers.add(new CardController());
        controllers.add(new DeckController());

        //TODO decide if this is the correct place for that
        UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();
        userRepositoryDatabase.deleteToken();
    }

    @Override
    public Response handle(Request request) {

        for (Controller controller: controllers) {
            if (!controller.supports(request.getRoute())) {
                continue;
            }

            return controller.handle(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Route " + request.getRoute() + " not found in app!");

        return response;
    }
}
