package net.server;

import searchEngine.BooleanSearchEngine;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.print("Укажите путь к папке с pdf-файлами: ");
            File dir = new File(scanner.nextLine());
            if (dir.exists() && dir.isDirectory()) {
                new SimpleServer(new BooleanSearchEngine(dir));
            } else {
                System.err.println("Указанная папка не существует или недоступна");
            }
        }
    }
}
