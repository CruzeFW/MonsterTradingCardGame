package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DeckRepositoryDatabase;
import at.technikum.server.http.Request;

public class DeckService {
    public DeckRepositoryDatabase deckRepositoryDatabase = new DeckRepositoryDatabase();

    public DeckService(DeckRepositoryDatabase deckRepositoryDatabase){
        this.deckRepositoryDatabase = deckRepositoryDatabase;
    }

    public Object[] getDeck(Request request){
        Object[] arr = new Object[2];

        //TODO hier weiter machen
        return arr;
    }

    public Integer updateDeck(Request request){
        int response = 0;
        //TODO hier weiter machen
        return response;
    }
}
