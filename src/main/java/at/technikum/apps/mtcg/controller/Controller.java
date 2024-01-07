package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.util.ResponseCreator;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public abstract class Controller {

    protected final ResponseCreator responseCreator;
    protected Controller(){
        this.responseCreator = new ResponseCreator();
    }
    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    //TODO add Responses here?
}
