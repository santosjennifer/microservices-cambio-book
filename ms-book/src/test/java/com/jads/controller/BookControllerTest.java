package com.jads.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.jads.dto.BookDto;
import com.jads.exception.BookNotFoundException;
import com.jads.service.BookService;

import feign.FeignException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
	
	String BOOK_API = "/book/{id}/{currency}";
	
    @Autowired
    private MockMvc mockMvc;
	
    @InjectMocks
    private BookController controller;

    @MockBean
    private BookService service;
    
    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }

	@Test
	@DisplayName("Deve validar o retorno do livro")
	public void getObjectBookTest() {
		String id = "1";
		String currency = "BRL";
		
		BookDto book = new BookDto();
		
		when(service.calculatePrice(id, currency)).thenReturn(book);

        BookDto result = controller.findBook(id, currency);

        assertEquals(book, result);
	}
	
    @Test
    @DisplayName("Deve retornar sucesso ao chamar o endpoint book")
    public void getJsonResopnseCambioTest() throws Exception {
		String id = "1";
		String currency = "BRL";
		String title = "Percy Jackson";
		String author = "Rick Riordan";
		Date launchDate = new Date();
		Double price = 59.90;
		String env = "8001";

		BookDto book = new BookDto(Long.parseLong(id), title, author, launchDate, price, currency, env);
		
		when(service.calculatePrice(id, currency)).thenReturn(book);

        mockMvc.perform(get(BOOK_API, id, currency))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("author").value(author))
                .andExpect(jsonPath("launchDate").isNotEmpty())
                .andExpect(jsonPath("price").value(price))
                .andExpect(jsonPath("currency").value(currency))
                .andExpect(jsonPath("environment").value(env))
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve retornar mensagem de erro quando não encontrar o livro com o id informado")
    public void getMessageWhenReturnBookNotFoundExceptionTest() throws Exception {
		String id = "33";
		String currency = "EUR";
		
		when(service.calculatePrice(id, currency)).thenThrow(BookNotFoundException.class);

        mockMvc.perform(get(BOOK_API, id, currency))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Registro não encontrado."))
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve retornar mensagem de erro quando o serviço de cambio retornar erro")
    public void getMessageWhenReturnFeignExceptionBadRequestTest() throws Exception {
		String id = "1";
		String currency = "EU";
		
		when(service.calculatePrice(id, currency)).thenThrow(FeignException.BadRequest.class);

        mockMvc.perform(get(BOOK_API, id, currency))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Erro de solicitação ao serviço do cambio. Verifique os valores enviados."))
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve retornar mensagem de erro quando o serviço de cambio estiver indisponível")
    public void getMessageWhenReturnServiceUnavailableTest() throws Exception {
		String id = "1";
		String currency = "BRL";
		
		when(service.calculatePrice(id, currency)).thenThrow(FeignException.ServiceUnavailable.class);

        mockMvc.perform(get(BOOK_API, id, currency))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Serviço de cambio indisponível."))
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve lançar mensagem de erro quando ocorrer uma exceção")
    public void getMessageWhenReturnRuntimeExceptioneTest() throws Exception {
		String id = "1";
		String currency = "BRL";
		
		when(service.calculatePrice(id, currency)).thenThrow(RuntimeException.class);
		
        mockMvc.perform(get(BOOK_API, id, currency))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Ocorreu um erro."))
                .andReturn();
    }
}
