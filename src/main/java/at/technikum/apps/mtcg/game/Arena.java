package at.technikum.apps.mtcg.game;

import at.technikum.apps.mtcg.dto.BattleLog;
import at.technikum.apps.mtcg.dto.FightingCard;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.CardRepositoryDatabase;
import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.CardTypeParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Arena {

    private final BattleRepositoryDatabase battleRepositoryDatabase;
    private final DeckRepositoryDatabase deckRepositoryDatabase;
    private final UserRepositoryDatabase userRepositoryDatabase;
    private final CardRepositoryDatabase cardRepositoryDatabase;
    private final CardTypeParser cardTypeParser;

    public Arena(){
        this.battleRepositoryDatabase = new BattleRepositoryDatabase();
        this.deckRepositoryDatabase = new DeckRepositoryDatabase();
        this.userRepositoryDatabase = new UserRepositoryDatabase();
        this.cardRepositoryDatabase = new CardRepositoryDatabase();
        this.cardTypeParser = new CardTypeParser();
        user1 = new User();
        user2 = new User();
        winner = new User();
        loser = new User();
        this.removeFromPlayer1 = new ArrayList<>();
        this.removeFromPlayer2 = new ArrayList<>();
        this.battleLog = new BattleLog();
    }

    private Battle battle;
    private User user1;
    private User user2;
    private User winner;
    private User loser;
    private ArrayList<Card> deckUser1;
    private ArrayList<Card> deckUser2;
    private ArrayList<Card> removeFromPlayer1;
    private ArrayList<Card> removeFromPlayer2;
    private BattleLog battleLog;

    // get battle, users and decks into arena
    public String prepareArena(Integer id) throws SQLException{
        // get battle from db
        battle = battleRepositoryDatabase.findBattleWithId(id);
        user1.setId(battle.getUser1());
        user1 = userRepositoryDatabase.getUserAndEloWithId(user1);
        user2.setId(battle.getUser2());
        user2 = userRepositoryDatabase.getUserAndEloWithId(user2);

        // get deck for each user
        Optional<ArrayList<Card>> deck1 = deckRepositoryDatabase.findDeck(user1);
        deck1.ifPresent(cards -> deckUser1 = cards);

        Optional<ArrayList<Card>> deck2 = deckRepositoryDatabase.findDeck(user2);
        deck2.ifPresent(cards -> deckUser2 = cards);

        startArena();
        return battleLog.log;
    }

    // get cards to battle, battles for as long as the round max is reached or a user has no more cards
    public void startArena() throws SQLException {
        battleLog.addUsers(user1, user2);
        int counter = 0;
        while(!deckUser1.isEmpty() && !deckUser2.isEmpty() && counter < 100 ){
            Card player1Card = chooseRandom(deckUser1);
            Card player2Card = chooseRandom(deckUser2);
            FightingCard p1 = new FightingCard(
                    player1Card.getName(),
                    player1Card.getOwner(),
                    user1.getUsername(),
                    player1Card.getDamage(),
                    player1Card.getDamage(),
                    cardTypeParser.getTypeFromCard(player1Card),
                    cardTypeParser.getElementFromCard(player1Card),
                    "");
            FightingCard p2 = new FightingCard(
                    player2Card.getName(),
                    player2Card.getOwner(),
                    user2.getUsername(),
                    player2Card.getDamage(),
                    player2Card.getDamage(),
                    cardTypeParser.getTypeFromCard(player2Card),
                    cardTypeParser.getElementFromCard(player2Card),
                    "");

            battleLog.addRoundCounter(counter);
            battleLog.addCards(p1, p2);

            FightingCard loser = fight(p1, p2);
            counter++;
            if(loser == null){
                continue;
            }
            removeCardFromDeck(loser, player1Card, player2Card);
        }

        closeArena(counter);

    }

    // randomizer to get a random card from the deck
    private Card chooseRandom(ArrayList<Card> deck){
        Collections.shuffle(deck);
        return deck.getFirst();
    }

    // fight me AMK
    private FightingCard fight(FightingCard p1, FightingCard p2){

        p1.setSpecial(checkSpecial(p1, p2));
        p2.setSpecial(checkSpecial(p2, p1));

        // check if there is a special condition set
        if(!p1.getSpecial().isEmpty() || !p2.getSpecial().isEmpty()){
            p1.setCurrentDamage(p1.getCurrentDamage() * adaptDamageWithSpecial(p1));
            p2.setCurrentDamage(p2.getCurrentDamage() * adaptDamageWithSpecial(p2));
            return calculateDamage(p1, p2);
        }

        // check if pure monster fight or mixed fight
        if(p1.getType().equals("monster") && p2.getType().equals("monster")){
            return calculateDamage(p1, p2);
        }else{
             return checkEffectiveness(p1, p2);
        }
    }

    // check special conditions, add to log and return condition
    private String checkSpecial(FightingCard fc, FightingCard otherFc){
        if(fc.getName().equals("Dragon") && otherFc.getName().equals("FireElf")){
            battleLog.addSpecial(1);
            return "miss";
        }
        if(fc.getName().contains("Goblin") && otherFc.getName().equals("Dragon")){
            battleLog.addSpecial(2);
            return "afraid";
        }
        if(fc.getName().equals("WaterSpell") && otherFc.getName().equals("Knight")){
            battleLog.addSpecial(3);
            return "maxDmg";
        }
        if(fc.getType().equals("spell") && otherFc.getName().equals("Kraken")){
            battleLog.addSpecial(4);
            return "noDmg";
        }
        return "";
    }

    // return factor to calculate damage depending on the given condition
    private float adaptDamageWithSpecial(FightingCard fc){
        switch (fc.getSpecial()){
            case "miss", "afraid", "noDmg":
                return 0;
            case "maxDmg":
                return 100;
            default:
                return 1;
        }
    }

    // compare currentDamage and return loser of fight
    private FightingCard calculateDamage(FightingCard p1, FightingCard p2){
        battleLog.addCardsWithConditionalDamage(p1,p2);
        if(p1.getCurrentDamage() == p2.getCurrentDamage()){
            battleLog.drawInRound();
            return null;
        }else if(p1.getCurrentDamage() > p2.getCurrentDamage()){
            battleLog.roundWinner(p1);
            return p2;
        }else{
            battleLog.roundWinner(p2);
            return p1;
        }
    }

    // compares elements and changes the corresponding damage
    private FightingCard checkEffectiveness(FightingCard p1, FightingCard p2){
        switch (p1.getElement()){
            case "fire":
                if (p2.getElement().equals("water")){
                    p1.setCurrentDamage(p1.getCurrentDamage() / 2);         // not effective
                    p2.setCurrentDamage(p2.getCurrentDamage() * 2);         // effective

                    battleLog.effectiveness(1, p1, p2);
                } else if (p2.getElement().equals("normal")) {
                    p1.setCurrentDamage(p1.getCurrentDamage() * 2);         // effective
                    p2.setCurrentDamage(p2.getCurrentDamage() / 2);         // not effective

                    battleLog.effectiveness(2, p1, p2);
                }
            case "water":
                if (p2.getElement().equals("fire")){
                    p1.setCurrentDamage(p1.getCurrentDamage() * 2);         // effective
                    p2.setCurrentDamage(p2.getCurrentDamage() / 2);         // not effective

                    battleLog.effectiveness(3, p1, p2);
                } else if (p2.getElement().equals("normal")) {
                    p1.setCurrentDamage(p1.getCurrentDamage() / 2);         // not effective
                    p2.setCurrentDamage(p2.getCurrentDamage() * 2);         // effective

                    battleLog.effectiveness(4, p1, p2);
                }
            case "normal":
                if (p2.getElement().equals("water")){
                    p1.setCurrentDamage(p1.getCurrentDamage() * 2);         // effective
                    p2.setCurrentDamage(p2.getCurrentDamage() / 2);         // not effective

                    battleLog.effectiveness(5, p1, p2);
                } else if (p2.getElement().equals("fire")) {
                    p1.setCurrentDamage(p1.getCurrentDamage() / 2);         // not effective
                    p2.setCurrentDamage(p2.getCurrentDamage() * 2);         // effective

                    battleLog.effectiveness(6, p1, p2);
                }
        }

        return calculateDamage(p1, p2);
    }

    // removes card from active deck and saves it into ArrayList for later use
    private void removeCardFromDeck(FightingCard loser, Card p1c, Card p2c){
        battleLog.removeCard(loser);
        if(p1c.getOwner().equals(loser.getOwnerId())){
            removeFromPlayer1.add(p1c);
            deckUser1.remove(p1c);
        }else{
            removeFromPlayer2.add(p2c);
            deckUser2.remove(p2c);
        }
    }

    // updates elo, DB, and card owner
    private void closeArena(Integer counter) throws SQLException {
        if(counter == 100 && !deckUser1.isEmpty() && !deckUser2.isEmpty()){
            // OPTIONAL
//            updateElo(user1, user2, false);
            battleLog.finalDraw();
            battleLog.endLog();

            battleRepositoryDatabase.updateAfterBattle(battle,null, null, battleLog.log);
        }else{
            if(deckUser1.isEmpty()){
                battleLog.addOutcome(user1, user2);
                winner = user2;
                loser = user1;
            }else{
                battleLog.addOutcome(user2, user1);
                winner = user1;
                loser = user2;
            }
            ArrayList<Card> changeOwner = findFullList(removeFromPlayer1, removeFromPlayer2);

            for(Card card : changeOwner){
                card.setOwner(winner.getId());
                cardRepositoryDatabase.updateOwner(card);
            }

            updateElo(winner, loser, true);

            battleLog.endLog();

            battleRepositoryDatabase.updateAfterBattle(battle, winner.getId(), loser.getId(), battleLog.log);
        }
    }

    // return the list which is full
    private ArrayList<Card> findFullList(ArrayList<Card> p1, ArrayList<Card> p2){
        if(p1.size() == 4){
            return p1;
        }else return p2;
    }

    // update user elo values (also implemented for draw, currently disabled, as not necessary)
    private void updateElo(User user, User otherUser, boolean won) throws SQLException {
        if(won){
            user.setElo(user.getElo() + 14);
            otherUser.setElo(otherUser.getElo() - 9);
//        }else{
//            user.setElo(user.getElo() - 3);
//            otherUser.setElo(otherUser.getElo() - 3);
        }
        userRepositoryDatabase.updateElo(user);
        userRepositoryDatabase.updateElo(otherUser);
    }
}
