package com.jads.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jads.client.CambioClient;
import com.jads.dto.BookDto;
import com.jads.dto.CambioDto;
import com.jads.exception.BookNotFoundException;
import com.jads.model.Book;
import com.jads.repository.BookRepository;
import com.jads.service.BookService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository repository;

	@Autowired
	private CambioClient cambioClient;

	@Autowired
	private Environment environment;

	@Autowired
	private ModelMapper modelMapper;

	@CircuitBreaker(name = "cambioService", fallbackMethod = "fallbackCalculatePrice")
	public BookDto calculatePrice(String id, String currency) {
		try {
			Long bookId = Long.parseLong(id);
			Book book = repository.findById(bookId)
					.orElseThrow(() -> new BookNotFoundException(id));
			
			CambioDto cambioDto = getCambio(book.getPrice(), currency);
			BookDto bookDto = modelMapper.map(book, BookDto.class);
			updateBookPrice(bookDto, cambioDto);

			return bookDto;
		} catch (BookNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao calcular o preço do livro.");
		}
	}

	protected CambioDto getCambio(Double price, String currency) {
		return cambioClient.getCambio(price, "USD", currency);
	}

	protected void updateBookPrice(BookDto book, CambioDto cambio) {
		String port = environment.getProperty("local.server.port");
		book.setEnvironment("Book port " + port + " Cambio port " + cambio.getEnvironment());
		book.setPrice(cambio.getConvertedValue());
	}
	
    public BookDto fallbackCalculatePrice(String id, String currency, Throwable t) {
        Long bookId = Long.parseLong(id);
        Book book = repository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(id));
        
        BookDto bookDto = modelMapper.map(book, BookDto.class);

        double fixedConversionRate = 5.1;
        double price = book.getPrice() * fixedConversionRate;
        BigDecimal formattedPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        
        bookDto.setPrice(formattedPrice.doubleValue());
        bookDto.setEnvironment("Fallback: valor fixo utilizado");

        return bookDto;
    }

}
