package at.technikum.apps.mtcg.util;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Response;

public class ResponseCreator {

    public Response createResponse(HttpStatus httpStatus, HttpContentType contentType, String message){
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(contentType);
        response.setBody(message);

        return response;
    }

}
