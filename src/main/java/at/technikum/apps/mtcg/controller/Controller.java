package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.util.ResponseCreator;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public abstract class Controller {

    protected final ResponseCreator responseCreator;
    protected Controller(){
        this.responseCreator = new ResponseCreator();
    }
    public abstract boolean supports(String route);

    public abstract Response handle(Request request) throws SQLException;

    //TODO add Responses here?
}
