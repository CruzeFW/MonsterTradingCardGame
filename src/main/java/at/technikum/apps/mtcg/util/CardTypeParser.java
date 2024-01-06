package at.technikum.apps.mtcg.util;

import at.technikum.apps.mtcg.entity.Card;


public class CardTypeParser {
    public CardTypeParser(){}

    public String getTypeFromCard(Card card){
        String name = card.getName();
        if(name.contains("Spell")){
            return "spell";
        }else{
            return "monster";
        }
    }

    public String getElementFromCard(Card card){
        String name = card.getName();
        if(name.contains("Fire")){
            return "fire";
        }else if(name.contains("Water")){
            return "water";
        } else{
            return null;
        }
    }




}
