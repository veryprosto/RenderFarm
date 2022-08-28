package ru.veryprosto.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Task {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String status;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoCreate = true)
    private User user;

    @DatabaseField(canBeNull = false)
    private Date taskCreateTime;

    public Task() {
    }

    public Task(String name, User user) {
        this.name = name;
        this.status = "RENDERING";
        this.taskCreateTime = new Date();
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}

