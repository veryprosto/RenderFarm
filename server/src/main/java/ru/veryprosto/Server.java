package ru.veryprosto;

import ru.veryprosto.controller.Controller;
import ru.veryprosto.model.Task;
import ru.veryprosto.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server implements TCPConnectionListener {
    private final List<TCPConnection> connections = new ArrayList<>();
    private Controller controller;

    Server() {
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                    controller = new Controller();
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                } catch (SQLException e) {
                    System.out.println("SQL exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        handler(tcpConnection, "");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        handler(tcpConnection, value);
    }

    @Override
    public void onDisconnct(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    void sendToConnection(TCPConnection tcpConnection, String value) {
        tcpConnection.sendString(value);
    }

    private void handler(TCPConnection tcpConnection, String value) {
        String userName = tcpConnection.getUserName();
        User user = controller != null ? controller.getUserByName(userName) : null;
        String response = "";
        switch (tcpConnection.getDialogStatus()) {
            case CONNECT:
                response = "Выберите действие - введите соответствующее число:\n" +
                        "1.\tАвторизация - ввод login(Если ранее была регистрация).\n" +
                        "2.\tРегистрация нового пользователя.\n" +
                        "0.\tВыйти из программы.\n";
                tcpConnection.setDialogStatus(DialogStatus.LOGIN);
                break;
            case LOGIN:
                switch (value) {
                    case ("1"):
                        response = "Авторизация\n" +
                                "Введите login: ";
                        tcpConnection.setDialogStatus(DialogStatus.ENTERED_LOGIN);
                        break;
                    case ("2"):
                        response = "Регистрация\n" +
                                "Введите login: ";
                        tcpConnection.setDialogStatus(DialogStatus.REGISTRATION);
                }
                break;
            case ENTERED_LOGIN:
                if (checkLogin(value)) {
                    if (tcpConnection.getUserName().equals("")) tcpConnection.setUserName(value);
                    tcpConnection.setDialogStatus(DialogStatus.TASK);
                    response = "С возвращением " + tcpConnection.getUserName() + "!\n" +
                            "Выберите действие\n" +
                            "1.\tСоздание новой задачи.\n" +
                            "2.\tПоказать список задач.\n" +
                            "0.\tВыйти из программы.\n";
                } else {
                    response = "Такой логин не зарегистрирован, для продолжения нажмите ввод.";
                    tcpConnection.setDialogStatus(DialogStatus.CONNECT);
                }
                break;
            case REGISTRATION:
                if (registration(value))
                    response = "Логин " + value + " успешно зарегистрирован! Для продолжения нажмите ввод.\n";
                else
                    response = "Такой логин уже зарегистрирован, придумайте другой логин. Для продолжения нажмите ввод.";

                tcpConnection.setDialogStatus(DialogStatus.CONNECT);
                break;
            case TASK:
                switch (value) {
                    case ("1"):
                        response = "Создание новой задачи.:\n" +
                                "Введите название:";
                        tcpConnection.setDialogStatus(DialogStatus.CREATE_TASK);

                        break;
                    case ("2"):
                        List<Task> tasksByUser = controller.getTasksByUser(user);
                        StringBuffer sb = new StringBuffer();
                        tasksByUser.forEach(sb::append);
                        response = sb.toString();
                }
                break;
            case CREATE_TASK:
                if (user != null && createTask(value, user))
                    response = "Задача " + value + " успешно создана! Для продолжения нажмите ввод.";
                else
                    response = "Возникли проблемы с созданием задача с названием " + value + ". Попробуйте придумать другое название! Для продолжения нажмите ввод.";
                tcpConnection.setDialogStatus(DialogStatus.ENTERED_LOGIN);
        }
        tcpConnection.sendString(response);
    }

    private boolean createTask(String taskName, User user) {
        return controller.createTask(new Task(taskName, user));
    }

    private boolean registration(String login) {
        return controller.createUser(new User(login));
    }

    private boolean checkLogin(String login) {
        List<User> users = controller.getUsers();
        long count = users.stream().filter(user -> user.getName().equals(login)).count();
        return count > 0;
    }
}
