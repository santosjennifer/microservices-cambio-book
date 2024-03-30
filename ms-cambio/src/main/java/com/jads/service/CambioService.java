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
		String port = environment.getProperty("local.server.port");
		Cambio cambio = repository.findByFromAndTo(from, to);
		BigDecimal convertedValue = calculateConvertedValue(amountValue, cambio);
		updateCambioFields(cambio, port, convertedValue);
		return cambio;
	}

	protected BigDecimal calculateConvertedValue(BigDecimal amountValue, Cambio cambio) {
		BigDecimal conversionFactor = cambio.getConversionFactor();
		return conversionFactor.multiply(amountValue);
	}

	protected void updateCambioFields(Cambio cambio, String port, BigDecimal convertedValue) {
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		cambio.setEnvironment(port);
	}

}
