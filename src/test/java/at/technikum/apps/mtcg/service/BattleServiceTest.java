package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BattleServiceTest {

    @Test
    void shouldReturnOptionalUser_WhenTokenIsCorrect() throws SQLException {
        // Arrange
        UserRepositoryDatabase userRepositoryDatabase = mock(UserRepositoryDatabase.class);
        BattleRepositoryDatabase battleRepositoryDatabase = mock(BattleRepositoryDatabase.class);
        BattleService battleService = new BattleService(userRepositoryDatabase, battleRepositoryDatabase);
        Request request = mock(Request.class);

        User user = new User(
                "id",
                "username",
                "password",
                "name",
                "authorization",
                "bio",
                "image",
                100,
                50
        );

        when(request.getAuthorization()).thenReturn(user.getAuthorization());
        when(userRepositoryDatabase.findWithToken(user)).thenReturn(Optional.of(user));

        // Act
        Optional<User> retrievedUser = battleService.checkToken(request);

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertEquals("id", retrievedUser.get().getId());
        assertEquals("username", retrievedUser.get().getUsername());
        assertEquals(100, retrievedUser.get().getElo());

    }

    @Test
    void shouldReturnOptionalUser_WhenTokenIsFalse() throws SQLException {
        // Arrange
        UserRepositoryDatabase userRepositoryDatabase = mock(UserRepositoryDatabase.class);
        BattleRepositoryDatabase battleRepositoryDatabase = mock(BattleRepositoryDatabase.class);
        BattleService battleService = new BattleService(userRepositoryDatabase, battleRepositoryDatabase);
        Request request = mock(Request.class);
        User user = new User();

        when(request.getAuthorization()).thenReturn("authFound");
        when(userRepositoryDatabase.findWithToken(user)).thenReturn(Optional.empty());

        // Act
        Optional<User> retrievedUser = battleService.checkToken(request);

        // Assert
        assertTrue(retrievedUser.isEmpty());

    }


}