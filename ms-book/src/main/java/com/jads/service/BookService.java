package com.jads.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jads.client.CambioClient;
import com.jads.dto.CambioDTO;
import com.jads.model.Book;
import com.jads.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository repository;

	@Autowired
	private CambioClient cambioClient;

	@Autowired
	private Environment environment;

	public Book calculatePrice(String id, String currency) throws Exception {
		Long bookId = Long.parseLong(id);
		Book book = repository.getReferenceById(bookId);

		CambioDTO cambio = getCambio(book.getPrice(), currency);
        updateBookPrice(book, cambio);
        
		return book;
	}

	private CambioDTO getCambio(Double price, String currency) {
		return cambioClient.getCambio(price, "USD", currency);
	}

	private void updateBookPrice(Book book, CambioDTO cambio) {
		String port = environment.getProperty("local.server.port");
		book.setEnvironment("Book port " + port + " Cambio port " + cambio.getEnvironment());
		book.setPrice(cambio.getConvertedValue());
	}

}
