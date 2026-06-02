package chatapp;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // auto-flush

            // First message from client is their username
            clientName = in.readLine();
            System.out.println(clientName + " has joined.");
            ChatServer.broadcast(clientName + " has joined the chat!", this);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("[" + clientName + "]: " + message);
                // Broadcast to everyone else
                ChatServer.broadcast("[" + clientName + "]: " + message, this);
            }
        } catch (IOException e) {
            System.out.println(clientName + " disconnected.");
        } finally {
            ChatServer.clients.remove(this);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    public void sendMessage(String message) {
        if (out != null) out.println(message);
    }
}