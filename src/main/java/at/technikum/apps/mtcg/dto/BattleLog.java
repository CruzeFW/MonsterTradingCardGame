package at.technikum.apps.mtcg.dto;

import at.technikum.apps.mtcg.entity.User;

public class BattleLog {
    public String log;
    public BattleLog(){
        this.log = "*** Start of battle log ***\n";
    }

    public void addUsers(User user1, User user2){
        log += "Current players: \n" +
                user1.getUsername() +
                " vs. " +
                user2.getUsername() + "\n";
    }

    public void addRoundCounter(Integer counter){
        log += "Round " + counter+1 + ":\n";
    }

    public void addCards(FightingCard p1, FightingCard p2){
        log += "  " + p1.getOwnerName() + "'s card: \n" +
                "    Name: " + p1.getName() + "\n" +
                "    Damage: " + p1.getDamage() + "\n" +
                "       *** vs *** \n"+
                "  " + p2.getOwnerName() + "'s card: \n" +
                "    Name: " + p2.getName() + "\n" +
                "    Damage: " + p2.getDamage() + "\n";
    }

    public void addCardsWithConditionalDamage(FightingCard p1, FightingCard p2){
        log += " Final cards to battle: \n" +
                "   Name: " + p1.getName() + "\n" +
                "   Damage: " + p1.getCurrentDamage() + "\n" +
                "       *** vs *** \n"+
                "   Name: " + p2.getName() + "\n" +
                "   Damage: " + p2.getCurrentDamage() + "\n";
    }

    public void addSpecial(Integer i){
        log += "  A special effect has been triggered: \n";
        switch (i){
            case 1:
                log += "   - The fireElf dodges the attack of the Dragon!\n";
                break;
            case 2:
                log += "   - The Goblin is afraid of the Dragon and won't attack!\n";
                break;
            case 3:
                log += "   - The WaterSpell drowns the Knight!\n";
                break;
            case 4:
                log += "   - The Kraken takes no damage from Spells\n";
                break;
        }
    }

    public void drawInRound(){
        log += "DRAW!\n";
    }

    public void roundWinner(FightingCard fc){
        log += "WINNER of current round: \n" +
                "*** " + fc.getOwnerName() + "'s "+ fc.getName() + " ***\n";
    }

    public void removeCard(FightingCard fc){
        log += "  Removing " + fc.getName() + " from " + fc.getOwnerName() + "'s deck.\n";
    }

    public void effectiveness(Integer i, FightingCard p1, FightingCard p2){
        log += "  The effectiveness of the elements changes the cards damages: \n";
        switch (i){
            case 1:
                log += "  + Fire card: \n   - default damage: " + p1.getDamage() + "\n"+
                        "   - current damage: "+ p1.getCurrentDamage() + "\n"+
                        "  + Water card: \n   - default damage: " + p2.getDamage() + "\n"+
                        "   - current damage: "+ p2.getCurrentDamage() + "\n";
                break;
            case 2:
                log += "  + Fire card: \n   - default damage: " + p1.getDamage() + "\n"+
                        "   - current damage: "+ p1.getCurrentDamage() + "\n" +
                        "  + Normal card: \n    - default damage: " + p2.getDamage() + "\n"+
                        "   - current damage: "+ p2.getCurrentDamage() + "\n";
                break;
            case 3:
                log += "  + Water card: \n   - default damage: " + p1.getDamage() + "\n"+
                        "   - current damage: "+ p1.getCurrentDamage() + "\n"+
                        "  + Fire card: \n   - default damage: " + p2.getDamage() + "\n"+
                        "   - current damage: "+ p2.getCurrentDamage()+ "\n";
                break;
            case 4:
                log += "  + Water card: \n   - default damage: " + p1.getDamage() + "\n"+
                        "   - current damage: "+ p1.getCurrentDamage() + "\n" +
                        "  + Normal card: \n   - default damage: " + p2.getDamage() + "\n"+
                        "   - current damage: "+ p2.getCurrentDamage()+ "\n";
                break;
            case 5:
                log += "  + Normal card: \n   - default damage: " + p1.getDamage() + "\n"+
                        "   - current damage: "+ p1.getCurrentDamage() + "\n" +
                        "  + Water card: \n   - default damage: " + p2.getDamage() + "\n"+
                        "   - current damage: "+ p2.getCurrentDamage()+ "\n";
                break;
            case 6:
                log += "  + Normal card: \n   - default damage: " + p1.getDamage() + "\n"+
                        "   - current damage: "+ p1.getCurrentDamage() + "\n" +
                        "  + Fire card: \n   - default damage: " + p2.getDamage() + "\n"+
                        "   - current damage: "+ p2.getCurrentDamage()+ "\n";
                break;
        }
    }

    public void finalDraw(){
        log += """
                --------------------
                RESULT:\s
                        DRAW
                --------------------
                """;

    }

    public void addOutcome(User user, User otherUser){
        log += "--------------------\n" +
                "RESULT:\n"+
                "Winner: " + otherUser.getUsername() + "\n" +
                "Loser: " + user.getUsername() + "\n" +
                "--------------------\n";
    }

    public void endLog(){
        log += "\n" +
                "******    End of log    ******";
    }
}
