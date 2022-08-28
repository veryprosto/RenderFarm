package ru.veryprosto.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.veryprosto.model.Task;

import java.sql.SQLException;

public class TaskDao extends BaseDaoImpl<Task, Integer> {

    public TaskDao(ConnectionSource connectionSource, Class<Task> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}

