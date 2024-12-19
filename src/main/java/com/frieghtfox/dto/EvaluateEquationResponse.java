package com.frieghtfox.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EvaluateEquationResponse {
	
	private int equationId;
	private String equation;
	private Map<String, Integer> variables;
	private int result;

}
