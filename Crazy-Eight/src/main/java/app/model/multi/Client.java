package app.model.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static volatile boolean isConnected = false;

    private static volatile MessageHandler currentHandler;

    public static void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        isConnected = true;
    }

    private static String read() throws IOException {
        if(in == null) throw new IOException("Not connected");
        return in.readLine();
    }

    public static void send(String msg){
        if(out == null) System.err.println("Not connected");
        out.write(msg + "\n");
        out.flush();
    }

    public static void setHandler(MessageHandler handler){
        currentHandler = handler;
    }

    // Server > Client로 오는 메세지 계속 listen
    public static void listen() throws IOException {
        new Thread(()->{
            if(!isConnected) return;
            try{
                String line;
                while((line = read()) != null){
                    if(currentHandler != null) currentHandler.handle(line);   // controller에서 구현을 해야함. 여기서 어떤 모델의 값이 바뀌었는지 알수잇음
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    public static void close() throws IOException {
        isConnected = false;
        if(in != null) in.close();
        if(out != null) out.close();
        if(socket != null) socket.close();
    }
}

