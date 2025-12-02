package app.model.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The Client class provides the functionality to establish a connection to a server,
 * send and receive messages, and handle incoming messages through user-defined handlers.
 */
public class Client {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static volatile boolean isConnected = false;

    private static volatile boolean running = true;
    private static Thread listenThread;

    private static volatile MessageHandler currentHandler;

    /**
     * Establishes a connection to a server at the specified IP address and port.
     * Initializes input and output streams for communication with the server.
     * Sets the connection state to active.
     *
     * @param ip   the IP address of the server to connect to
     * @param port the port number of the server to connect to
     * @throws IOException if an I/O error occurs when attempting to connect
     */
    public static void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        isConnected = true;
    }

    /**
     * Reads a single line of text from the input stream if a connection is established.
     *
     * @return the line of text read from the input stream
     * @throws IOException if not connected or an I/O error occurs during reading
     */
    private static String read() throws IOException {
        if (in == null) throw new IOException("Not connected");
        return in.readLine();
    }

    /**
     * Sends the specified message to the connected server through the output stream.
     * If the client is not connected, an error message is printed to the standard error stream.
     *
     * @param msg the message to be sent to the server
     */
    public static void send(String msg) {
        if (out == null) System.err.println("Not connected");
        else {
            out.write(msg + "\n");
            out.flush();
        }
    }

    /**
     * Sets the current message handler to handle incoming messages from the server.
     * The provided handler will define the behavior for processing these messages.
     *
     * @param handler the MessageHandler instance to set as the current message handler
     */
    public static void setHandler(MessageHandler handler) {
        currentHandler = handler;
    }

    /**
     * Listens for messages from the server and processes them continuously
     * in a separate thread. This method is intended to handle incoming
     * server-to-client communication.
     * <p>
     * The method creates and starts a daemon thread that reads messages
     * from the input stream using the {@code read()} method. Each message
     * is passed to the current handler, defined by {@code currentHandler},
     * for processing.
     * <p>
     * If the connection is not established (i.e., {@code isConnected} is false),
     * the thread exits immediately. If an I/O error occurs during message
     * listening, the exception stack trace is printed.
     * <p>
     * This method is crucial for maintaining real-time communication
     * between the client and the server.
     *
     * @throws IOException if an I/O error occurs while setting up the listening thread
     */
    // Server > Client로 오는 메세지 계속 listen
    public static void listen() throws IOException {
        listenThread = new Thread(() -> {
            if (!isConnected) return;
            try {
                String line;
                while ((line = read()) != null) {
                    if (currentHandler != null)
                        currentHandler.handle(line);   // controller에서 구현을 해야함. 여기서 어떤 모델의 값이 바뀌었는지 알수잇음
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenThread.setDaemon(true);
        listenThread.start();
    }

    /**
     * Closes all active resources associated with the client, including input and output streams,
     * the socket, and the listening thread. This method ensures that the client connection is properly
     * terminated and any managed threads are gracefully stopped.
     * <p>
     * The following operations are performed during the closing process:
     * 1. Sets the client's connection state to inactive by updating the {@code isConnected} flag.
     * 2. Closes the input stream, output stream, and socket, if they were initialized.
     * 3. Attempts to stop the listening thread. If the thread doesn't terminate within the specified
     * timeout (1000 ms), it logs a message and interrupts the thread.
     * <p>
     * Any exceptions that occur during the cleanup process are caught and their stack traces are
     * printed to assist with debugging.
     */
    public static void close() {
        try {
            isConnected = false;
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            if (listenThread != null && listenThread.isAlive()) {
                listenThread.join(1000);
                if (listenThread.isAlive()) {
                    System.err.println("listenThread is alive");
                    listenThread.interrupt();
                }
            }
            System.out.println("Client closed");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

