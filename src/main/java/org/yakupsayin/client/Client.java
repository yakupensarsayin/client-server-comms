package org.yakupsayin.client;

import com.squareup.moshi.Moshi;
import org.yakupsayin.Communications;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client extends Thread{
    public static void main(String[] args) throws IOException {
        System.out.print("Select username: ");

        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", 9000);

        Moshi moshi = new Moshi.Builder().build();

        JoinGameRequest gameRequest = new JoinGameRequest(username);

        String json = moshi.adapter(JoinGameRequest.class).toJson(gameRequest);

        Communications.writeMessage(socket.getOutputStream(), json);
    }
}