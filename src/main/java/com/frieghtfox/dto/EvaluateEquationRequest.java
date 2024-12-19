package com.frieghtfox.dto;

import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EvaluateEquationRequest {
	
	private Map<String, Integer> variables;

}
