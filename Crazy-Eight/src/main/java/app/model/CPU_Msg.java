package app.model;

import java.util.Random;

/**
 * The CPU_Msg class provides a collection of predefined messages for CPU interactions
 * in a game, supporting both English and Korean languages. These messages include
 * greetings, notifications about game states, and responses to chat inputs.
 */
public class CPU_Msg {

    private static final String[] ENGLISH_GREETINGS = {
            "Welcome to the game! Let’s see who can get rid of their cards the fastest!",
            "I hope you're ready! The one who plays their cards right wins!",
            "This is a race to zero cards. Are you fast enough?",
            "Let’s see if you have what it takes to beat me!",
            "Luck and strategy – that’s what this game is about. Let’s begin!",
            "The faster you play, the more points you get. Think fast!",
            "Can you handle the pressure? Let’s find out!",
            "Are you lucky today? Or is it just my time to win?",
            "May the best player win! Good luck… you’ll need it!",
            "Get ready! This is going to be a fun match!"
    };

    /**
     * Provides a random English greeting from the predefined list of greetings.
     *
     * @return a randomly selected English greeting as a String.
     */
    public static String getEnglishGreeting() {
        Random rand = new Random();
        return ENGLISH_GREETINGS[rand.nextInt(ENGLISH_GREETINGS.length)];
    }

    private static final String[] KOREAN_GREETINGS = {
            "게임에 오신 걸 환영해! 누가 먼저 카드를 다 없앨지 보자고!",
            "준비됐어? 제대로 플레이해야 이길 수 있을 거야!",
            "이 게임은 누가 더 빨리 카드를 버리느냐의 싸움이야. 넌 충분히 빠를까?",
            "나를 이길 실력이 있는지 한 번 볼까?",
            "운과 전략! 이 게임은 그 두 가지가 전부야. 시작해볼까?",
            "빠르게 플레이할수록 점수를 더 많이 얻는다고! 고민하지 마!",
            "긴장하지 마~ 하지만 난 이기려고 온 거야!",
            "오늘 운이 좋을까? 아니면 내 승리가 정해진 걸까?",
            "최고의 플레이어가 승리한다! 행운을 빌어… 정말 필요할 거야!",
            "준비됐지? 이거 엄청 재밌을 거야!"
    };

    /**
     * Retrieves a random Korean greeting from a predefined list of greetings.
     *
     * @return a randomly selected Korean greeting as a String.
     */
    public static String getKoreanGreeting() {
        Random rand = new Random();
        return KOREAN_GREETINGS[rand.nextInt(KOREAN_GREETINGS.length)];
    }

    private static final String[] ENGLISH_BAD_DRAW = {
            "Ugh… I really didn’t need this!",
            "Seriously? Another card?",
            "No! This makes my hand worse!",
            "Great… just what I didn’t want.",
            "This is not looking good for me...",
            "Come on, I needed to get rid of cards, not add more!",
            "I guess I have to change my strategy now.",
            "Well, that’s unfortunate…",
            "Why do I always get unlucky at the worst moments?",
            "This game just got harder for me!"
    };

    /**
     * Provides a randomly selected message from the predefined list of bad draw messages in English.
     *
     * @return a String containing a randomly chosen English bad draw message.
     */
    public static String getEnglishBadDraw() {
        Random rand = new Random();
        return ENGLISH_BAD_DRAW[rand.nextInt(ENGLISH_BAD_DRAW.length)];
    }

    private static final String[] KOREAN_BAD_DRAW = {
            "으으… 이건 정말 필요 없었는데!",
            "뭐야? 또 카드를 먹어야 한다고?",
            "아니! 내 손패가 더 나빠졌잖아!",
            "아... 최악이야. 이건 원하지 않았어.",
            "이제 상황이 점점 나빠지네...",
            "내 목표는 카드를 버리는 거지, 더 먹는 게 아니라고!",
            "이제 전략을 다시 생각해야겠어.",
            "운이 따라주질 않네…",
            "왜 중요한 순간마다 운이 이렇게 나쁜 거야?",
            "게임이 더 어려워졌어!"
    };

    /**
     * Retrieves a randomly selected message from a predefined list of bad draw messages in Korean.
     *
     * @return a String containing a randomly chosen Korean bad draw message.
     */
    public static String getKoreanBadDraw() {
        Random rand = new Random();
        return KOREAN_BAD_DRAW[rand.nextInt(KOREAN_BAD_DRAW.length)];
    }

