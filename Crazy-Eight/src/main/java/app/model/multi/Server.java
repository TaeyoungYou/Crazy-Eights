package app.model.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Server class handles the management of a server socket to listen for incoming
 * client connections, manage connected clients, and broadcast messages between them.
 * It is a singleton class ensuring that only one server instance exists.
 * <p>
 * Features:
 * - Initializes a server socket on a given port.
 * - Accepts and manages up to a limited number of client connections.
 * - Handles broadcasting messages to all connected clients.
 * - Provides functionality to start and stop the server safely.
 */
public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private final static List<ClientHandler> clients = new ArrayList<>();

    private Player player;
    private volatile boolean stopped = false;
    private Thread acceptThread;

    /**
     * Constructs a Server instance that initializes a server socket and starts accepting clients.
     *
     * @param port The port number on which the server will listen for incoming connections.
     * @throws IOException If an I/O error occurs while opening the server socket.
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.player = new Player(0);

        System.out.printf("%d#%s#%s\n", player.getNetworkId(), MsgType.CREATE_GAME.toString(), port);
        acceptThread = new Thread(this::acceptClients);
        acceptThread.setDaemon(true);
        acceptThread.start();

        Client.connect("localhost", port);
    }

    /**
     * Starts the server by creating a new instance of the Server class that binds
     * to the specified port. If an I/O error occurs during the server startup, an
     * error message is logged to the standard output.
     *
     * @param port The port number on which the server will listen for incoming connections.
     */
    public static void start(int port) {
        try {
            instance = new Server(port);

        } catch (IOException e) {
            System.out.println("Fail to start server: " + e.getMessage());
        }
    }

    /**
     * Accepts incoming client connections to the server until the server is stopped,
     * a limit on the number of connected clients is reached, or the server socket is closed.
     * <p>
     * This method listens for client connections on the server socket and creates a new
     * {@code ClientHandler} instance for each successful connection. Each client is handled
     * in a separate thread to allow concurrent communication. The method ensures proper
     * resource cleanup and safe termination of connections when the server is stopped.
     * <p>
     * Behavior:
     * - The method runs in a loop, accepting new client connections as long as the following
     * conditions are met:
     * 1. The server is not stopped.
     * 2. The maximum limit of allowed clients (3) is not reached.
     * 3. The server socket is open and not closed.
     * - When a client connects:
     * 1. A {@code ClientHandler} instance is created for the client.
     * 2. The client handler is added to the list of active clients.
     * 3. The handler is started in a new thread for communication management with the client.
     * 4. Log messages are printed to indicate the connection of the client and its network ID.
     * - If the server is stopped while this method is running, it closes the client socket
     * for any ongoing connection attempt and terminates the loop.
     * - Logs are produced to indicate the lifecycle of the method and any exceptions that occur.
     * <p>
     * Error Handling:
     * - Catches and handles {@code SocketException} to manage situations where the server is stopped
     * or the socket is closed during an `accept` operation.
     * - Gracefully exits the loop and logs the appropriate messages in case of such exceptions.
     * - Catches general {@code IOException} and logs the exception message for debugging.
     * <p>
     * Exit Conditions:
     * - The method stops execution when any of the following occurs:
     * 1. The `stopped` flag is set to {@code true}.
     * 2. The maximum client limit (3) is reached.
     * 3. The server socket is closed.
     * 4. An {@code IOException} occurs and is not recoverable.
     */
    private void acceptClients() {
        try {
            while (!stopped && clients.size() < 3 && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    if (stopped) {
                        clientSocket.close();
                        break;
                    }
                    ClientHandler handler = new ClientHandler(clientSocket, this, clients.size());
                    clients.add(handler);
                    new Thread(handler).start();
                    System.out.println("[SERVER] Player " + handler.getPlayer().getNetworkId() + " connected" + clientSocket.getRemoteSocketAddress());
                } catch (SocketException e) {
                    if (stopped) {
                        System.out.println("[SERVER] Server socket closed, accept loop exiting.");
                        break;
                    } else throw e;
                }

            }
            System.out.println("[SERVER] acceptClients exit");
        } catch (IOException e) {
            System.out.println("[SERVER] acceptClients exit " + e.getMessage());
        }
    }

    /**
     * Sends a broadcast message to all connected clients.
     *
     * @param msg    The message to be broadcasted to all clients.
     * @param sender The client handler instance that is the source of the broadcasted message.
     */
    public static void broadcast(String msg, ClientHandler sender) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    /**
     * Stops the server and releases all associated resources.
     * <p>
     * This method ensures that the server is stopped gracefully by performing the following steps:
     * 1. Checks if the server is already stopped. If so, the method returns immediately.
     * 2. Logs the attempt to stop the server.
     * 3. Marks the server as stopped by setting the `stopped` flag to true.
     * 4. Closes and clears all connected client handlers in a thread-safe manner.
     * 5. Closes the server socket if it is open, ensuring resources are released properly.
     * 6. Waits for the `acceptThread` to terminate if it is still alive.
     * 7. Logs the completion of the server shutdown process.
     * <p>
     * Exceptions that occur during the shutdown process are caught and logged to aid in debugging.
     */
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

    /**
     * Removes the specified client from the list of clients.
     *
     * @param client the ClientHandler instance to be removed from the list of clients
     */
    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    /**
     * Retrieves the singleton instance of the Server class.
     * <p>
     * This method ensures a single, globally accessible instance of the Server
     * is returned. If the instance does not already exist, it must be initialized
     * elsewhere in the application.
     *
     * @return the singleton instance of the Server
     */
    public static Server getInstance() {
        return instance;
    }

    /**
     * Retrieves the server socket associated with the server instance.
     *
     * @return the {@code ServerSocket} instance used by the server to listen for incoming connections.
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
