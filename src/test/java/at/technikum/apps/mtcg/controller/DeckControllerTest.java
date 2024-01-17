package at.technikum.apps.mtcg.controller;


import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class DeckControllerTest {

    @Test
    void shouldReturnMatchingRoute() {
        // Arrange
        DeckService deckService = mock(DeckService.class);
        DeckController deckController = new DeckController(deckService);
        String route = "/deck";
        String alternativeRoute = "/deck?format=plain";

        boolean foundRoute = false;
        boolean foundAlternativeRoute = false;

        // Act
        foundRoute = deckController.supports(route);
        foundAlternativeRoute = deckController.supports(alternativeRoute);

        // Assert
        assert(foundRoute);
        assert(foundAlternativeRoute);
    }

    @Test
    void shouldHandleRequestedRoutes() throws SQLException {
        // Arrange
        DeckService deckService = mock(DeckService.class);
        DeckController deckController = spy(new DeckController(deckService));
        Response response = mock(Response.class);
        Request getRequest = mock(Request.class);
        Request putRequest = mock(Request.class);

        when(getRequest.getMethod()).thenReturn("GET");
        when(putRequest.getMethod()).thenReturn("PUT");

        doReturn(response).when(deckController).handle(getRequest);
        doReturn(response).when(deckController).handle(putRequest);

        // Act
        deckController.handle(getRequest);
        deckController.handle(putRequest);

        // Assert
        verify(deckController, times(1)).handle(getRequest);
        verify(deckController, times(1)).handle(putRequest);
    }

    @Test
    void shouldNotHandleRequestedRoutes() throws SQLException {
        // Arrange
        DeckService deckService = mock(DeckService.class);
        DeckController deckController = new DeckController(deckService);
        Response response = mock(Response.class);
        Request request = mock(Request.class);

        when(request.getMethod()).thenReturn("DELETE");

        // Act
        response = deckController.handle(request);

        // Assert
        assertEquals("Method not allowed.", response.getBody());
    }

}