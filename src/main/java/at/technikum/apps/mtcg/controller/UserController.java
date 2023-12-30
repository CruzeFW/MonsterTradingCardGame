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

public class UserController implements Controller {

    private final UserService userService;
    public UserController(){
        this.userService = new UserService(new UserRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.matches("/users/\\w+") || route.equals("/users");
    }

    //check for the route and method and return the corresponding response
    @Override
    public Response handle(Request request) {

        if(request.getRoute().equals("/users")) {
            if(request.getMethod().equals("POST")) {
                return create(request);
            }
        }

        if(request.getRoute().matches("/users/\\w+")){
            //TODO change the response to the correct response
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

    // create a user in the database and checks if it doesn't already exists
    public Response create(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        User user;
        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<User> user1 = userService.find(user);
        if(user1.isPresent()){
            Response response = new Response();
            response.setStatus(HttpStatus.CONFLICT);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("User with same username already registered");
            return response;
        }else{

            userService.save(user);

            Response response = new Response();
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("User successfully created");
            return response;
        }
    }


    public Response searchForUser(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setUsername(request.getRoute().split("/")[2]);
        user.setAuthorization(request.getAuthorization());

        Optional<User> returnedUser;
        returnedUser = userService.find(user);

        if(returnedUser.isPresent()){
            User foundUser = returnedUser.get();
            boolean authorized = userService.checkIfAuthorized(user, foundUser);
            if(authorized){
                String userJson;
                try {
                    userJson = objectMapper.writeValueAsString(foundUser);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody(userJson);
                //TODO ask Prof wegen der "Description" in der API

                return response;
            } else {
                Response response = new Response();
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setContentType(HttpContentType.TEXT_PLAIN);
                response.setBody("Unauthorized request");

                return response;
            }

        }else{
            Response response = new Response();
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User not found.");

            return response;
        }
    }


    //TODO implement the correct function, needs token/sessions
    public Response update(Request request){
        //TODO firstly check if admin oder valid user
        //TODO ACHTUNG WEGEN TOKEN DER DA MITGEGEBEN WIRD



        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Not yet implemented");

        return response;
    }
}