    private static final String[] ENGLISH_ATTACK = {
            "Take this! Let’s see how you handle it!",
            "Boom! Enjoy those extra cards!",
            "I hope you like drawing cards, because you’re getting more!",
            "That’s gonna hurt! Have fun with that!",
            "This should slow you down a bit!",
            "Ouch! That’s not looking good for you!",
            "Now the real fun begins!",
            "You didn’t see that coming, did you?",
            "Let’s make things more interesting!",
            "That’s what happens when you challenge me!"
    };

    /**
     * Provides a randomly selected English attack message from a predefined list.
     *
     * @return a String containing a randomly chosen English attack message.
     */
    public static String getEnglishAttack() {
        Random rand = new Random();
        return ENGLISH_ATTACK[rand.nextInt(ENGLISH_ATTACK.length)];
    }

    private static final String[] KOREAN_ATTACK = {
            "받아라! 이걸 어떻게 막을 수 있을까?",
            "펑! 카드 더 가져가!",
            "카드 먹을 준비됐지? 더 줄게!",
            "이거 아플 텐데? 잘 버텨봐!",
            "이제 너 좀 느려지겠지?",
            "아야~ 너한테 안 좋은 소식이야!",
            "이제 진짜 재미있어지겠어!",
            "이건 예상 못 했지?",
            "게임을 더 흥미롭게 만들어볼까?",
            "나한테 덤비면 이렇게 되는 거야!"
    };

    /**
     * Provides a randomly selected Korean attack message from a predefined list of messages.
     *
     * @return a randomly chosen Korean attack message as a String.
     */
    public static String getKoreanAttack() {
        Random rand = new Random();
        return KOREAN_ATTACK[rand.nextInt(KOREAN_ATTACK.length)];
    }

    private static final String[] ENGLISH_FEW_CARDS_LEFT = {
            "Almost there… just a few more cards!",
            "I can see the finish line!",
            "Not many cards left now. Can you keep up?",
            "I'm so close to victory!",
            "Just one more move and I might win this!",
            "You better hurry, I'm running out of cards!",
            "Can you feel the pressure? I know I can!",
            "Getting nervous? You should be!",
            "I'm about to win! What’s your next move?",
            "One step closer to victory!"
    };

    /**
     * Retrieves a randomly selected message from the predefined list of messages
     * indicating there are only a few cards left, in English.
     *
     * @return a randomly chosen English message about having a few cards left.
     */
    public static String getEnglishFewCardsLeft() {
        Random rand = new Random();
        return ENGLISH_FEW_CARDS_LEFT[rand.nextInt(ENGLISH_FEW_CARDS_LEFT.length)];
    }

    private static final String[] KOREAN_FEW_CARDS_LEFT = {
            "거의 다 왔다... 이제 몇 장 남지 않았어!",
            "승리가 눈앞이야!",
            "이제 몇 장 안 남았는데, 따라올 수 있겠어?",
            "거의 이긴 거나 다름없지!",
            "한 수만 더 두면 이길지도 몰라!",
            "빨리 움직여! 내 카드가 얼마 안 남았어!",
            "긴장돼? 나도 좀 긴장되는데?",
            "불안하지? 그래야 정상이지!",
            "거의 이긴다! 다음 수를 잘 생각해!",
            "승리까지 한 걸음 남았어!"
    };

    /**
     * Retrieves a randomly selected message from a predefined list of messages
     * indicating there are only a few cards left, in Korean.
     *
     * @return a randomly chosen Korean message about having a few cards left as a String.
     */
    public static String getKoreanFewCardsLeft() {
        Random rand = new Random();
        return KOREAN_FEW_CARDS_LEFT[rand.nextInt(KOREAN_FEW_CARDS_LEFT.length)];
    }

    private static final String[] ENGLISH_TOO_MANY_CARDS = {
            "Ugh... My hand is way too full!",
            "I can barely hold all these cards!",
            "How did this happen?! My deck is overflowing!",
            "Too many cards... This isn't looking good!",
            "I need to start getting rid of these fast!",
            "This is bad... I’m carrying a mountain of cards!",
            "Well, this isn’t ideal…",
            "I swear my hand just keeps getting bigger!",
            "Looks like I’m in trouble now…",
            "At this rate, I’ll never win!"
    };

    /**
     * Retrieves a randomly selected message from a predefined list
     * of English messages indicating that too many cards are present.
     *
     * @return a randomly chosen English message about having too many cards.
     */
    public static String getEnglishTooManyCards() {
        Random rand = new Random();
        return ENGLISH_TOO_MANY_CARDS[rand.nextInt(ENGLISH_TOO_MANY_CARDS.length)];
    }

