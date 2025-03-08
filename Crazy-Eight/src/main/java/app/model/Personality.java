package app.model;

public enum Personality {
    Friendly,
    Sarcastic,
    Serious,
    Flirty,
    Lazy,
    Edgy;

    public static Personality getRandomPersonality(){
        return Personality.values()[(int) (Math.random() * Personality.values().length)];
    }
}
