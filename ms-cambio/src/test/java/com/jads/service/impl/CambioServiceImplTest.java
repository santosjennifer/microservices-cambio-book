package com.jads.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jads.dto.CambioDto;
import com.jads.model.Cambio;
import com.jads.repository.CambioRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CambioServiceImplTest {

    @Mock
    private Environment environment;

    @Mock
    private CambioRepository repository;
    
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CambioServiceImpl service;

    @BeforeEach
    public void setup() {
    	MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar o valor do convertido do cambio e environment port")
    public void convertCurrencyTest() {
        Cambio cambio = new Cambio();
        cambio.setFrom("USD");
        cambio.setTo("EUR");
        cambio.setConversionFactor(new BigDecimal("0.85"));
        
        CambioDto cambioDto = new CambioDto();
        
        when(repository.findByFromAndTo("USD", "EUR")).thenReturn(cambio);
        when(environment.getProperty("local.server.port")).thenReturn("8080");
        when(modelMapper.map(cambio, CambioDto.class)).thenReturn(cambioDto);
        
        CambioDto result = service.convertCurrency("100", "USD", "EUR");

        assertEquals(new BigDecimal("85.00"), result.getConvertedValue());
        assertEquals("8080", result.getEnvironment());
    }

    @Test
    @DisplayName("Deve calcular o valor convertido do cambio")
    public void calculateConvertedValueTest() {
        Cambio cambio = new Cambio();
        cambio.setConversionFactor(new BigDecimal("0.85"));

        BigDecimal convertedValue = service.calculateConvertedValue(new BigDecimal("100"), cambio);

        assertEquals(new BigDecimal("85.00"), convertedValue);
    }

    @Test
    @DisplayName("Deve validar a atualização dos campos port e convertedValue")
    public void updateCambioFieldsTest() {
    	CambioDto cambio = new CambioDto();

        service.updateCambioFields(cambio, "8080", new BigDecimal("85.00"));

        assertEquals(new BigDecimal("85.00"), cambio.getConvertedValue());
        assertEquals("8080", cambio.getEnvironment());
    }
    
}
