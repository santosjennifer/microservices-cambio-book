package com.jads.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

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

import com.jads.model.Cambio;
import com.jads.service.CambioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CambioController.class)
public class CambioControllerTest {
	
	String CAMBIO_API = "/cambio/{amount}/{from}/{to}";
	
    @Autowired
    private MockMvc mockMvc;
	
    @InjectMocks
    private CambioController controller;

    @MockBean
    private CambioService service;
    
    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar objeto Cambio esperado ao chamar getCambio com parâmetros válidos")
    public void getObjectCambioTest() {
        String amount = "100";
        String from = "USD";
        String to = "BRL";

        Cambio cambio  = new Cambio();
        when(service.convertCurrency(amount, from, to)).thenReturn(cambio);

        Cambio result = controller.getCambio(amount, from, to);

        assertEquals(cambio, result);
    }
    
    @Test
    @DisplayName("Deve retornar sucesso ao chamar o endpoint /cambio")
    public void getJsonResopnseCambioTest() throws Exception {
        Long id = 1L;
        String amount = "100";
        String from = "USD";
        String to = "EUR";
        BigDecimal value = new BigDecimal(100.0);
        String env = "9000";

        Cambio cambio = new Cambio(id, from, to, value, value, env);
        
        when(service.convertCurrency(amount, from, to)).thenReturn(cambio);

        mockMvc.perform(get(CAMBIO_API, amount, from, to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("from").value(from))
                .andExpect(jsonPath("to").value(to))
                .andExpect(jsonPath("conversionFactor").value(amount))
                .andExpect(jsonPath("convertedValue").value(amount))
                .andExpect(jsonPath("environment").value(env))
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve retornar mensagem tratada ao ocorrer NullPointerException")
    public void getMessageWhenReturnNullPointerExceptioTest() throws Exception {
        String amount = "20";
        String from = "USD";
        String to = "EUR";

        when(service.convertCurrency(amount, from, to)).thenThrow(NullPointerException.class);

        mockMvc.perform(get(CAMBIO_API, amount, from, to))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Moeda inválida."))
                .andExpect(jsonPath("debug").hasJsonPath())
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve retornar mensagem tratada ao ocorrer NumberFormatException")
    public void getMessageWhenReturnNumberFormatExceptionTest() throws Exception {
        String amount = "abc";
        String from = "USD";
        String to = "EUR";

        when(service.convertCurrency(amount, from, to)).thenThrow(NumberFormatException.class);

        mockMvc.perform(get(CAMBIO_API, amount, from, to))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Valor inválido."))
                .andExpect(jsonPath("debug").hasJsonPath())
                .andReturn();
    }
    
    @Test
    @DisplayName("Deve retornar mensagem de erro generica ao ocorrer RuntimeException")
    public void getMessageWhenReturnRuntimeExceptionTest() throws Exception {
        String amount = "1";
        String from = "USD";
        String to = "EUR";

        when(service.convertCurrency(amount, from, to)).thenThrow(RuntimeException.class);

        mockMvc.perform(get(CAMBIO_API, amount, from, to))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Ocorreu um erro."))
                .andExpect(jsonPath("debug").hasJsonPath())
                .andReturn();
    }

}
