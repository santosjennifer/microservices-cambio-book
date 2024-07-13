package com.jads.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jads.client.CambioClient;
import com.jads.dto.BookDto;
import com.jads.dto.CambioDto;
import com.jads.exception.BookNotFoundException;
import com.jads.model.Book;
import com.jads.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @Mock
    private CambioClient cambioClient;

    @Mock
    private Environment environment;
    
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl service;

    @Test
    @DisplayName("Deve calcular o valor do livro com base no cambio retornado")
    public void calculatePriceTest() {
        String id = "1";
        String currency = "EUR";
        Long bookId = Long.parseLong(id);
        Double convertedValue = 85.0;
        Double cambioValue = 100.0;
        String bookPort = "3999";
        String cambioPort = "4000";
        
        Book book = new Book();
        book.setId(bookId);
        book.setPrice(cambioValue);
        when(repository.findById(bookId)).thenReturn(Optional.of(book));

        CambioDto cambioDto = new CambioDto();
        cambioDto.setConvertedValue(convertedValue);
        cambioDto.setEnvironment(cambioPort);
        when(cambioClient.getCambio(cambioValue, "USD", currency)).thenReturn(cambioDto);

        when(environment.getProperty("local.server.port")).thenReturn(bookPort);
        
        BookDto bookDto = new BookDto();
        bookDto.setPrice(convertedValue);
        bookDto.setEnvironment("Book port " + bookPort + " Cambio port " + cambioPort);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto result = service.calculatePrice(id, currency);

        assertEquals(convertedValue, result.getPrice());
        assertEquals("Book port " + bookPort + " Cambio port " + cambioPort, result.getEnvironment());
        verify(repository, times(1)).findById(bookId);
        verify(cambioClient, times(1)).getCambio(cambioValue, "USD", currency);
        verify(environment, times(1)).getProperty("local.server.port");
    }
    
    @Test
    @DisplayName("Deve retornar o valor do cambio")
    public void getCambioTest() {
        double price = 100.0;
        String currency = "EUR";
        
        CambioDto cambio = new CambioDto();
        cambio.setConvertedValue(85.0);
        when(cambioClient.getCambio(price, "USD", currency)).thenReturn(cambio);

        CambioDto result = service.getCambio(price, currency);

        assertEquals(cambio, result);
        verify(cambioClient, times(1)).getCambio(price, "USD", currency);
    }
    
    @Test
    @DisplayName("Deve atualizar o valor do livro e o environment port")
    public void updateBookPriceTest() {
        BookDto book = new BookDto();
        when(environment.getProperty("local.server.port")).thenReturn("5000");
        
        CambioDto cambio = new CambioDto();
        cambio.setConvertedValue(85.0);
        cambio.setEnvironment("4000");

        service.updateBookPrice(book, cambio);

        assertEquals(85.0, book.getPrice());
        assertEquals("Book port 5000 Cambio port 4000", book.getEnvironment());
    }
    
    @Test
    @DisplayName("Deve retornar um erro ao calcular o valor do livro")
    public void calculatePriceExceptionTest() throws Exception {
        Long id = 1l;
        String currency = "AAA";
        String message = "Ocorreu um erro ao calcular o preço do livro.";
        
        Book book = new Book();
        book.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        when(repository.findById(id)).thenThrow(
        		new RuntimeException(message));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.calculatePrice(id.toString(), currency);
        });

        assertEquals(message, exception.getMessage());
        verify(repository, times(1)).findById(id);
    }
    
    @Test
    @DisplayName("Deve lançar uma exceção quando o livro não é encontrado")
    public void calculatePriceBookNotFoundExceptionTest() {
        String id = "1";
        String currency = "EUR";
        Long bookId = Long.parseLong(id);

        when(repository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            service.calculatePrice(id, currency);
        });

        assertEquals("Livro não encontrado com o ID: " + id, exception.getMessage());
        verify(repository, times(1)).findById(bookId);
    }
	
}
