package com.jads.service;

import com.jads.dto.BookDto;

public interface BookService {

	BookDto calculatePrice(String id, String currency);
	
}
