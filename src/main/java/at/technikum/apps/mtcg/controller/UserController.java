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

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class UserController extends Controller {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @Override
    public boolean supports(String route) {
        return route.matches("/users/\\w+") || route.equals("/users");
    }

    // check for the route and method, call the correct next function
    @Override
    public Response handle(Request request) throws SQLException {

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
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }

    // create a user in the database and checks if it doesn't already exist
    public Response create(Request request) throws SQLException {
        int responseType = userService.postMethodCalled(request);

        if(responseType == 1){
            return responseCreator.createResponse(HttpStatus.CONFLICT, HttpContentType.TEXT_PLAIN, "User with same username already registered");
        }else{
            return responseCreator.createResponse(HttpStatus.CREATED, HttpContentType.TEXT_PLAIN, "User successfully created");
        }
    }

    // searches for user in database
    public Response searchForUser(Request request) throws SQLException {
        Object[] arr = userService.getMethodCalled(request);

        if(arr[0].equals(0)){
            //TODO ask Prof wegen der "Description" in der API
            return responseCreator.createResponse(HttpStatus.OK, HttpContentType.APPLICATION_JSON, (String) arr[1]);
        } else if (arr[0].equals(1)){
            return responseCreator.createResponse(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, "User not found.");
        }else{
            return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
        }
    }

    // update user in database
    public Response update(Request request) throws SQLException {
        int responseType = userService.putMethodCalled(request);

        if(responseType == 0) {
            return responseCreator.createResponse(HttpStatus.OK, HttpContentType.TEXT_PLAIN, "User successfully updated.");
        } else if (responseType == 1) {
            return responseCreator.createResponse(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, "User not found.");
        } else {
            return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request.");
        }
    }

}
