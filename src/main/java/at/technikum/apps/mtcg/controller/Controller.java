package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public abstract class Controller {

    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    //TODO add Responses here?
}
