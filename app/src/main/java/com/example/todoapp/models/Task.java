package com.example.todoapp.models;

public class Task {
    private String name;
    private Boolean isDone;

    public Task() {
        name = "";
        isDone = false;
    }

    public Task(String name, Boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }
}
