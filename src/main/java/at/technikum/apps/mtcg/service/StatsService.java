package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.StatsRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Optional;

public class StatsService {
    public final StatsRepositoryDatabase statsRepositoryDatabase;
    private final UserRepositoryDatabase userRepositoryDatabase;

    public StatsService(StatsRepositoryDatabase statsRepositoryDatabase, UserRepositoryDatabase userRepositoryDatabase){
        this.statsRepositoryDatabase = statsRepositoryDatabase;
        this.userRepositoryDatabase = userRepositoryDatabase;
    }

    // check user credentials, returns stats to found user
    public Object[] getMethodCalled(Request request) throws SQLException {
        Object[] arr = new Object[3];
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            arr[0] = 1;
            return arr;
        }
        User foundUser = user.get();
        Stats stats = getStats(foundUser);

        arr[0] = 0;
        String statsJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            statsJson = objectMapper.writeValueAsString(stats);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        arr[1] = statsJson;

        return arr;
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? quintet in CardService + DeckService + ScoreboardService + BattleController
    private Optional<User> checkToken(Request request) throws SQLException {
        Optional<User> foundUser = Optional.empty();
        if (request.getAuthorization() == null) {
            return foundUser;           // no token
        } else {
            User findUser = new User();
            findUser.setAuthorization(request.getAuthorization());
            Optional<User> user = userRepositoryDatabase.findWithToken(findUser);

            if(user.isEmpty()){
                return foundUser;       // no user with that token exists
            }
            return user;           // correct authentication
        }
    }

    // call db to fill Stats entity with values
    public Stats getStats(User user) throws SQLException {
        Stats stats = statsRepositoryDatabase.getNameAndElo(user);
        stats.setWins(statsRepositoryDatabase.calculateWins(user));
        stats.setLosses(statsRepositoryDatabase.calculateLosses(user));

        return stats;
    }
}
