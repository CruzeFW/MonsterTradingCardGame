package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionControllerTest {

    @Test
    void shouldReturnMatchingRoute() {
        // Arrange
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        String route = "/sessions";

        boolean foundRoute = false;

        // Act
        foundRoute = sessionController.supports(route);

        // Assert
        assert(foundRoute);
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_Ok() throws SQLException {
        // Arrange
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        Request request = mock(Request.class);
        Response response = new Response();
        int decider = 0;

        when(request.getMethod()).thenReturn("POST");
        when(sessionService.postMethodCalled(request)).thenReturn(decider);

        // Act
        response = sessionController.handle(request);

        // Assert
        assertEquals("User login successful.", response.getBody());
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_UnauthorizedRequest() throws SQLException {
        // Arrange
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        Request request = mock(Request.class);
        Response response = new Response();
        int decider = 1;

        when(request.getMethod()).thenReturn("POST");
        when(sessionService.postMethodCalled(request)).thenReturn(decider);

        // Act
        response = sessionController.handle(request);

        // Assert
        assertEquals("Invalid username/password provided.", response.getBody());
    }

    @Test
    void shouldNotHandleRequestedMethod() throws SQLException {
        // Arrange
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        Request request = mock(Request.class);
        Response response = new Response();

        when(request.getMethod()).thenReturn("DELETE");

        // Act
        response = sessionController.handle(request);

        // Assert
        assertEquals("Method not allowed.", response.getBody());
    }
}