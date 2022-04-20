package net.server;

import com.google.gson.Gson;
import searchEngine.PageEntry;
import searchEngine.SearchEngine;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class SimpleServer {
    public static final int PORT = 8989;
    public static final Charset SOCKET_CHARSET = Charset.forName("cp866");

    private final SearchEngine engine;

    public SimpleServer(SearchEngine engine) {
        this.engine = engine;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер успешно запущен!");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Установлено соединение: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                    try (Scanner incomingMessage = new Scanner(socket.getInputStream(), SOCKET_CHARSET);
                         PrintWriter outgoingMessage = new PrintWriter(socket.getOutputStream(), true, SOCKET_CHARSET)) {
                        outgoingMessage.println("Пожалуйста, введите ниже слово для поиска: ");
                        List<PageEntry> pageEntryList = engine.search(incomingMessage.nextLine().strip());
                        if (pageEntryList == null || pageEntryList.isEmpty()) {
                            outgoingMessage.println("По вашему запросу ничего не найдено");
                        } else {
                            outgoingMessage.println(toJson(pageEntryList));
                        }
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


    private String toJson(List<PageEntry> pageEntryList) {
        return new Gson().toJson(pageEntryList);
    }
}
