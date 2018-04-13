package com.auth0.samples.authapi.task;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	private TaskRepository taskRepository;

	public TaskController(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@PostMapping
	public Task addTask(@RequestBody Task task) {
		taskRepository.save(task);
		return task;
	}

	@GetMapping
	public TasksResponse getTasks(@RequestParam(name = "query", required = false) String query) {
		TasksResponse response = new TasksResponse();
		response.setBooks(taskRepository.findByTitleContaining(query!=null?query:""));
		return response;
	}

	@GetMapping("/me")
	public List<Task> getTasksAdmin() {
		return taskRepository.findAll();
	}
	@PutMapping("/{id}")
	public void editTask(@PathVariable long id, @RequestBody Task task) {
		Task existingTask = taskRepository.findOne(id);
		Assert.notNull(existingTask, "Task not found");
		//existingTask.setDescription(task.getDescription());
		taskRepository.save(existingTask);
	}

	@DeleteMapping("/{id}")
	public void deleteTask(@PathVariable long id) {
		taskRepository.delete(id);
	}
}
