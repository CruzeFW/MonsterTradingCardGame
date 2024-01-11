package at.technikum.apps.mtcg.game;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepositoryDatabase;
import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.apps.mtcg.util.CardTypeParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

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
        while(!deckUser1.isEmpty() || !deckUser2.isEmpty() || counter < 100 ){
            Card player1Card = chooseRandom(deckUser1);
            Card player2Card = chooseRandom(deckUser2);

            Card loser = fight(player1Card, player2Card);

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
        Random random = new Random();
        int chosenCard = random.nextInt(deck.size());

        return deck.get(chosenCard);
    }

    //
    private Card fight(Card player1Card, Card player2Card){
        String typeCard1 = cardTypeParser.getTypeFromCard(player1Card);
        String typeCard2 = cardTypeParser.getTypeFromCard(player2Card);
        String elementCard1 = cardTypeParser.getElementFromCard(player1Card);
        String elementCard2 = cardTypeParser.getElementFromCard(player2Card);

        if(typeCard1.equals("monster") && typeCard2.equals("monster")){
            return compareEqualTypeDamage(player1Card, player2Card);
        }else{
            // TODO HIER WEITERMACHEN
            // compareElements(player1Card, player2Card);
            if(elementCard1.equals("normal") && elementCard2.equals("normal")){
                return compareEqualTypeDamage(player1Card, player2Card);
            } else if (elementCard1.equals("normal") && elementCard2.equals("water")) {

            }
        }
        return null;
    }


    private Card compareEqualTypeDamage(Card player1Card, Card player2Card){
        if(player1Card.getDamage() > player2Card.getDamage()){
            return player2Card;
        }else{
            return player1Card;
        }
    }
    private void compareElements(Card player1Card, Card player2Card){



    }
}
