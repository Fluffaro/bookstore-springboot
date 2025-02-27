package com.example.todoapp.services;

import com.example.todoapp.models.Task;
import com.example.todoapp.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask){
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setAuthor(updatedTask.getAuthor());
            task.setPrice(updatedTask.getPrice());
            task.setDescription(updatedTask.getDescription());
            task.setCoverImage(updatedTask.getCoverImage());
            task.setCategory(updatedTask.getCategory());
            task.setFeatured(updatedTask.getFeatured());
            return taskRepository.save(task);
        }).orElse(null);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
