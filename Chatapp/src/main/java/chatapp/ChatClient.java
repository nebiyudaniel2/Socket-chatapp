package chatapp;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageListener listener;

    // Callback interface — ChatUI implements this to receive messages
    public interface MessageListener {
        void onMessageReceived(String message);
    }

    public void connect(String host, int port, String username, MessageListener listener) throws IOException {
        this.listener = listener;
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Send username as first message
        out.println(username);

        // Background thread listens for incoming messages
        Thread readerThread = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    final String msg = message;
                    // Notify the UI (must be called on JavaFX thread — handled in ChatUI)
                    listener.onMessageReceived(msg);
                }
            } catch (IOException e) {
                listener.onMessageReceived("** Disconnected from server **");
            }
        });
        readerThread.setDaemon(true); // dies when the app closes
        readerThread.start();
    }

    public void sendMessage(String message) {
        if (out != null) out.println(message);
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
    }
}