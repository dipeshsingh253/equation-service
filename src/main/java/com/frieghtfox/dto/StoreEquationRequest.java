package com.frieghtfox.dto;

import com.frieghtfox.validation.annotation.ValidEquation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreEquationRequest {

	@ValidEquation(message = "Please enter a valid eqaution")
	private String equation;

}
