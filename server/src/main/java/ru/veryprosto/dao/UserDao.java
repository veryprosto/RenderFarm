package ru.veryprosto.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.veryprosto.model.User;

import java.sql.SQLException;

public class UserDao extends BaseDaoImpl<User, Integer> {

    public UserDao(ConnectionSource connectionSource, Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }


}