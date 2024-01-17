package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PackageControllerTest {

    @Test
    void shouldReturnMatchingRoute() {
        // Arrange
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(packageService);
        String route = "/packages";

        boolean foundRoute = false;

        // Act
        foundRoute = packageController.supports(route);

        // Assert
        assert(foundRoute);
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_Created() throws SQLException {
        // Arrange
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(packageService);
        Request request = mock(Request.class);
        Response response = new Response();
        int decider = 0;

        when(request.getMethod()).thenReturn("POST");
        when(packageService.postMethodCalled(request)).thenReturn(decider);

        // Act
        response = packageController.handle(request);

        // Assert
        assertEquals("Package and cards successfully created.", response.getBody());
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_UnauthorizedRequest() throws SQLException {
        // Arrange
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(packageService);
        Request request = mock(Request.class);
        Response response = new Response();
        int decider = 1;

        when(request.getMethod()).thenReturn("POST");
        when(packageService.postMethodCalled(request)).thenReturn(decider);

        // Act
        response = packageController.handle(request);

        // Assert
        assertEquals("Unauthorized request", response.getBody());
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_Forbidden() throws SQLException {
        // Arrange
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(packageService);
        Request request = mock(Request.class);
        Response response = new Response();
        int decider = 2;

        when(request.getMethod()).thenReturn("POST");
        when(packageService.postMethodCalled(request)).thenReturn(decider);

        // Act
        response = packageController.handle(request);

        // Assert
        assertEquals("Provided user is not admin", response.getBody());
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_Conflict() throws SQLException {
        // Arrange
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(packageService);
        Request request = mock(Request.class);
        Response response = new Response();
        int decider = 3;

        when(request.getMethod()).thenReturn("POST");
        when(packageService.postMethodCalled(request)).thenReturn(decider);

        // Act
        response = packageController.handle(request);

        // Assert
        assertEquals("At least one card in the package already exists.", response.getBody());
    }

    @Test
    void shouldNotHandleRequestedMethod() throws SQLException {
        // Arrange
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(packageService);
        Request request = mock(Request.class);
        Response response = new Response();

        when(request.getMethod()).thenReturn("DELETE");

        // Act
        response = packageController.handle(request);

        // Assert
        assertEquals("Method not allowed.", response.getBody());
    }

}