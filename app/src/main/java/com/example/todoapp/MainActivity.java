package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.adapters.TaskListAdapter;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.models.Task;
import com.example.todoapp.viewmodels.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private PopupWindow popupWindow;
    private FloatingActionButton createNewTaskBtn;
    private RecyclerView taskListView;
    private TaskViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_main);
        Task task = new Task("Hello world", false);
        binding.setTask(task);

        ConstraintLayout main = findViewById(R.id.main);

        LayoutInflater inflater = LayoutInflater.from(this);
        View popup = inflater.inflate(R.layout.create_task_popup, main, false);

        popupWindow = new PopupWindow(popup, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);

        Button submitBtn = popup.findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                createNewTaskBtn.setVisibility(View.VISIBLE);

                EditText editText = popup.findViewById(R.id.edit_task_name);
                String taskName = editText.getText().toString();
                editText.setText("");
                CheckBox isDoneCheckBox = popup.findViewById(R.id.edit_task_is_done);
                Boolean isDone = isDoneCheckBox.isChecked();
                isDoneCheckBox.setChecked(false);
                ArrayList<Task> old = model.getTasks().getValue();
                old.add(new Task(taskName, isDone));
                model.getTasks().postValue(old);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                createNewTaskBtn.setVisibility(View.VISIBLE);
            }
        });

        EditText findTask = findViewById(R.id.findTask);
        createNewTaskBtn = findViewById(R.id.createNewTask);
        taskListView = findViewById(R.id.task_list);

        TaskListAdapter adapter = new TaskListAdapter(this, popupWindow);
        taskListView.setAdapter(adapter);
        taskListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        model = new ViewModelProvider(this).get(TaskViewModel.class);
        model.getTasks().observe(this, tasks -> {
            ArrayList<Task> newTasks = (ArrayList<Task>) tasks.clone();
            newTasks.removeIf(task1 -> !task1.getName().contains(adapter.getFilter()));
            adapter.setTasks(newTasks);
        });

        createNewTaskBtn.setOnClickListener(this::onCreateButtonClicked);
        findTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filter = s.toString();
                adapter.setFilter(filter);
                ArrayList<Task> newTasks = (ArrayList<Task>) model.getTasks().getValue().clone();
                newTasks.removeIf(task1 -> !task1.getName().contains(filter));
                adapter.setTasks(newTasks);
                Toast.makeText(MainActivity.this, Integer.toString(model.getTasks().getValue().size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onCreateButtonClicked(View view) {
        createNewTaskBtn.setVisibility(View.INVISIBLE);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}