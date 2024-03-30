package com.jads.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jads.model.Cambio;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class CambioRepositoryIntegrationTest {

    @Autowired
    private CambioRepository repository;

    @Test
	@DisplayName("Deve retornar sucesso quando pesquisar por uma combinação válida de moedas")
    public void findByFromAndToTest() {
        Cambio cambio = new Cambio();
        cambio.setFrom("USD");
        cambio.setTo("EUR");
        cambio.setConversionFactor(new BigDecimal("1.23"));
        repository.save(cambio);

        Cambio foundCambio = repository.findByFromAndTo("USD", "EUR");

        assertEquals(cambio, foundCambio);
    }
    
    @Test
	@DisplayName("Deve retornar nulo quando não houver dados para a combinação de moedas")
    public void testFindByFromAndTo() {
        Cambio foundCambio = repository.findByFromAndTo("USD", "BRL");

        assertNull(foundCambio);
    }
    
}
