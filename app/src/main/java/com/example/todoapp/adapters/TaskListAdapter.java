package com.example.todoapp.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.models.Task;
import com.example.todoapp.viewmodels.TaskViewModel;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    Context context;
    PopupWindow popupWindow;
    private ArrayList<Task> tasks;
    private String filter = "";

    public void setFilter(String filter) {
        this.filter = filter;
        notifyDataSetChanged();
    }

    public TaskListAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public String getFilter() {
        return filter;
    }

    public TaskListAdapter(Context context, PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
        this.context = context;
        tasks = new ArrayList<Task>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTaskName().setText(tasks.get(position).getName());
        holder.getIsDone().setChecked(tasks.get(position).getDone());
        holder.getIsDone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTask(position, new Task(tasks.get(position).getName(), holder.getIsDone().isChecked()));
            }
        });
        holder.getDelete_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(position);
            }
        });
        holder.getTaskName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = popupWindow.getContentView();
                TextView formTitle = view.findViewById(R.id.form_title);
                formTitle.setText("Change task");
                EditText editTaskName = view.findViewById(R.id.edit_task_name);
                editTaskName.setText(tasks.get(position).getName());
                CheckBox isDone = view.findViewById(R.id.edit_task_is_done);
                isDone.setChecked(tasks.get(position).getDone());
                Button submit = view.findViewById(R.id.submit);
                submit.setText("Change");

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        changeTask(position, new Task(editTaskName.getText().toString(), isDone.isChecked()));
                    }
                });
            }
        });
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void changeTask(int position, Task newTask) {
        this.tasks.set(position, newTask);
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        this.tasks.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private CheckBox isDone;
        private LinearLayout item;
        private Button delete_btn;

        public TextView getTaskName() {
            return taskName;
        }

        public CheckBox getIsDone() {
            return isDone;
        }

        public LinearLayout getItem() {
            return item;
        }

        public Button getDelete_btn() {
            return delete_btn;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name);
            isDone = itemView.findViewById(R.id.is_done);
            item = itemView.findViewById(R.id.task_item);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }


}
