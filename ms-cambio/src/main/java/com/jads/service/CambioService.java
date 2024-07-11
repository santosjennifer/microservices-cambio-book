package com.jads.service;

import com.jads.dto.CambioDto;

public interface CambioService {
	
	CambioDto convertCurrency(String amount, String from, String to);
	
}
