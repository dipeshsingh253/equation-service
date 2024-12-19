package com.frieghtfox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquationNode {
	
	private String value;
	private EquationNode right;
	private EquationNode left;
	
	public EquationNode(String value) {
		this.value = value;
	}
	

}
