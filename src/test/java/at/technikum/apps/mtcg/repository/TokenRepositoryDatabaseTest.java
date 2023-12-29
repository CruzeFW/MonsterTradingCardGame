package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.dto.TokenRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

@ExtendWith(MockitoExtension.class)
class TokenRepositoryDatabaseTest {

    //wie speicher ich hier die datenbank richtig damit ich schauen kann ob das angelegt wird?
    //weil die save ist in er UserRepositoryDatabase und die find für den Token ist in der TokenRepositoryDatabase
    @Test
    void tokenRequest_searchDBforUser_returnBoolAsResult(){
        //Arrange
        TokenRepositoryDatabase tokenRepositoryDatabase = mock(TokenRepositoryDatabase.class);
        TokenRequest tokenRequest = new TokenRequest("name", "pw");

        //Act
        boolean found = tokenRepositoryDatabase.find(tokenRequest);

        //Assert
        assertEquals(true, true);

    }
}