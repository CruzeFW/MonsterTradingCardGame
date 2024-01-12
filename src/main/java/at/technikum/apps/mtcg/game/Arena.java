package at.technikum.apps.mtcg.game;

import at.technikum.apps.mtcg.dto.FightingCard;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.apps.mtcg.util.CardTypeParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Arena {

    private final BattleRepositoryDatabase battleRepositoryDatabase;
    private final DeckRepositoryDatabase deckRepositoryDatabase;
    private final CardTypeParser cardTypeParser;

    public Arena(){
        this.battleRepositoryDatabase = new BattleRepositoryDatabase();
        this.deckRepositoryDatabase = new DeckRepositoryDatabase();
        this.cardTypeParser = new CardTypeParser();
        user1 = new User();
        user2 = new User();
    };

    private Battle battle;
    private User user1;
    private User user2;
    private ArrayList<Card> deckUser1;
    private ArrayList<Card> deckUser2;
    private ArrayList<Card> removeFromPlayer1;
    private ArrayList<Card> removeFromPlayer2;

    public void startArena() throws SQLException {
        System.out.println("in arena logic");
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

            FightingCard loser = fight(p1, p2);


            // enter more code here :)

            counter++;
        }

    }

    // get battle, users and decks into arena
    public void prepareArena(Integer id) throws SQLException{
        // get battle from db
        battle = battleRepositoryDatabase.findBattleWithId(id);
        user1.setId(battle.getUser1());
        user2.setId(battle.getUser2());

        // get deck for each user
        Optional<ArrayList<Card>> deck1 = deckRepositoryDatabase.findDeck(user1);
        deck1.ifPresent(cards -> deckUser1 = cards);

        Optional<ArrayList<Card>> deck2 = deckRepositoryDatabase.findDeck(user2);
        deck2.ifPresent(cards -> deckUser2 = cards);

        startArena();
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
            p1.setCurrentDamage(p1.getCurrentDamage() * adaptDamageWithSpecial(p1, p2));
            p2.setCurrentDamage(p2.getCurrentDamage() * adaptDamageWithSpecial(p2, p1));
            return calculateDamage(p1, p2);
        }

        // pure monster fight
        if(p1.getType().equals("monster") && p2.getType().equals("monster")){
            return calculateDamage(p1, p2);
        }else{
            // mixed fights
            // TODO HIER WEITERMACHEN
             return compareTypes(p1, p2);

        }
    }

    private String checkSpecial(FightingCard card, FightingCard otherCard){
        if(card.getName().equals("Dragon") && otherCard.getName().equals("FireElf")){
            return "miss";
        }
        if(card.getName().contains("Goblin") && otherCard.getName().equals("Dragon")){
            return "afraid";
        }
        if(card.getName().equals("WaterSpell") && otherCard.getName().equals("Knight")){
            return "maxDmg";
        }
        if(card.getType().equals("spell") && otherCard.getName().equals("Kraken")){
            return "noDmg";
        }
        return "";
    }

    private float adaptDamageWithSpecial(FightingCard card, FightingCard other){
        switch (card.getSpecial()){
            case "miss", "afraid", "noDmg":
                return 0;
            case "maxDmg":
                return 100;
            default:
                return 1;
        }
    }

    private FightingCard calculateDamage(FightingCard p1, FightingCard p2){
        if(p1.getCurrentDamage() > p2.getCurrentDamage()){
            return p2;
        }else{
            return p1;
        }
    }

    private FightingCard compareTypes(FightingCard p1, FightingCard p2){
        switch (p1.getType()){
            case "monster":
                if(p2.getType().equals("monster")){
                    return calculateDamage(p1, p2);
                }else {
                    return checkEffectiveness(p1, p2);
                }
            case "spell":
                return checkEffectiveness(p1, p2);
        }
        return null;
    }

    private FightingCard checkEffectiveness(FightingCard p1, FightingCard p2){
        switch (p1.getElement()){
            case "fire":
                //if(p2.)
        }



        return calculateDamage(p1, p2);
    }
}
