package com.frieghtfox.repositiory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.frieghtfox.dto.EquationDto;
import com.frieghtfox.model.Equation;
import com.frieghtfox.model.EquationNode;
import com.frieghtfox.repository.EquationRepository;

public class EquationRepositoryTest {
	private EquationRepository equationRepository;

	@BeforeEach
	public void setUp() {
		equationRepository = new EquationRepository();
		EquationRepository.setCount(0);
	}

	// Test for store method
	@Test
	void testStore() {
		EquationNode equationNode = mock(EquationNode.class);
		Set<String> variables = Set.of("x", "y", "z");
		int id = equationRepository.store(equationNode, variables);

		assertEquals(1, id);
	}

	// Test for retrieve method (retrieving all equations)
	@Test
	void testRetrieve() {
		EquationNode equationNode = mock(EquationNode.class);
		Set<String> variables = Set.of("x", "y", "z");
		equationRepository.store(equationNode, variables);
		List<EquationDto> equations = equationRepository.retrive();

		assertFalse(equations.isEmpty());
		assertEquals(1, equations.size());
		assertEquals(1, equations.get(0).getId());
	}

	// Test for retriveById method (retrieving a specific equation by ID)
	@Test
	void testRetrieveById() {
		EquationNode equationNode = mock(EquationNode.class);
		Set<String> variables = Set.of("x", "y", "z");
		int id = equationRepository.store(equationNode, variables);
		Optional<Equation> equation = equationRepository.retriveById(id);

		assertTrue(equation.isPresent());
		assertEquals(variables, equation.get().getVariables());
	}

	// Test for retriveById when the ID doesn't exist
	@Test
	void testRetrieveByIdNotFound() {
		Optional<Equation> equation = equationRepository.retriveById(999);
		assertFalse(equation.isPresent());
	}
}
