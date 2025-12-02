package app.model.multi;

import javafx.util.Pair;

import java.util.List;

/**
 * A data transfer object (DTO) for managing operations related to a game.
 * This class provides static utility methods to initialize game components
 * such as decks and players, and to handle actions like drawing and playing cards.
 */
public class GameDTO {

    /**
     * Initializes a deck of cards based on the given data string. Each card is specified
     * as a combination of a suit and a rank, separated by a colon. The cards are separated
     * by spaces in the data string. Each parsed card is added to the provided deck.
     *
     * @param data the string containing card data in the format "suit:rank suit:rank ..."
     * @param deck the Deck object to which the parsed cards will be added
     */
    public static void initDeck(String data, Deck deck) {
        String[] cards = data.split(" ");
        for (String set : cards) {
            int suit = Integer.parseInt(set.split(":")[0]);
            int rank = Integer.parseInt(set.split(":")[1]);
            Card card = new Card(suit, rank);
            deck.add(card);
        }
    }

    /**
     * Initializes players based on the given data string. Each player is specified
     * as a combination of an id and an icon, separated by a colon. The players
     * are separated by spaces in the data string. Each parsed player is added
     * to the provided players and users lists.
     *
     * @param data    the string containing player data in the format "id:icon id:icon ..."
     * @param players the list to which all parsed players will be added
     * @param users   the list to which all parsed players will be added (same as players)
     */
    public static void initPlayers(String data, List<Player> players, List<Player> users) {
        String[] player = data.split(" ");
        for (String p : player) {
            int id = Integer.parseInt(p.split(":")[0]);
            String icon = p.split(":")[1];

            Player temp = new Player(id);
            temp.setPlayer(true);
            temp.setIcon(icon, true);
            players.add(temp);
            users.add(temp);
        }
    }

    /**
     * Parses player data from the provided string and adds the players to the given list.
     * Each player is specified using an id and an icon, separated by a colon. The players
     * are separated by spaces in the data string. If a player with the same id already
     * exists in the list, it will not be added again.
     *
     * @param data    the string containing player data in the format "id:icon id:icon ..."
     * @param players the list to which all parsed players will be added
     */
    public static void createPlayers(String data, List<Player> players) {
        String[] player = data.split(" ");
        for (String p : player) {
            int id = Integer.parseInt(p.split(":")[0]);
            String icon = p.split(":")[1];
            boolean exists = false;
            for (Player pl : players) {
                if (pl.getNetworkId() == id) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Player temp = new Player(id);
                temp.setPlayer(false);
                temp.setIcon(icon, true);
                players.add(temp);
            }
        }
    }

    /**
     * Parses the provided data string to extract an identifier and a card.
     * The data string should be formatted as "id suit:rank", where:
     * - id is an integer representing the identifier.
     * - suit and rank represent the suit and rank of the card respectively.
     *
     * @param data the string containing the card data in the format "id suit:rank".
     * @return a Pair containing the identifier as an Integer and the parsed Card object.
     */
    public static Pair<Integer, Card> drawCard(String data) {
        String[] info = data.split(" ");
        int id = Integer.parseInt(info[0]);
        String[] card = info[1].split(":");

        return new Pair<>(id, new Card(Integer.parseInt(card[0]), Integer.parseInt(card[1])));
    }

    /**
     * Parses the provided data string to extract card information and creates a Card object.
     * The input string should be formatted as "suit:rank", where:
     * - suit is an integer representing the suit of the card.
     * - rank is an integer representing the rank of the card.
     *
     * @param data the string containing the card data in the format "suit:rank".
     * @return a Card object created based on the parsed suit and rank.
     */
    public static Card putDummy(String data) {
        String[] info = data.split(":");
        return new Card(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
    }

    /**
     * Parses the provided data string to extract an identifier and a card.
     * The data string should be formatted as "id suit:rank", where:
     * - id is an integer representing the identifier.
     * - suit and rank represent the suit and rank of the card respectively.
     *
     * @param data the string containing the card data in the format "id suit:rank"
     * @return a Pair containing the identifier as an Integer and the parsed Card object
     */
    public static Pair<Integer, Card> putCard(String data) {
        String[] info = data.split(" ");
        int id = Integer.parseInt(info[0]);
        String[] card = info[1].split(":");
        return new Pair<>(id, new Card(Integer.parseInt(card[0]), Integer.parseInt(card[1])));
    }

}
