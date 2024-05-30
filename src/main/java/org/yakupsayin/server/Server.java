package org.yakupsayin.server;

import org.yakupsayin.server.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server{
    /*
        Since we are working with multiple threads,
        we have to make sure that when a thread updates the ArrayList,
        it is updated.

        That's why we use CopyOnWriteArrayList!
     */
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static final int PORT = 9000; // It may be modified to be retrieved from args in the future

    public static void main(String[] args) {
        // Create a server socket with "try with resources"
        try (ServerSocket listener = new ServerSocket(PORT)){

            // Unless the Server Socket itself is closed
            while (true){
                System.out.println("Server waiting client...");

                // Accept incoming requests
                Socket clientSocket = listener.accept();

                System.out.println("New client connected!");

                // Assign a new handler to handle the new client's requests.
                ClientHandler newClient = new ClientHandler(clients, clientSocket);

                // Since we will have more than one client, we assign a dedicated thread to them.
                Thread clientThread = new Thread(newClient);
                clientThread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}