    private static final String[] KOREAN_TOO_MANY_CARDS = {
            "아... 내 손패가 너무 많아!",
            "카드를 다 들고 있을 수가 없잖아!",
            "이게 어떻게 된 거야?! 카드가 넘쳐흘러!",
            "카드가 너무 많아... 이거 큰일인데?",
            "빨리 줄여야겠어! 이렇게는 못 이겨!",
            "이건 심각한데... 카드 산을 들고 다니는 기분이야!",
            "이거 진짜 최악이다…",
            "내 손패가 끝없이 늘어나는 것 같아!",
            "이제 나 좀 곤란한 상황인데…",
            "이러다 진짜 끝까지 못 버리겠어!"
    };

    /**
     * Retrieves a randomly selected Korean message from a predefined list of messages
     * that indicate too many cards are present.
     *
     * @return a randomly selected message in Korean about having too many cards.
     */
    public static String getKoreanTooManyCards() {
        Random rand = new Random();
        return KOREAN_TOO_MANY_CARDS[rand.nextInt(KOREAN_TOO_MANY_CARDS.length)];
    }

    private static final String[] ENGLISH_CHAT_RESPONSES = {
            "Oh? You’re trying to distract me, aren’t you?",
            "Talking won’t help you win, you know!",
            "Less chatting, more playing!",
            "Are you nervous? You seem to be talking a lot!",
            "You think words will save you? Let’s see your cards!",
            "I’ll take that as a sign of desperation. 😏",
            "I see you like to chat. Let’s see if you like to win too!",
            "Focus! This is a serious game!",
            "Keep talking, I’ll keep winning!",
            "You’re fun to play with! But I’m still going to win!",
            "Trying to bluff me? Good luck with that!",
            "Keep chatting, maybe luck will come your way.",
            "I love a good conversation, but I love winning more!",
            "Haha, are you asking for mercy? Not happening!",
            "Your words are strong… but are your cards stronger?",
            "Oh, do you need some help? Should I go easy on you?",
            "Enough talk, let's get back to the game!",
            "You sound confident! Let’s see if your gameplay matches.",
            "Bluffing won't work on me, I read players like a book!",
            "I'll chat with you after I win this round!"
    };

    /**
     * Retrieves a randomly selected English chat response from a predefined list of responses.
     *
     * @return a randomly chosen English chat response as a String.
     */
    public static String getEnglishChatResponse() {
        Random rand = new Random();
        return ENGLISH_CHAT_RESPONSES[rand.nextInt(ENGLISH_CHAT_RESPONSES.length)];
    }
    
    private static final String[] KOREAN_CHAT_RESPONSES = {
            "오? 나를 방해하려는 수작인가?",
            "말한다고 게임에서 이길 순 없다고!",
            "말 줄이고 게임에 집중하는 게 어때?",
            "긴장했어? 말이 많아지는 걸 보니!",
            "말로 이길 생각? 카드로 보여줘!",
            "이거 패배의 향기가 나는데? 😏",
            "채팅 좋아하네? 게임도 그렇게 잘할까?",
            "집중해! 이건 진지한 승부야!",
            "계속 떠들어! 난 그동안 이길게!",
            "너랑 플레이하는 거 재밌네! 하지만 내가 이길 거야!",
            "설마 이게 네 전략이야? 방해 작전?",
            "계속 떠들면 운이 올지도 모르지!",
            "대화하는 거 좋은데, 난 이기는 게 더 좋아!",
            "혹시 봐달라는 거야? 절대 안 봐줄 거야!",
            "입으로는 이겼네? 이제 카드로 증명해봐!",
            "아, 도움 필요해? 내가 좀 봐줘야 할까?",
            "말은 그만하고, 본 게임에 집중하자!",
            "자신감 넘치네? 카드 실력도 그러길 바래!",
            "허세는 통하지 않아, 난 다 읽고 있다고!",
            "게임 끝나면 이야기하자! 일단 내가 이기고!"
    };

    /**
     * Retrieves a randomly selected Korean chat response from a predefined list of responses.
     *
     * @return a randomly chosen Korean chat response as a String.
     */
    public static String getKoreanChatResponse() {
        Random rand = new Random();
        return KOREAN_CHAT_RESPONSES[rand.nextInt(KOREAN_CHAT_RESPONSES.length)];
    }

}
