package com.frieghtfox.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.frieghtfox.dto.EquationResponse;
import com.frieghtfox.dto.EvaluateEquationResponse;
import com.frieghtfox.dto.StoreEquationResponse;
import com.frieghtfox.exception.EquationNotFoundException;
import com.frieghtfox.exception.RequiredVariablesMissingException;
import com.frieghtfox.model.Equation;
import com.frieghtfox.model.EquationNode;
import com.frieghtfox.repository.EquationRepository;
import com.frieghtfox.util.EquationUtil;

@Service
public class EquationService {

	private final EquationRepository equationRepository;
	private final EquationUtil equationUtil;

	public EquationService(EquationRepository equationRepository, EquationUtil equationUtil) {
		this.equationRepository = equationRepository;
		this.equationUtil = equationUtil;
	}

	public StoreEquationResponse storeEquation(String expression) {

		String infix = equationUtil.formatExpression(expression);
		String postfix = equationUtil.convertToPostFix(infix);
		Set<String> variables = new HashSet<>();
		for(char ch:postfix.toCharArray()) {
			if(equationUtil.isVariable(ch+"")) {
				variables.add(ch+"");
			}
		}
		int equationId = equationRepository.store(equationUtil.createExpressionTree(postfix),variables);

		return new StoreEquationResponse(equationId, "Equation stored successfully");
	}

	public List<EquationResponse> retrieveExpression() {

		return equationRepository.retrive().stream()
				.map(entry -> new EquationResponse(entry.getId(),
						equationUtil.revertFormattedExpression(equationUtil.convertToInfix(entry.getRoot()))))
				.collect(Collectors.toList());

	}

	public EvaluateEquationResponse evaluate(int equationId, Map<String, Integer> variables) {
		
		
		Optional<Equation> equation = equationRepository.retriveById(equationId);
		if (equation.isEmpty()) {
			throw new EquationNotFoundException("Please enter a valid equation Id");
		}
		Set<String> missingVariables = new HashSet<>();
		for(String var : equation.get().getVariables()) {
			if (!variables.containsKey(var)) {
				missingVariables.add(var);
			}
		}
		if (!missingVariables.isEmpty()) {
			throw new RequiredVariablesMissingException("Please provide valid value for listed required variables",missingVariables) ;
		}
		EquationNode root = equation.get().getRoot();

		return new EvaluateEquationResponse(equationId,
				equationUtil.revertFormattedExpression(equationUtil.convertToInfix(root)), variables,
				equationUtil.evaluate(root, variables));

	}

}
