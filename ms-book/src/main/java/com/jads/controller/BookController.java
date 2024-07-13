package com.jads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jads.dto.BookDto;
import com.jads.service.BookService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("book")
@Tag(name = "Book")
@OpenAPIDefinition(info = @Info(title = "Book API", version = "v1.0", description = "Documentation of Book API"))
public class BookController {
	
	@Autowired
	private BookService service;

	@GetMapping("/{id}/{currency}")
	public BookDto findBook(
			@PathVariable("id") String id, 
			@PathVariable("currency") String currency) {
		
		return service.calculatePrice(id, currency);
	}
	
}
