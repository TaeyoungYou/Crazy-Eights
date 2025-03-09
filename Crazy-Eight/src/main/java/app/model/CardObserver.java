package app.model;

public interface CardObserver {
    void update(Card card);
    void updateAddToDeck(Card card);
}
