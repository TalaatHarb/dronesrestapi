package com.mohamedsamir1495.dronesrestapi.exception;

import com.mohamedsamir1495.dronesrestapi.dto.ErrorResponseDto;
import com.mohamedsamir1495.dronesrestapi.exception.drone.DroneValidationException;
import com.mohamedsamir1495.dronesrestapi.exception.generic.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
		Map<String, String> validationErrors = new HashMap<>();
		List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

		validationErrorList.forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String validationMsg = error.getDefaultMessage();
			validationErrors.put(fieldName, validationMsg);
		});
		return Mono.just(ResponseEntity.badRequest().body(validationErrors));
	}


	@ExceptionHandler(Exception.class)
	public Mono<ResponseEntity<ErrorResponseDto>> handleGlobalException(Exception exception, ServerWebExchange exchange)  {
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
				exchange.getRequest().getPath().toString(),
				HttpStatus.INTERNAL_SERVER_ERROR,
				exception.getMessage(),
				LocalDateTime.now()
		);
		return Mono.just(ResponseEntity.internalServerError().body(errorResponseDTO));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public Mono<ResponseEntity<ErrorResponseDto>> handleResourceNotFoundException(ResourceNotFoundException exception,  ServerWebExchange exchange) {
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
				exchange.getRequest().getPath().toString(),
				HttpStatus.NOT_FOUND,
				exception.getMessage(),
				LocalDateTime.now()
		);
		return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO));
	}

	@ExceptionHandler(DroneValidationException.class)
	public Mono<ResponseEntity<ErrorResponseDto>> handleDroneValidationException(DroneValidationException exception,  ServerWebExchange exchange) {
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
				exchange.getRequest().getPath().toString(),
				HttpStatus.valueOf(Integer.parseInt(exchange.getResponse().getStatusCode().toString())),
				exception.getMessage(),
				LocalDateTime.now()
		);
		return Mono.just(ResponseEntity.status(HttpStatus.valueOf(Integer.parseInt(exchange.getResponse().getStatusCode().toString()))).body(errorResponseDTO));
	}

}
