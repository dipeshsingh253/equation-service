package com.frieghtfox.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Equation {
	
	private Set<String> variables;
	private EquationNode root;

}
