package com.frieghtfox.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.frieghtfox.dto.EquationDto;
import com.frieghtfox.model.Equation;
import com.frieghtfox.model.EquationNode;

import lombok.Setter;


@Repository

public class EquationRepository {
	
	@Setter
	private static int count = 0;
	private Map<Integer, Equation> equationMap = new HashMap<>();

	public int store(EquationNode equation, Set<String> variables) {
		int id = ++count;
		
		equationMap.put(id, new Equation(variables, equation));
		return id;
	}

	public List<EquationDto> retrive() {
		return equationMap.entrySet().stream()
				.map(entry -> new EquationDto(entry.getKey(), entry.getValue().getRoot()))
				.collect(Collectors.toList());
	}
	
	public Optional<Equation> retriveById(int id) {
		if(!equationMap.containsKey(id)) {
			return Optional.empty();
		}
		return Optional.of(equationMap.get(id));
	}

}
