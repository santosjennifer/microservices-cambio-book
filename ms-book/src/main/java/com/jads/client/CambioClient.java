package com.jads.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jads.dto.CambioDto;

@FeignClient(name = "ms-cambio")
public interface CambioClient {
	
	@GetMapping(value = "/cambio/{amount}/{from}/{to}")
	public CambioDto getCambio(
			@PathVariable("amount") Double amount,
			@PathVariable("from") String from,
			@PathVariable("to") String to);
}
