package com.frieghtfox.dto;

import com.frieghtfox.model.EquationNode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EquationDto {
	
	private int id;
	private EquationNode root;

}
