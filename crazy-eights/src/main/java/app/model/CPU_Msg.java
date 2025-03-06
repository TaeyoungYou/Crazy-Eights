package app.model;

public class CPU_Msg {
    private final static String[] aiThoughts = {
            "Hmm... which card should I play?",
            "This is a tough choice...",
            "I need to think carefully.",
            "Should I be aggressive or play it safe?",
            "What is my opponent planning?",
            "Maybe I should hold onto this card for later.",
            "Is this the right move?",
            "I have a bad feeling about this...",
            "I think this is my best option.",
            "Let's take a calculated risk.",
            "This might be a mistake...",
            "I need to consider all my options.",
            "What would a human player do here?",
            "Alright, let's go with this one.",
            "I hope this works...",
            "This could change everything!",
            "Time to make a bold move!",
            "What are the odds of winning if I play this?",
            "I should analyze the probabilities first.",
            "Alright, decision time!"
    };
    public static String getMessage(int i){
        return aiThoughts[i];
    }
    public static int getSize(){
        return aiThoughts.length;
    }
}
