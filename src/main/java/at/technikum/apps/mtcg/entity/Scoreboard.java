package at.technikum.apps.mtcg.entity;

import java.util.ArrayList;

public class Scoreboard {

    public Scoreboard(){};
    public Scoreboard (ArrayList<Stats> scoreboard){
        this.scoreboard = scoreboard;
    }

    ArrayList<Stats> scoreboard = new ArrayList<>();

}
