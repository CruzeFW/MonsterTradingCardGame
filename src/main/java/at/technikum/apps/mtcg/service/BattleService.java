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
    public BattleService(UserRepositoryDatabase userRepositoryDatabase, BattleRepositoryDatabase battleRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.battleRepositoryDatabase = battleRepositoryDatabase;
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

        // TODO BATTLELOGIC enter lobby
        openBattle(foundUser);

        // write into log...

        arr[0] = 0;
        arr[1] = "aaaaaa";
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
    private synchronized void openBattle(User user) throws SQLException {

        // TODO THREAD HANDLING HERE
        Optional<Battle> battle = battleRepositoryDatabase.findOpenBattle(user);
        if(battle.isEmpty()){
            battleRepositoryDatabase.startNewBattle(user);
            try {
                wait();                                 // wartet bis das battle aus is?!
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            Battle foundBattle = battle.get();
            battleRepositoryDatabase.joinOpenBattle(user, foundBattle);
            Arena.startBattle(foundBattle.getId());                // hier alles mitgeben was das battle braucht (Deck, user etc)
            notify();
        }
    }
}
