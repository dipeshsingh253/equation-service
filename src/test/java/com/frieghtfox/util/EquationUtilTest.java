package com.frieghtfox.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;
import com.frieghtfox.model.EquationNode;

public class EquationUtilTest {

	private final EquationUtil equationUtil = new EquationUtil();

	@Test
	public void testConvertToPostFix() {
		String infix = "a+b*(c^d-e)^(f+g*h)-i";
		String expectedPostfix = "abcd^e-fgh*+^*+i-";
		String result = equationUtil.convertToPostFix(infix);
		assertEquals(expectedPostfix, result);
	}

	@Test
	public void testConvertToPostFixWithVariables() {
		String infix = "x*y+z";
		String expectedPostfix = "xy*z+";
		String result = equationUtil.convertToPostFix(infix);
		assertEquals(expectedPostfix, result);
	}

	@Test
	public void testConvertToInfix() {
		EquationNode node = new EquationNode("+", new EquationNode("x"),
				new EquationNode("*", new EquationNode("y"), new EquationNode("z")));

		String result = equationUtil.convertToInfix(node);
		String expected = "z*y+x";
		assertEquals(expected, result);
	}

	@Test
	public void testCreateExpressionTree() {
		String postfix = "xy*z+";
		EquationNode root = equationUtil.createExpressionTree(postfix);
		assertNotNull(root);
	}

	@Test
	public void testCreateExpressionTreeThrowsExceptionForInvalidPostfix() {
		String invalidPostfix = "xy*+z";

		try {
			equationUtil.createExpressionTree(invalidPostfix);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid postfix expression: " + invalidPostfix, e.getMessage());
		}
	}

	@Test
	public void testEvaluate() {
		EquationNode node = new EquationNode("-", new EquationNode("^", new EquationNode("2"),
				new EquationNode("/", new EquationNode("b"), new EquationNode("a")

				)), new EquationNode("*", new EquationNode("+", new EquationNode("z"), new EquationNode("y")),
						new EquationNode("x")));

		Map<String, Integer> variables = Map.of("x", 2, "y", 3, "z", 4, "a", 2, "b", 3);

		int result = equationUtil.evaluate(node, variables);
		assertEquals(14, result);
	}

	@Test
	public void testEvaluateWithVariables() {
		EquationNode node = new EquationNode("+", new EquationNode("a"), new EquationNode("b"));

		Map<String, Integer> variables = Map.of("a", 5, "b", 3);

		int result = equationUtil.evaluate(node, variables);
		assertEquals(8, result);
	}

	@Test
	public void testIsVariable() {
		assertTrue(equationUtil.isVariable("x"));
		assertTrue(equationUtil.isVariable("y"));
		assertFalse(equationUtil.isVariable("123"));
		assertFalse(equationUtil.isVariable("xy"));
	}

	@Test
	public void testFormatExpression() {
		String expression = "x+y";
		String formatted = equationUtil.formatExpression(expression);
		assertEquals("x+y", formatted);

		expression = "x2y";
		formatted = equationUtil.formatExpression(expression);
		assertEquals("x*2*y", formatted);
	}

	@Test
	public void testRevertFormattedExpression() {
		String formatted = "x*2*y";
		String reverted = equationUtil.revertFormattedExpression(formatted);
		assertEquals("x2y", reverted);

		formatted = "x+(y*z)";
		reverted = equationUtil.revertFormattedExpression(formatted);
		assertEquals("x+(yz)", reverted);
	}

	@Test
	public void testPrec() {
		assertEquals(3, equationUtil.prec('^'));
		assertEquals(2, equationUtil.prec('*'));
		assertEquals(2, equationUtil.prec('/'));
		assertEquals(1, equationUtil.prec('+'));
		assertEquals(1, equationUtil.prec('-'));
	}
}
