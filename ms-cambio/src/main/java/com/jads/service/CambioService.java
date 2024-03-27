package com.jads.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jads.model.Cambio;
import com.jads.repository.CambioRepository;

@Service
public class CambioService {

	@Autowired
	private Environment environment;

	@Autowired
	private CambioRepository repository;

	public Cambio convertCurrency(String amount, String from, String to) {
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
