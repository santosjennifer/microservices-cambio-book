package com.jads.exception.handler;

import java.util.Date;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jads.exception.ExceptionResponse;

import jakarta.ws.rs.BadRequestException;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidConfigurationPropertyValueException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(
			InvalidConfigurationPropertyValueException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Recurso não encontrado.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Requisição inválida.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Valor inválido.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ExceptionResponse> handleNumberFormatException(NumberFormatException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Valor inválido.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Moeda inválida.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleCustomException(Exception ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Ocorreu um erro.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
