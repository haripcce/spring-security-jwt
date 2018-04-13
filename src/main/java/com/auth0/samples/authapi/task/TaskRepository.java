package com.auth0.samples.authapi.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
	public List<Task> findByTitleContaining(String title);
}
