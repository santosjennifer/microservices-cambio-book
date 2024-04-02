package com.jads.service;

import com.jads.model.Cambio;

public interface CambioService {
	
	Cambio convertCurrency(String amount, String from, String to);
	
}
