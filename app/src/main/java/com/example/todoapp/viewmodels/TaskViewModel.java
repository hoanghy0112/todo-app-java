package com.example.todoapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoapp.models.Task;

import java.util.ArrayList;

public class TaskViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>(new ArrayList<Task>());

    public MutableLiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }
}
