package com.frieghtfox.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RetrieveEquationResponse {
	
	private List<EquationResponse> equations;

}
