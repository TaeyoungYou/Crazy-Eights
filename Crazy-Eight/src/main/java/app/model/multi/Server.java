package app.model.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private final static List<ClientHandler> clients = new ArrayList<>();

    private Player player;
    private volatile boolean stopped = false;
    private Thread acceptThread;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.player = new Player(0);

        System.out.printf("%d#%s#%s\n", player.getNetworkId(),MsgType.CREATE_GAME.toString(),port);
        acceptThread = new  Thread(this::acceptClients);
        acceptThread.setDaemon(true);
        acceptThread.start();

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
            while(!stopped && clients.size() < 3 && !serverSocket.isClosed()){
                try{
                    Socket clientSocket = serverSocket.accept();
                    if(stopped) {
                        clientSocket.close();
                        break;
                    }
                    ClientHandler handler = new ClientHandler(clientSocket, this, clients.size());
                    clients.add(handler);
                    new Thread(handler).start();
                    System.out.println("[SERVER] Player " + handler.getPlayer().getNetworkId() + " connected" + clientSocket.getRemoteSocketAddress());
                }catch(SocketException e){
                    if(stopped) {
                        System.out.println("[SERVER] Server socket closed, accept loop exiting.");
                        break;
                    }else throw e;
                }

            }
            System.out.println("[SERVER] acceptClients exit");
        } catch(IOException e){
            System.out.println("[SERVER] acceptClients exit "+e.getMessage());
        }
    }

    public static void broadcast(String msg, ClientHandler sender){
        for(ClientHandler client : clients){
                client.sendMessage(msg);
        }
    }

    public void stop() {
        if (stopped) return;
        System.out.println("[SERVER] TRY TO STOP SERVER");
        stopped = true;

        synchronized (clients) { // 수정: 동기화 추가
            for (ClientHandler client : clients) {
                System.out.println("[SERVER] ClientHandler closed: " + client.getPlayer().getNetworkId());
                client.close();
            }
            clients.clear();
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // 소켓 닫기
                System.out.println("[SERVER] ServerSocket closed"); // 추가: 디버깅 로그
            }
            if (acceptThread != null && acceptThread.isAlive()) {
                acceptThread.join(); // 수정: 스레드 종료 대기
                System.out.println("[SERVER] acceptThread joined"); // 추가: 디버깅 로그
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] Server stopped"); // 추가: 종료 완료 로그
    }

    public static void removeClient(ClientHandler client){
        clients.remove(client);
    }

    public static Server getInstance() {
        return instance;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
