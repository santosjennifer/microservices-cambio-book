package com.jads.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.jads.model.Cambio;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CambioRepositoryTest {

	 @Mock
	 private CambioRepository repository;
	 
	 @Test
	 @DisplayName("Deve validar o retorno do findByFromAndTo")
	 public void findByFromAndToTest() {
	     String from = "USD";
	     String to = "EUR";
	     Cambio expectedCambio = new Cambio();
	     expectedCambio.setId(1L);
	     expectedCambio.setFrom(from);
	     expectedCambio.setTo(to);
	     when(repository.findByFromAndTo(from, to)).thenReturn(expectedCambio);

	     Cambio actualCambio = repository.findByFromAndTo(from, to);

	     assertEquals(expectedCambio, actualCambio);

	 }
}
