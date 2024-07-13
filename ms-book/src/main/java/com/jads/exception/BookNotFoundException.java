package com.jads.exception;

public class BookNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BookNotFoundException(String id) {
		super("Livro não encontrado com o ID: " + id);
	}

}
