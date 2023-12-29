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

    @Override
    public Response handle(Request request) {

        if(request.getRoute().equals("/users")) {
            if(request.getMethod().equals("POST")) {
                //TODO change the response to the correct response
                //TODO check if the user already exists and if so don't add it!!
                return create(request);
            } else {
                //TODO change the response to the correct response
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setContentType(HttpContentType.TEXT_PLAIN);
                response.setBody("Method not allowed");

                return response;
            }
        }

        //TODO add "/users/"
        if(request.getRoute().matches("/users/\\w+")){
            //TODO change the response to the correct response
            switch(request.getMethod()){
                case "GET": return find(request);
                case "PUT": return update(request);
            }
        }else{
            //TODO change the response to the correct response
            Response response = new Response();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Method not allowed");

            return response;
        }

        //TODO delete this response, code should never come here
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("End of UserController response handle reached");
        return response;
    }

    // create a user in the database
    public Response create(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        user = userService.save(user);

        String userJson = null;
        try {
            userJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(userJson);

        return response;
    }

    //TODO implement the correct function, needs token/sessions
    public Response find(Request request){
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Not yet implemented");

        return response;
    }

    //TODO implement the correct function, needs token/sessions
    public Response update(Request request){
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Not yet implemented");

        return response;
    }
}
