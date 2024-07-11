package com.jads.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jads.dto.CambioDto;
import com.jads.model.Cambio;
import com.jads.repository.CambioRepository;
import com.jads.service.CambioService;

@Service
public class CambioServiceImpl implements CambioService {

	@Autowired
	private Environment environment;

	@Autowired
	private CambioRepository repository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CambioDto convertCurrency(String amount, String from, String to) {
		BigDecimal amountValue = new BigDecimal(amount);
		String port = environment.getProperty("local.server.port");

		Cambio cambio = repository.findByFromAndTo(from, to);

		CambioDto cambioDto = modelMapper.map(cambio, CambioDto.class);

		BigDecimal convertedValue = calculateConvertedValue(amountValue, cambio);
		updateCambioFields(cambioDto, port, convertedValue);

		return cambioDto;
	}

	protected BigDecimal calculateConvertedValue(BigDecimal amountValue, Cambio cambio) {
		BigDecimal conversionFactor = cambio.getConversionFactor();
		return conversionFactor.multiply(amountValue);
	}

	protected void updateCambioFields(CambioDto cambioDto, String port, BigDecimal convertedValue) {
		cambioDto.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		cambioDto.setEnvironment(port);
	}

}
