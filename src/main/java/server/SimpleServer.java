package server;

import com.google.gson.Gson;
import searchEngine.BooleanSearchEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class SimpleServer {
    private static final Charset SOCKET_CHARSET = Charset.forName("cp866");
    private static final File DIR = new File("pdfs");
    public static final int PORT = 8989;

    private final BooleanSearchEngine engine;

    public SimpleServer() {
        this.engine = new BooleanSearchEngine(DIR);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер успешно запущен!");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Установлено соединение: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                    try (Scanner incomingMessage = new Scanner(socket.getInputStream(), SOCKET_CHARSET);
                         PrintWriter outgoingMessage = new PrintWriter(socket.getOutputStream(), true, SOCKET_CHARSET)) {
                        outgoingMessage.println("Пожалуйста, введите ниже слово для поиска: ");
                        outgoingMessage.println(toJson(incomingMessage.nextLine().strip()));
                        outgoingMessage.println("Завершение сеанса");
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка установки соединения!");
                }
            }
        } catch (IOException e) {
            System.err.println("Не удалось запустить сервер!");
        }
    }

    private String toJson(String word) {
        return new Gson().toJson(engine.search(word));
    }

    public static void main(String[] args) {
        new SimpleServer();
    }
}
