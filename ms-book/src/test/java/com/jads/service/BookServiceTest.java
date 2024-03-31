package com.jads.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jads.client.CambioClient;
import com.jads.dto.CambioDTO;
import com.jads.model.Book;
import com.jads.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    @Mock
    private BookRepository repository;

    @Mock
    private CambioClient cambioClient;

    @Mock
    private Environment environment;

    @InjectMocks
    private BookService service;

    @Test
    @DisplayName("Deve calcular o valor do livro com base no cambio retornado")
    public void calculatePriceTest() throws Exception {
        String id = "1";
        String currency = "EUR";
        Long bookId = Long.parseLong(id);
        Double convertedValue = 85.0;
        Double cambioValue = 100.0;
        
        Book book = new Book();
        book.setId(bookId);
        book.setPrice(cambioValue);
        when(repository.getReferenceById(bookId)).thenReturn(book);

        CambioDTO cambioDTO = new CambioDTO();
        cambioDTO.setConvertedValue(convertedValue);
        cambioDTO.setEnvironment("8000");
        when(cambioClient.getCambio(cambioValue, "USD", currency)).thenReturn(cambioDTO);

        when(environment.getProperty("local.server.port")).thenReturn("8080");

        Book result = service.calculatePrice(id, currency);

        assertEquals(convertedValue, result.getPrice());
        assertEquals("Book port 8080 Cambio port 8000", result.getEnvironment());
        verify(repository, times(1)).getReferenceById(bookId);
        verify(cambioClient, times(1)).getCambio(cambioValue, "USD", currency);
        verify(environment, times(1)).getProperty("local.server.port");
    }
    
    @Test
    @DisplayName("Deve retornar o valor do cambio")
    public void getCambioTest() {
        double price = 100.0;
        String currency = "EUR";
        
        CambioDTO cambio = new CambioDTO();
        cambio.setConvertedValue(85.0);
        when(cambioClient.getCambio(price, "USD", currency)).thenReturn(cambio);

        CambioDTO result = service.getCambio(price, currency);

        assertEquals(cambio, result);
        verify(cambioClient, times(1)).getCambio(price, "USD", currency);
    }
    
    @Test
    @DisplayName("Deve atualizar o valor do livro e o environment port")
    public void updateBookPriceTest() {
        Book book = new Book();
        when(environment.getProperty("local.server.port")).thenReturn("5000");
        
        CambioDTO cambio = new CambioDTO();
        cambio.setConvertedValue(85.0);
        cambio.setEnvironment("4000");

        service.updateBookPrice(book, cambio);

        assertEquals(85.0, book.getPrice());
        assertEquals("Book port 5000 Cambio port 4000", book.getEnvironment());
    }
	
}
