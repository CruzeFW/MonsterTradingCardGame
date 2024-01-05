package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.server.http.Request;

import java.util.Optional;

public class BattleService {

    private final UserRepositoryDatabase userRepositoryDatabase;
    private final BattleRepositoryDatabase battleRepositoryDatabase;
    public BattleService(UserRepositoryDatabase userRepositoryDatabase, BattleRepositoryDatabase battleRepositoryDatabase){
        this.userRepositoryDatabase = userRepositoryDatabase;
        this.battleRepositoryDatabase = battleRepositoryDatabase;
    }

    // checks user authentication and then opens or joins a game
    public Object[] startBattle(Request request){
        Object[] arr = new Object[2];
        Optional<User> user = checkToken(request);
        if(user.isEmpty()){                // no token | user not found/not logged in
            arr[0] = 1;
            return arr;
        }
        User foundUser = user.get();
        int waitOrStart = openBattle(foundUser, request);
        // TODO BATTLELOGIC enter lobby and either wait or start game
        if(waitOrStart == 0){
            // openGame();
        }else{
            // joinGame();
        }

        // write into log...

        arr[0] = 0;
        arr[1] = "aaaaaa";
        return arr;
    }

    // check if a given token is connected to a user
    //TODO maybe auslagern? quintet in CardService + DeckService + ScoreboardService + StatsController
    private Optional<User> checkToken(Request request) {
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
    private Integer openBattle(User user, Request request){
        Optional<Battle> battle = battleRepositoryDatabase.findOpenBattle(user);
        if(battle.isEmpty()){
            battleRepositoryDatabase.startNewBattle(user);
            return 0;
        } else {
            Battle foundBattle = battle.get();
            battleRepositoryDatabase.joinOpenBattle(user, foundBattle);
            return 1;
        }
    }
}
