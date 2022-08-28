package ru.veryprosto.controller;

import ru.veryprosto.dao.TaskDao;
import ru.veryprosto.dao.UserDao;
import ru.veryprosto.model.Task;
import ru.veryprosto.model.User;
import ru.veryprosto.service.Init;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Controller {
    UserDao userDao = new UserDao(Init.getConnectionSource(), User.class);
    TaskDao taskDao = new TaskDao(Init.getConnectionSource(), Task.class);

    public Controller() throws SQLException {
    }

    public boolean createUser(User user) {
        try {
            userDao.create(user);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean createTask(Task task) {
        try {
            taskDao.create(task);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public List<User> getUsers() {
        try {
            return userDao.queryForAll();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
        return Collections.emptyList();
    }

    public User getUserByName(String name) {
        try {
            return userDao.queryBuilder().where().eq("name", name).queryForFirst();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
        return null;
    }

    public List<Task> getTasksByUser(User user) {
        try {
            return taskDao.queryBuilder().where().eq("user_id", user.getId()).query();
        } catch (SQLException e) {
            System.out.println("Ошибка при запросе в БД");
        }
        return Collections.emptyList();
    }
}
