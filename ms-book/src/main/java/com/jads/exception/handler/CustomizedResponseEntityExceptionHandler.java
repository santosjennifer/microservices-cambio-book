package com.jads.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jads.exception.ExceptionResponse;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(EntityNotFoundException ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Livro não encontrato.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ FeignException.BadRequest.class })
	public ResponseEntity<ExceptionResponse> handleFeignBadRequest(FeignException.BadRequest ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				"Erro de solicitação ao serviço do cambio. Verifique os valores enviados.", ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ FeignException.ServiceUnavailable.class })
	public ResponseEntity<ExceptionResponse> handleFeignServiceUnavailable(FeignException.ServiceUnavailable ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Serviço de cambio indisponível.", ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleExceptions(Exception ex) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Ocorreu um erro.", ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
