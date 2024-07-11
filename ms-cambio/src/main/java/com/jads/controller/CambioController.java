package com.jads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jads.dto.CambioDto;
import com.jads.service.CambioService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("cambio")
@Tag(name = "Cambio")
@OpenAPIDefinition(info = @Info(title = "Cambio API", version = "v1.0", description = "Documentation of Cambio API"))
public class CambioController {

	@Autowired
	private CambioService cambioService;

	@GetMapping("/{amount}/{from}/{to}")
	public CambioDto getCambio(
			@PathVariable("amount") String amount, 
			@PathVariable("from") String from,
			@PathVariable("to") String to) {

		return cambioService.convertCurrency(amount, from, to);
	}

}
