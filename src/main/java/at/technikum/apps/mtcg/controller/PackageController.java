package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.PackageRepositoryDatabase;
import at.technikum.apps.mtcg.service.PackageService;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class PackageController extends Controller{

    private final PackageService packageService;

    public PackageController(){
        this.packageService = new PackageService(new PackageRepositoryDatabase(), new CardRepositoryDatabase());
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/packages");
    }

    // handle post request on /packages
    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("POST")){
            return create(request);
        }
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }

    // create new package
    private Response create(Request request){
        int responseType = packageService.postMethodCalled(request);

        if(responseType == 0){
            return responseCreator.createResponse(HttpStatus.CREATED, HttpContentType.TEXT_PLAIN, "Package and cards successfully created.");

        } else if (responseType == 1) {
            return responseCreator.createResponse(HttpStatus.UNAUTHORIZED, HttpContentType.TEXT_PLAIN, "Unauthorized request");
        } else if (responseType == 2) {
            return responseCreator.createResponse(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, "Provided user is not admin");

        }else {
            // the other ones got added to the DB
            return responseCreator.createResponse(HttpStatus.CONFLICT, HttpContentType.TEXT_PLAIN, "At least one card in the package already exists.");
        }

    }
}
