package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.repository.*;
import at.technikum.apps.mtcg.service.*;

import java.util.ArrayList;
import java.util.List;

public class Injector {

    public List<Controller> createController() {
        BattleRepositoryDatabase battleRepositoryDatabase = new BattleRepositoryDatabase();
        CardRepositoryDatabase cardRepositoryDatabase = new CardRepositoryDatabase();
        DeckRepositoryDatabase deckRepositoryDatabase = new DeckRepositoryDatabase();
        PackageRepositoryDatabase packageRepositoryDatabase = new PackageRepositoryDatabase();
        ScoreboardRepositoryDatabase scoreboardRepositoryDatabase = new ScoreboardRepositoryDatabase();
        StatsRepositoryDatabase statsRepositoryDatabase = new StatsRepositoryDatabase();
        TokenRepositoryDatabase tokenRepositoryDatabase = new TokenRepositoryDatabase();
        TradingRepositoryDatabase tradingRepositoryDatabase = new TradingRepositoryDatabase();
        TransactionRepositoryDatabase transactionRepositoryDatabase = new TransactionRepositoryDatabase();
        UserRepositoryDatabase userRepositoryDatabase = new UserRepositoryDatabase();


        BattleService battleService = new BattleService(userRepositoryDatabase, battleRepositoryDatabase);
        CardService cardService = new CardService(userRepositoryDatabase, cardRepositoryDatabase);
        DeckService deckService = new DeckService(deckRepositoryDatabase, userRepositoryDatabase);
        PackageService packageService = new PackageService(packageRepositoryDatabase, cardRepositoryDatabase);
        ScoreboardService scoreboardService = new ScoreboardService(statsRepositoryDatabase, userRepositoryDatabase, scoreboardRepositoryDatabase);
        SessionService sessionService = new SessionService(tokenRepositoryDatabase);
        StatsService statsService = new StatsService(statsRepositoryDatabase, userRepositoryDatabase);
        TradingService tradingService = new TradingService(userRepositoryDatabase, tradingRepositoryDatabase, cardRepositoryDatabase);
        TransactionService transactionService = new TransactionService(transactionRepositoryDatabase);
        UserService userService = new UserService(userRepositoryDatabase);


        List<Controller> controllerList = new ArrayList<>();
        controllerList.add(new BattleController(battleService));
        controllerList.add(new CardController(cardService));
        controllerList.add(new DeckController(deckService));
        controllerList.add(new PackageController(packageService));
        controllerList.add(new ScoreboardController(scoreboardService));
        controllerList.add(new SessionController(sessionService));
        controllerList.add(new StatsController(statsService));
        controllerList.add(new TradingController(tradingService));
        controllerList.add(new TransactionController(transactionService));
        controllerList.add(new UserController(userService));

        return controllerList;
    }
}
