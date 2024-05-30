package org.yakupsayin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Communications {

    /**
     * It reads the message coming to InputStream and converts it to String.
     * @param is Socket's inputStream.
     * @return Message that came.
     */
    public static String readMessage(InputStream is){
        try {
            // We allocate an array of 1024 bytes.
            byte[] messageByte = new byte[8192];

            // We get the number of bytes read by InputStream.
            int bytesRead = is.read(messageByte);

            /*
                Using the byte array from messageByte,
                we create a String with as many bytes as the number of bytes read.
                For multi-language support, we convert to UTF-8
                (maybe the user's name contains non-Standard ASCII characters).
            */
            return new String(messageByte, 0, bytesRead, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.out.println("An error was encountered while trying to read a message: " + e.getMessage());
            return null;
        }
    }

    /**
     * It sends the given String expression to the socket(s) it addresses.
     * @param os Socket's outputStream.
     * @param message The message to be sent to the socket.
     */
    public static void writeMessage(OutputStream os, String message){
        try {
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            os.write(messageByte);
            os.flush();
        } catch (IOException e) {
            System.out.println("An error was encountered while trying to write a message: " + e.getMessage());
        }
    }
}
