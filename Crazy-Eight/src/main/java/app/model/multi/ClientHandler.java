package app.model.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


/**
 * The ClientHandler class is responsible for managing the connection between the server
 * and an individual client. It handles receiving and sending messages to the connected
 * client, as well as managing client-specific attributes such as its Player object.
 * This class implements Runnable and is intended to be run on its own thread.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;

    private Player player;

    private volatile boolean running = true;
    private volatile boolean closed = false;

    /**
     * Constructs a new ClientHandler for managing the connection between the server
     * and an individual client. Initializes input and output streams for communication
     * with the client and assigns a unique identifier to the client's Player object.
     *
     * @param socket the client socket used for communication
     * @param server the server instance managing this ClientHandler
     * @param index  the unique index assigned to this client, used to create the Player object
     * @throws IOException if an I/O error occurs when initializing input and output streams
     */
    public ClientHandler(Socket socket, Server server, int index) throws IOException {
        this.socket = socket;
        this.server = server;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.player = new Player(index);
        sendMessage(player.getNetworkId() + "#ASSIGN_ID#character");
    }

    /**
     * Continuously listens for incoming messages from the associated client,
     * processes them, and broadcasts the messages to all connected clients
     * using the server's broadcasting functionality. Handles exceptions related
     * to socket and input/output operations, ensuring proper closure of resources
     * and cleanup during termination.
     * <p>
     * This method is executed in a separate thread for each client, enabling
     * concurrent handling of multiple client connections.
     * <p>
     * Behavior:
     * - Reads messages sent by the client through the input stream.
     * - Forwards each received message to the server for broadcasting to other clients.
     * - Handles socket disconnection and I/O errors gracefully.
     * - Ensures proper resource deallocation (e.g., closing streams and sockets).
     * <p>
     * Exceptions:
     * - Catches and logs `SocketException` when the client disconnects unexpectedly.
     * - Catches and logs `IOException` for other I/O related issues.
     * - Ensures proper cleanup via the `close` method in the `finally` block.
     */
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
            if (!closed) close();
        }
    }

    /**
     * Sends a message to the client connected to this `ClientHandler` instance.
     *
     * @param msg the message to be sent to the client
     */
    // 무조건 ClientHandler와 연결된 Client에게만 전송
    public void sendMessage(String msg) {
        out.println(msg);
    }

    /**
     * Releases resources associated with this `ClientHandler` instance and
     * performs cleanup operations, including closing the client socket
     * and terminating input/output streams. This method ensures the proper
     * shutdown of the handler, marking it as closed.
     * <p>
     * Behavior:
     * - Terminates the main client handling loop by setting the `running` flag to `false`.
     * - Marks the handler as closed by setting the `closed` flag to `true`.
     * - Safely closes the client socket, input stream, and output stream if they are
     * not null or already closed.
     * - Outputs status messages to indicate the progress of resource deallocation.
     * - Logs any `IOException` encountered during the cleanup process, ensuring
     * that errors do not interrupt the cleanup procedure.
     * <p>
     * Thread Safety:
     * - This method should be invoked in a controlled manner to avoid race conditions,
     * especially when accessed by multiple threads.
     * <p>
     * Exceptions:
     * - Prints stack trace of any `IOException` encountered during the socket or
     * stream closure operations.
     */
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


    /**
     * Retrieves the Player object associated with this ClientHandler instance.
     *
     * @return the Player object managed by this ClientHandler
     */
    public Player getPlayer() {
        return player;
    }
}
