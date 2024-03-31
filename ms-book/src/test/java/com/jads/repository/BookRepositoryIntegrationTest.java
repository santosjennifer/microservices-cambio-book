package com.jads.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jads.model.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryIntegrationTest {

	@Autowired
	private BookRepository repository;

	@Test
	@DisplayName("Deve validar os dados do livro retornandos do banco de dados")
	public void saveBookTest() {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Percy Jackson");
		book.setAuthor("Rick Riordan");
		book.setPrice(59.99);
		book.setLaunchDate(new Date());

		Book savedBook = repository.save(book);

		assertNotNull(savedBook);
		assertEquals(book.getId(), savedBook.getId());
		assertEquals(book.getTitle(), savedBook.getTitle());
		assertEquals(book.getAuthor(), savedBook.getAuthor());
		assertEquals(book.getPrice(), savedBook.getPrice());
		assertEquals(book.getLaunchDate(), savedBook.getLaunchDate());
	}

	@Test
	@DisplayName("Deve retornar null quando não encontrar o livro com o ID informado")
	public void returnNullWhenNotFindBookTest() {
		Book book = repository.findById(10L).orElse(null);

		assertNull(book);
	}
}
