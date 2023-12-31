package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.apps.mtcg.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.Optional;

public class UserController extends Controller {

    private final UserService userService;
    public UserController(){
        this.userService = new UserService(new UserRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.matches("/users/\\w+") || route.equals("/users");
    }

    // check for the route and method, call the correct next function
    @Override
    public Response handle(Request request) {

        if(request.getRoute().equals("/users")) {
            if(request.getMethod().equals("POST")) {
                return create(request);
            }
        }

        if(request.getRoute().matches("/users/\\w+")){
            switch(request.getMethod()){
                case "GET": return searchForUser(request);
                case "PUT": return update(request);
            }
        }

        //TODO delete this response, code should never come here
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("End of UserController response handle reached");
        return response;
    }

    // create a user in the database and checks if it doesn't already exist
    public Response create(Request request) {

        int responseType = userService.postMethodCalled(request);

        Response response = new Response();
        if(responseType == 1){
            response.setStatus(HttpStatus.CONFLICT);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User with same username already registered");
        }else{
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User successfully created");
        }
        return response;
    }

    // searches for user in database
    public Response searchForUser(Request request) {

        Object[] arr = userService.getMethodCalled(request);

        Response response = new Response();
        if(arr[0].equals(0)){
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody((String) arr[1]);
                //TODO ask Prof wegen der "Description" in der API

        } else if (arr[0].equals(1)){
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User not found.");
        }else{
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request.");
        }
        return response;
    }

    // update user in database
    public Response update(Request request){
        int responseType = userService.putMethodCalled(request);

        Response response = new Response();
        if(responseType == 0) {
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("User successfully updated.");

        } else if (responseType == 1) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User not found.");

        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request");
        }
        return response;
    }

}
