package app.model.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private final static List<ClientHandler> clients = new ArrayList<>();

    private Player player;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.player = new Player(0);

        System.out.printf("%d#%s#%s\n", player.getNetworkId(),MsgType.CREATE_GAME.toString(),port);
        new Thread(this::acceptClients).start();

        Client.connect("localhost", port);
    }


    public static void start(int port){
        try{
            instance = new Server(port);

        } catch(IOException e){
            System.out.println("Fail to start server: " + e.getMessage());
        }
    }

    private void acceptClients() {
        try{
            while(clients.size() < 3){
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this, clients.size());
                clients.add(handler);
                new Thread(handler).start();
                System.out.println("[SERVER] Player " + handler.getPlayer().getNetworkId() + " connected" + clientSocket.getRemoteSocketAddress());
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void broadcast(String msg, ClientHandler sender){
        for(ClientHandler client : clients){
                client.sendMessage(msg);
        }
    }

    public void stop() throws IOException {
        for(ClientHandler client : clients) client.close();
        serverSocket.close();
    }

    public static Server getInstance() {
        return instance;
    }
}
