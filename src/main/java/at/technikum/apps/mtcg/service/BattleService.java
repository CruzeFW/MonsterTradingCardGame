package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;
import at.technikum.apps.mtcg.game.Arena;

import java.sql.SQLException;
import java.util.Optional;

public class BattleService {

    private final UserRepositoryDatabase userRepositoryDatabase;
    private final BattleRepositoryDatabase battleRepositoryDatabase;
    private String log;
    public BattleService(UserRepositoryDatabase userRepositoryDatabase, BattleRepositoryDatabase battleRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.battleRepositoryDatabase = battleRepositoryDatabase;
        this.log = "";
    }

    // checks user authentication and then opens or joins a game
    public Object[] startBattle(Request request) throws SQLException {
        Object[] arr = new Object[2];
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            arr[0] = 1;
            return arr;
        }
        User foundUser = user.get();

        arr[0] = 0;
        arr[1] = "\n" + foundUser.getUsername() + "'s thread: \n" + openBattle(foundUser);

        return arr;
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? quintet in CardService + DeckService + ScoreboardService + StatsController
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

    // searches for an open battle; starts a new one or joins an open one
    private synchronized String openBattle(User user) throws SQLException {
        Optional<Battle> battle = battleRepositoryDatabase.findOpenBattle(user);
        if(battle.isEmpty()){
            battleRepositoryDatabase.startNewBattle(user);
            try {
                wait();                                 // waits till the user stops the process
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            Battle foundBattle = battle.get();
            battleRepositoryDatabase.joinOpenBattle(user, foundBattle);
            Arena arena = new Arena();
            log = arena.prepareArena(foundBattle.getId());           // start of arena, log is returned when finished
            notify();
        }
        return getReturnLog(log);
    }

    // returning error if string is empty
    private String getReturnLog(String log) throws SQLException {
        if(log.isEmpty()){
            return "Opsie daysie WHAT THE FUCK IS HAPPENING HERE";          // TODO error handling :>
        }
        return log;
    }
}
