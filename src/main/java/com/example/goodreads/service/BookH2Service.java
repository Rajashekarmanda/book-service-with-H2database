package com.example.goodreads.service;

import com.example.goodreads.model.Book;
import com.example.goodreads.repository.BookRepository;
import com.example.goodreads.model.BookRowMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

@Service
public class BookH2Service implements BookRepository {

	@Autowired
	private JdbcTemplate db;

	@Override
	public ArrayList<Book> getBooks() {
		List<Book> bookList = db.query("select * from book", new BookRowMapper());
		ArrayList<Book> books = new ArrayList<>(bookList);
		return books;
	}

	@Override
	public Book getBookById(int bookId) {
		try {
			Book book = db.queryForObject("select * from book where id = ?", new BookRowMapper(), bookId);
			return book;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Book addBook(Book book) {
		db.update("insert into book(name,imageUrl) values(?,?)", book.getName(), book.getImageUrl());
		Book savedBook = db.queryForObject("select * from book where name = ? and imageUrl = ?", new BookRowMapper(),
				book.getName(), book.getImageUrl());
		return savedBook;
	}

	@Override
	public Book updateBook(int bookId, Book book) {
		if (book.getName() != null) {
			db.update("update book SET name = ? WHERE id = ?", book.getName(), bookId);
		}
		if (book.getImageUrl() != null) {
			db.update("update book SET imageUrl = ? WHERE id = ?", book.getImageUrl(), bookId);
		}
		return getBookById(bookId);
	}

	@Override
	public void deleteBook(int bookId) {
		db.update("delete from book where id = ?", bookId);
	}

}