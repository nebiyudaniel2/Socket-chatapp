package chatapp;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 5000;
    // Shared list of all connected client handlers
    public static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Server started on port " + PORT);
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket socket = serverSocket.accept(); // blocks until a client connects
            System.out.println("New client connected: " + socket.getInetAddress());

            ClientHandler handler = new ClientHandler(socket);
            clients.add(handler);
            new Thread(handler).start(); // each client runs on its own thread
        }
    }

    // Called by a ClientHandler to send a message to ALL clients
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) { // don't echo back to sender
                client.sendMessage(message);
            }
        }
    }
}