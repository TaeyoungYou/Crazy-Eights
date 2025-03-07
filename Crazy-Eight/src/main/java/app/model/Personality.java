package app.model;

public enum Personality {
    Friendly,
    Sarcastic,
    Chaotic,
    Serious,
    Mysterious,
    Flirty,
    Lazy;

    public static Personality getRandomPersonality(){
        return Personality.values()[(int) (Math.random() * Personality.values().length)];
    }
}
