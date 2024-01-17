package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CardControllerTest {

    @Test
    void shouldReturnMatchingRoute() {
        // Arrange
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(cardService);
        String route = "/cards";

        boolean foundRoute = false;

        // Act
        foundRoute = cardController.supports(route);

        // Assert
        assert(foundRoute);
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_UnauthorizedRequest() throws SQLException {
        // Arrange
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(cardService);
        Request request = mock(Request.class);
        Response response = new Response();
        Object[] arr = new Object[2];
        arr[0] = 1;

        when(request.getMethod()).thenReturn("GET");
        when(cardService.showAllAcquiredCards(request)).thenReturn(arr);

        // Act
        response = cardController.handle(request);

        // Assert
        assertEquals("Unauthorized request.", response.getBody());
    }

    @Test
    void shouldHandleRequestedMethod_AndReturn_UserHasNoCards() throws SQLException {
        // Arrange
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(cardService);
        Request request = mock(Request.class);
        Response response = new Response();
        Object[] arr = new Object[2];
        arr[0] = 2;

        when(request.getMethod()).thenReturn("GET");
        when(cardService.showAllAcquiredCards(request)).thenReturn(arr);

        // Act
        response = cardController.handle(request);

        // Assert
        assertEquals("User has no cards.", response.getBody());
    }

    @Test
    void shouldNotHandleRequestedMethod() throws SQLException {
        // Arrange
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(cardService);
        Request request = mock(Request.class);
        Response response = new Response();


        when(request.getMethod()).thenReturn("DELETE");

        // Act
        response = cardController.handle(request);


        // Assert
        assertEquals("Method not allowed.", response.getBody());
    }
}