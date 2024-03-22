package com.jads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jads.client.CambioClient;
import com.jads.model.Book;
import com.jads.repository.BookRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("book")
@Tag(name = "Book")
@OpenAPIDefinition(info = @Info(title = "Book API", version = "v1.0", description = "Documentation of Book API"))
public class BookController {
	
	@Autowired
	private BookRepository repository;
	
	@Autowired
	private CambioClient cambioClient;
	
	@Autowired
	private Environment environment;

	@GetMapping("/{id}/{currency}")
	public Book findBook(
			@PathVariable("id") String id, 
			@PathVariable("currency") String currency) {
		
		Long bookId = Long.parseLong(id);
		
		Book book = repository.getReferenceById(bookId);

		var cambio = cambioClient.getCambio(book.getPrice(), "USD", currency);
		
		String port = environment.getProperty("local.server.port");
		book.setEnvironment("Book port " + port + " Cambio port " + cambio.getEnvironment());
		book.setPrice(cambio.getConvertedValue());
		
		return book;
	}
	
}
