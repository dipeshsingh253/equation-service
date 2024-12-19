package com.frieghtfox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.frieghtfox.dto.GenericErrorResponse;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EquationNotFoundException.class)
	public ResponseEntity<GenericErrorResponse<String>> handleEquationNotFoundExceptions(EquationNotFoundException ex) {
		return new ResponseEntity<>(new GenericErrorResponse<>(ex.getMessage(), LocalDateTime.now()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RequiredVariablesMissingException.class)
	public ResponseEntity<GenericErrorResponse<String>> handleRequiredVariablesMissingExceptions(
			RequiredVariablesMissingException ex) {
		return new ResponseEntity<>(
				new GenericErrorResponse<>(ex.getMessage() + ": " + ex.getMissingVariables().toString(),
						LocalDateTime.now()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<GenericErrorResponse<String>> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex) {
		String errorMessage;

		if (ex.getCause() instanceof InvalidFormatException) {
			InvalidFormatException cause = (InvalidFormatException) ex.getCause();
			String fieldName = cause.getPath().get(cause.getPath().size() - 1).getFieldName();
			Object invalidValue = cause.getValue();
			String targetType = cause.getTargetType().getSimpleName();

			errorMessage = String.format("Invalid value '%s' for field '%s'. Expected type: %s", invalidValue,
					fieldName, targetType);
		} else {
			errorMessage = "Invalid request body format. Please check !!!";
		}

		return new ResponseEntity<>(new GenericErrorResponse<String>(errorMessage, LocalDateTime.now()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GenericErrorResponse<Map<String, String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		BindingResult result = ex.getBindingResult();
		for (FieldError error : result.getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return new ResponseEntity<>(new GenericErrorResponse<>(errors, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
	}
}
