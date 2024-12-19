package com.frieghtfox.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.frieghtfox.dto.EquationDto;
import com.frieghtfox.dto.EquationResponse;
import com.frieghtfox.dto.EvaluateEquationResponse;
import com.frieghtfox.dto.StoreEquationResponse;
import com.frieghtfox.exception.EquationNotFoundException;
import com.frieghtfox.exception.RequiredVariablesMissingException;
import com.frieghtfox.model.Equation;
import com.frieghtfox.model.EquationNode;
import com.frieghtfox.repository.EquationRepository;
import com.frieghtfox.util.EquationUtil;

public class EquationServiceTest {
	@Mock
    private EquationRepository equationRepository;

    @Mock
    private EquationUtil equationUtil;

    @InjectMocks
    private EquationService equationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for storeEquation
    @Test
    void testStoreEquation() {
        String expression = "3x + 2y - z";

        when(equationUtil.formatExpression(expression)).thenReturn("3x+2y-z");
        when(equationUtil.convertToPostFix("3x+2y-z")).thenReturn("3x2yz+-");
        when(equationUtil.isVariable("x")).thenReturn(true);
        when(equationUtil.isVariable("y")).thenReturn(true);
        when(equationUtil.isVariable("z")).thenReturn(true);
        when(equationUtil.createExpressionTree("3x2yz+-")).thenReturn(new EquationNode());

        when(equationRepository.store(any(), any())).thenReturn(1); 

        StoreEquationResponse response = equationService.storeEquation(expression);

        assertEquals("Equation stored successfully", response.getMessage());
        assertEquals(1, response.getEquationId());
    }

    // Test for retrieveExpression
    @Test
    void testRetrieveExpression() {
        EquationDto equation = new EquationDto(1, new EquationNode());
        when(equationRepository.retrive()).thenReturn(List.of(equation));

        when(equationUtil.convertToInfix(any())).thenReturn("3x+2y-z");
        when(equationUtil.revertFormattedExpression(any())).thenReturn("3x+2y-z");

        List<EquationResponse> response = equationService.retrieveExpression();

        assertEquals(1, response.size());
        assertEquals("3x+2y-z", response.get(0).getEquation());
    }

    // Test for evaluate with valid variables
    @Test
    void testEvaluateEquationValid() {
        Map<String, Integer> variables = Map.of("x", 2, "y", 3, "z", 4);

        Equation equation = new Equation(Set.of("x", "y", "z"), new EquationNode());
        when(equationRepository.retriveById(1)).thenReturn(Optional.of(equation));
        when(equationUtil.evaluate(any(), any())).thenReturn(8);
        when(equationUtil.convertToInfix(any())).thenReturn("3x+2y-z");
        when(equationUtil.revertFormattedExpression(any())).thenReturn("3x+2y-z");

        EvaluateEquationResponse response = equationService.evaluate(1, variables);

        assertEquals(8, response.getResult());
        assertEquals("3x+2y-z", response.getEquation());
    }

    // Test for evaluate when equation is not found
    @Test
    void testEvaluateEquationNotFound() {
        Map<String, Integer> variables = Map.of("x", 2, "y", 3, "z", 4);

        when(equationRepository.retriveById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EquationNotFoundException.class, () -> {
            equationService.evaluate(1, variables);
        });

        assertEquals("Please enter a valid equation Id", exception.getMessage());
    }

    // Test for evaluate with missing variables
    @Test
    void testEvaluateEquationMissingVariables() {
        Map<String, Integer> variables = Map.of("x", 2, "y", 3);

        Equation equation = new Equation( Set.of("x", "y", "z"), new EquationNode());
        when(equationRepository.retriveById(1)).thenReturn(Optional.of(equation));

        Exception exception = assertThrows(RequiredVariablesMissingException.class, () -> {
            equationService.evaluate(1, variables);
        });

        assertTrue(exception.getMessage().contains("Please provide valid value for listed required variables"));
    }
}
