package ru.veryprosto.service;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.veryprosto.model.Task;
import ru.veryprosto.model.User;

import java.sql.SQLException;

public class Init {
    private static ConnectionSource connectionSource;

    static {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:renderfarm.db");
        } catch (SQLException throwables) {
            System.out.println("Ошибка при создании БД");
        }
    }

    public static void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Task.class);
        } catch (SQLException throwables) {
            System.out.println("Ошибка при создании таблиц");
        }
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
