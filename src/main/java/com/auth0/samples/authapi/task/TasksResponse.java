package com.auth0.samples.authapi.task;

import java.util.List;

public class TasksResponse {
	private List<Task> books;

	public List<Task> getBooks() {
		return books;
	}

	public void setBooks(List<Task> books) {
		this.books = books;
	}
}
