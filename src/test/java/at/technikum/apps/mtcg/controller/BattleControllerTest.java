package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.apps.mtcg.util.ResponseCreator;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleControllerTest {

    @Test
    void shouldReturnMatchingRoute() {
        // Arrange
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(battleService);
        String route = "/battles";

        boolean foundRoute = false;

        // Act
        foundRoute = battleController.supports(route);

        // Assert
        assert(foundRoute);
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_UnauthorizedRequest() throws SQLException {
        // Arrange
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(battleService);
        Request request = mock(Request.class);
        Response response = new Response();
        Object[] arr = new Object[2];
        arr[0] = 1;

        when(request.getMethod()).thenReturn("POST");
        when(battleService.startBattle(request)).thenReturn(arr);

        // Act
        response = battleController.handle(request);

        // Assert
        assertEquals("Unauthorized request.", response.getBody());
    }

    @Test
    void shouldNotHandleRequestedMethod() throws SQLException {
        // Arrange
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(battleService);
        Request request = mock(Request.class);
        Response response = new Response();

        when(request.getMethod()).thenReturn("DELETE");

        // Act
        response = battleController.handle(request);

        // Assert
        assertEquals("Method not allowed.", response.getBody());
    }
}