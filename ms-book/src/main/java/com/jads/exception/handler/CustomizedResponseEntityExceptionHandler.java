package com.jads.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jads.exception.ExceptionResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Livro não encontrato.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(EntityNotFoundException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Livro não encontrato.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleExceptions(Exception ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Valor inválido.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
    
}
