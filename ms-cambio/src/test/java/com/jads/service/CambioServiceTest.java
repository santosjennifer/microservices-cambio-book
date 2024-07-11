package com.jads.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jads.dto.CambioDto;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CambioServiceTest {

	@Test
	@DisplayName("Deve validar o metodo convertCurrency com mock do CambioService")
	public void convertCurrencyTest() {
        CambioService service = mock(CambioService.class);

        String amount = "1000";
        String from = "USD";
        String to = "EUR";
        CambioDto cambio = new CambioDto();
        cambio.setConvertedValue(new BigDecimal("185.00"));
        
        when(service.convertCurrency(amount, from, to)).thenReturn(cambio);

        CambioDto result = service.convertCurrency(amount, from, to);

        assertEquals(new BigDecimal("185.00"), result.getConvertedValue());
    }
}
