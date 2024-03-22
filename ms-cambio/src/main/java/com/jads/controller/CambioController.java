package com.jads.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jads.model.Cambio;
import com.jads.repository.CambioRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("cambio")
@Tag(name = "Cambio")
@OpenAPIDefinition(info = @Info(title = "Cambio API", version = "v1.0", description = "Documentation of Cambio API"))
public class CambioController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private CambioRepository repository;
	
	@GetMapping("/{amount}/{from}/{to}")
	public Cambio getCambio(
			@PathVariable("amount") String amount, 
			@PathVariable("from") String from,
			@PathVariable("to") String to) {
		
		BigDecimal amountValue = new BigDecimal(amount);
		
		Cambio cambio = repository.findByFromAndTo(from, to);
		
		String port = environment.getProperty("local.server.port");
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedValue = conversionFactor.multiply(amountValue);
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		cambio.setEnvironment(port);
		
		return cambio;
	}

}
