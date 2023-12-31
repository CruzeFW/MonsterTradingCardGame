package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.PackageRepositoryDatabase;
import at.technikum.apps.mtcg.service.PackageService;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class PackageController extends Controller{

    private final PackageService packageService;
    public PackageController(){
        this.packageService = new PackageService(new PackageRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/packages");
    }

    @Override
    public Response handle(Request request) {
        if(request.getRoute().equals("/packages")){
            if(request.getMethod().equals("POST")){
                return create(request);
            }
        }

        //TODO delete this response, code should never come here
        Response response = new Response();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("End of PackageController response handle reached");
        return response;
    }

    // create new package
    private Response create(Request request){
        int responseType = packageService.postMethodCalled(request);

        Response response = new Response();
        if(responseType == 0){
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Package and cards successfully created.");
        } else if (responseType == 1) {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Unauthorized request");
        } else if (responseType == 2) {
            response.setStatus(HttpStatus.FORBIDDEN);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("Provided user is not admin");
        }else {
            response.setStatus(HttpStatus.CONFLICT);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("At least one card in the package already exists.");       // the other ones got added to the DB
        }

        return response;
    }
}
