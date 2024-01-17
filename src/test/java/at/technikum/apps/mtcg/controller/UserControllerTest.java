package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void shouldReturnMatchingRoute() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String route = "/users";
        String specificRoute = "/users/username";

        boolean foundRoute = false;
        boolean foundSpecificRoute = false;

        // Act
        foundRoute = userController.supports(route);
        foundSpecificRoute = userController.supports(specificRoute);

        // Assert
        assert(foundRoute);
        assert(foundSpecificRoute);
    }

    @Test
    void shouldHandleRequestedMethod() throws SQLException {
        // Arrange
        UserService userService = mock(UserService.class);
        UserController userController = spy(new UserController(userService));
        Response response = mock(Response.class);
        Request postRequest = mock(Request.class);
        Request getRequest = mock(Request.class);
        Request putRequest = mock(Request.class);

        when(postRequest.getRoute()).thenReturn("/users");
        when(getRequest.getRoute()).thenReturn("/users/Username");
        when(putRequest.getRoute()).thenReturn("/users/Username");

        when(postRequest.getMethod()).thenReturn("POST");
        when(getRequest.getMethod()).thenReturn("GET");
        when(putRequest.getMethod()).thenReturn("PUT");

        doReturn(response).when(userController).handle(postRequest);
        doReturn(response).when(userController).handle(getRequest);
        doReturn(response).when(userController).handle(putRequest);

        // Act
        userController.handle(postRequest);
        userController.handle(getRequest);
        userController.handle(putRequest);

        // Assert
        verify(userController, times(1)).handle(postRequest);
        verify(userController, times(1)).handle(getRequest);
        verify(userController, times(1)).handle(putRequest);
    }

}