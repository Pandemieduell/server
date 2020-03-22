package de.pandemieduell.model;

import java.util.LinkedList;
import java.util.List;

public class CardDeck {
    private List<GovernmentCard> governmentCards;
    private List<PandemicCard> pandemicCards;
    private List<EventCard> eventCards;

    public CardDeck() {
        this.governmentCards = new LinkedList<>();
        this.pandemicCards = new LinkedList<>();
        this.eventCards = new LinkedList<>();
    }

    public List<GovernmentCard> getGovernmentCards() {
        return governmentCards;
    }

    public List<PandemicCard> getPandemicCards() {
        return pandemicCards;
    }

    public List<EventCard> getEventCards() {
        return eventCards;
    }

    public void insertGovernmentCard(GovernmentCard card){
        this.governmentCards.add(card);
    }
    public void insertPandemicCard(PandemicCard card){
        this.pandemicCards.add(card);
    }
    public void insertEventCard(EventCard card){
        this.eventCards.add(card);
    }
}
