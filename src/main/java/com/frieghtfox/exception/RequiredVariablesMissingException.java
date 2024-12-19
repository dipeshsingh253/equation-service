package com.frieghtfox.exception;

import java.util.Set;

import lombok.Getter;

@Getter
public class RequiredVariablesMissingException extends RuntimeException {
	private Set<String> missingVariables;

	public RequiredVariablesMissingException(String message, Set<String> missingVariables) {
		super(message);
		this.missingVariables = missingVariables;
	}
}
