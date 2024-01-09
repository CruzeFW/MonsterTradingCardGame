package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.PackageRepositoryDatabase;
import at.technikum.apps.mtcg.service.PackageService;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class PackageController extends Controller{

    private final PackageService packageService;

    public PackageController(PackageService packageService){
        this.packageService = packageService;
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/packages");
    }

    // handle post request on /packages
    @Override
    public Response handle(Request request) throws SQLException {
        if(request.getMethod().equals("POST")){
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
        return responseCreator.createResponse(HttpStatus.METHOD_NOT_ALLOWED, HttpContentType.TEXT_PLAIN, "Method not allowed.");
    }

}
