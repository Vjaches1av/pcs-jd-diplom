package client;

import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static final Charset SOCKET_CHARSET = Charset.forName("cp866");
    public static final String LOCALHOST = "localhost";
    public static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket(LOCALHOST, PORT);
             Scanner incomingMessage = new Scanner(socket.getInputStream(), SOCKET_CHARSET);
             PrintWriter outgoingMessage = new PrintWriter(socket.getOutputStream(), true, SOCKET_CHARSET);
             Scanner userInput = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.println("Установлено соединение: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

            System.out.println(incomingMessage.nextLine());
            outgoingMessage.println(userInput.nextLine());
            System.out.println(incomingMessage.nextLine());
            System.out.println(incomingMessage.nextLine());
        }
    }
}
