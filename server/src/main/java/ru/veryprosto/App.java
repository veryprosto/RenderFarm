package ru.veryprosto;

import ru.veryprosto.service.Init;

public class App {
    public static void main(String[] args) {
        Init.init();
        new Server();
    }
}

