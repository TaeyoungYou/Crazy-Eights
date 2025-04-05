package app.model.multi;

import java.util.List;

public class GameDTO {

    public static void initDeck(String data, Deck deck){
        String[] cards = data.split(" ");
        for(String set : cards){
            int suit = Integer.parseInt(set.split(":")[0]);
            int rank = Integer.parseInt(set.split(":")[1]);
            Card card = new Card(suit, rank);
            deck.add(card);
        }
    }

    public static void initPlayers(String data, List<Player> players, List<Player> users){
        String[] player = data.split(" ");
        for(String p : player){
            int id = Integer.parseInt(p.split(":")[0]);
            String icon = p.split(":")[1];

            Player temp = new Player(id);
            temp.setPlayer(true);
            temp.setIcon(icon);
            players.add(temp);
            users.add(temp);
        }
    }

    public static void createPlayers(String data, List<Player> players){
        String[] player = data.split(" ");
        for(String p : player){
            int id = Integer.parseInt(p.split(":")[0]);
            String icon = p.split(":")[1];
            boolean exists = false;
            for(Player pl: players){
                if(pl.getNetworkId() == id){
                    exists = true;
                    break;
                }
            }
            if(!exists){
                Player temp = new Player(id);
                temp.setPlayer(false);
                temp.setIcon(icon);
                players.add(temp);
            }
        }
    }
}
