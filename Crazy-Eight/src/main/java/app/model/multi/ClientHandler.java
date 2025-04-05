package app.model.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;

    private Player player;

    public ClientHandler(Socket socket, Server server, int index) throws IOException {
        this.socket = socket;
        this.server = server;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.player = new Player(index);
        sendMessage(player.getNetworkId() + "#ASSIGN_ID#character");
    }

    // Client > Server 메세지 읽고, board casting
    @Override
    public void run() {
        try{
            String input;
            while((input = in.readLine()) != null){
                server.broadcast(input, this);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            close();
        }
    }
    // 무조건 ClientHandler와 연결된 Client에게만 전송
    public void sendMessage(String msg){
        out.println(msg);
    }

    public void close(){
        try{
            if(in != null) in.close();
            if(out != null) out.close();
            if(socket != null) socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }
}
