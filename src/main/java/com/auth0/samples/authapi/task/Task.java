package com.auth0.samples.authapi.task;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long goodreadsId;
	private String title;
	private int pages;
	private String authors;


	protected Task() {
	}

	public long getGoodreadsId() {
		return goodreadsId;
	}

	public void setGoodreadsId(long goodreadsId) {
		this.goodreadsId = goodreadsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

}
