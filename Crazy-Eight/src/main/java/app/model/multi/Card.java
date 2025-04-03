package app.model.multi;

/**
 * Represents a playing card with a suit and rank.
 * The card's suit may be one of four standard suits: Spades, Hearts, Diamonds, Clubs.
 * The rank ranges from 0 to 12, representing Ace through King respectively.
 */
public class Card {
    private int suit;
    private int rank;

    /**
     * Default constructor for the Card class.
     * Initializes the suit and rank of the card to -1,
     * representing an unassigned or empty card state.
     */
    public Card() {
        this.suit = -1;
        this.rank = -1;
    }

    /**
     * Constructor for the Card class.
     * Initializes the card with its suit and rank values.
     *
     * @param suit the suit of the card, represented as an integer (e.g., 0 for Spades, 1 for Hearts, etc.)
     * @param rank the rank of the card, represented as an integer (e.g., 0 for Ace, 11 for Queen, etc.)
     */
    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Retrieves the suit of the card.
     * The suit is represented as an integer, where:
     * 0 = Spades, 1 = Hearts, 2 = Diamonds, and 3 = Clubs.
     *
     * @return the integer value representing the suit of the card.
     */
    public int getSuit() {
        return suit;
    }


    /**
     * Retrieves the rank of the card.
     * The rank is represented as an integer, where:
     * 0 = Ace, 1 to 10 = Two to Ten, 11 = Queen, and 12 = King.
     *
     * @return the integer value representing the rank of the card.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Converts the rank of the card to its corresponding string representation.
     * The mapping is as follows:
     **/
    public String getRankString() {
        if (rank == 0) return "A";
        if (rank == 10) return "J";
        if (rank == 11) return "Q";
        if (rank == 12) return "K";
        return String.valueOf(rank + 1);
    }


    /**
     * Retrieves the emoji representation of the suit of the card.
     * The suits are represented as follows:
     * 0 = "♠", 1 = "♥", 2 = "♦", 3 = "♣". If the suit does not match
     * these values, "Empty" is returned to indicate an unassigned or invalid state.
     *
     * @return a String representing the emoji for the suit of the card,
     *         or "Empty" if the suit is invalid.
     */
    public String getImogeSuit() {
        if (suit == 0) return "♠";
        if (suit == 1) return "♥";
        if (suit == 2) return "♦";
        if (suit == 3) return "♣";
        return "Empty";
    }

    /**
     * Retrieves the Korean name of the card's suit based on its integer representation.
     * The mapping is as follows:
     * 0 = "스페이드" (Spades)
     * 1 = "하트" (Hearts)
     * 2 = "다이아몬드" (Diamonds)
     * 3 = "클로버" (Clubs)
     * If the suit does not match one of these values, "비어있음" (Empty) is returned.
     *
     * @return the Korean name of the card's suit or "비어있음" if the suit is invalid.
     */
    public String getKoreanSuit() {
        if (suit == 0) return "스페이드";
        if (suit == 1) return "하트";
        if (suit == 2) return "다이아몬드";
        if (suit == 3) return "클로버";
        return "비어있음";
    }

    /**
     * Generates and returns the URL of the card image based on the card's suit and rank.
     * If the card is unassigned (both suit and rank are -1), it returns the URL for an empty card image.
     * Otherwise, it constructs the URL using the format "/card/Card-{suit}-{rank}.png".
     *
     * @return a String representing the URL of the card image. If both suit and rank are -1,
     *         the URL "/card/Card-Empty.png" is returned. For other cases, the URL is
     *         "/card/Card-{suit}-{rank}.png".
     */
    public String getCardURL() {
        if (suit == -1 && rank == -1) {
            return "/card/Card-Empty.png";
        }
        return String.format("/card/Card-%d-%d.png", suit, rank);
    }

    /**
     * Compares this Card object to another object to determine equality.
     * Two Card objects are considered equal if they have the same suit and rank.
     *
     * @param o the object to compare with this Card.
     * @return true if the specified object is a Card with the same suit and rank as this card, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.suit == card.suit && this.rank == card.rank;
    }
}
