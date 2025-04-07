package app.model.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;

    private Player player;

    private volatile boolean running = true;
    private volatile boolean closed = false;

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
        try {
            String input;
            while (running && (input = in.readLine()) != null) {
                server.broadcast(input, this);
            }
            System.out.println("[SERVER] ClientHandler " + player.getNetworkId() + " loop exited normally");
        } catch (SocketException e) {
            System.out.println("[SERVER] 클라이언트 " + player.getNetworkId() + " 연결 끊김 (reset)");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[SERVER] 클라이언트 " + player.getNetworkId() + " 연결 종료 처리");
            if(!closed) close();
        }
    }

    // 무조건 ClientHandler와 연결된 Client에게만 전송
    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void close() {
        System.out.println("[SERVER] ClientHandler " + player.getNetworkId() + " close()");
        running = false;
        closed = true;
        try {
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("[SERVER] ClientHandler " + player.getNetworkId() + " socket closed");
            if (in != null) in.close();
            System.out.println("[SERVER] ClientHandler " + player.getNetworkId() + " in closed");
            if (out != null) out.close();
            System.out.println("[SERVER] ClientHandler " + player.getNetworkId() + " out closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Player getPlayer() {
        return player;
    }
}
