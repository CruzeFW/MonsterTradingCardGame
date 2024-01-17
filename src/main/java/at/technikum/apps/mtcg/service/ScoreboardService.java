package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.ScoreboardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.StatsRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.ResponseParser;
import at.technikum.server.http.Request;

import java.sql.SQLException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Optional;

public class ScoreboardService {
    private final StatsRepositoryDatabase statsRepositoryDatabase;
    private final UserRepositoryDatabase userRepositoryDatabase;
    private final ScoreboardRepositoryDatabase scoreboardRepositoryDatabase;

    public ScoreboardService(StatsRepositoryDatabase statsRepositoryDatabase, UserRepositoryDatabase userRepositoryDatabase, ScoreboardRepositoryDatabase scoreboardRepositoryDatabase){
        this.statsRepositoryDatabase = statsRepositoryDatabase;
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.scoreboardRepositoryDatabase = scoreboardRepositoryDatabase;
    }

    // gets all users, then creates a scoreboard of their stats, sorts it and returns it
    public Object[] getMethodCalled(Request request) throws SQLException {
        Object[] arr = new Object[2];
        if(checkToken(request).isEmpty()){
            arr[0] = 1;
            return arr;
        }
        // get users
        ArrayList<User> users = scoreboardRepositoryDatabase.getAllUsers();
        // create scoreboard
        ArrayList<Stats> scoreboard = createScoreboard(users);
        // sort scoreboard after elo value (highest to lowest)
        Collections.sort(scoreboard);

        arr[0] = 0;

        ResponseParser responseParser = new ResponseParser();
        arr[1] = responseParser.statsListToString(scoreboard);

        return arr;
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? quintet in CardService + StatsService + DeckService + BattleService
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

    // creates stats for each given user, adds them to the scoreboard
    private ArrayList<Stats> createScoreboard(ArrayList<User> users) throws SQLException {
        ArrayList<Stats> scoreboard = new ArrayList<>();
        for(User user : users){
            Stats stats = new Stats();
            stats.setUser(user.getUsername());
            stats.setElo(user.getElo());
            stats.setWins(statsRepositoryDatabase.calculateWins(user));
            stats.setLosses(statsRepositoryDatabase.calculateLosses(user));

            scoreboard.add(stats);
        }
        return scoreboard;
    }
}
