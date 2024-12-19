package com.frieghtfox.util;

import java.util.Map;
import java.util.Stack;

import org.springframework.stereotype.Component;

import com.frieghtfox.model.EquationNode;

@Component
public class EquationUtil {

	public String convertToPostFix(String infix) {

		Stack<Character> stack = new Stack<>();
		StringBuilder postfixBuilder = new StringBuilder();

		for (char ch : infix.toCharArray()) {

			if (isOperand(ch)) {
				postfixBuilder.append(ch);
			} else if (ch == '(') {
				stack.push(ch);
			} else if (ch == ')') {
				while (stack.peek() != '(') {
					postfixBuilder.append(stack.pop());
				}
				stack.pop();
			} else {
				while (!stack.isEmpty() && (prec(ch) < prec(stack.peek()) || prec(ch) == prec(stack.peek()))) {
					postfixBuilder.append(stack.pop());
				}
				stack.push(ch);
			}
		}
		while (!stack.isEmpty()) {
			postfixBuilder.append(stack.pop());
		}

		return postfixBuilder.toString();
	}

	public String convertToInfix(EquationNode root) {

		StringBuilder res = new StringBuilder();
		infix(root, res);

		return res.toString();

	}

	private void infix(EquationNode root, StringBuilder sb) {
		if (root != null) {
			infix(root.getLeft(), sb);
			sb.append(root.getValue());
			infix(root.getRight(), sb);
		}
	}

	public EquationNode createExpressionTree(String postfix) {
		if (postfix == null || postfix.isEmpty()) {
			throw new IllegalArgumentException("Postfix expression cannot be null or empty.");
		}

		Stack<EquationNode> stack = new Stack<>();

		for (char ch : postfix.toCharArray()) {

			if (isOperand(ch)) {
				stack.push(new EquationNode(ch + ""));
			} else {
				if (stack.size() < 2) {
					throw new IllegalArgumentException("Invalid postfix expression: " + postfix);
				}
				EquationNode left = stack.pop();
				EquationNode right = stack.pop();
				EquationNode operatorNode = new EquationNode(ch + "", left, right);
				stack.push(operatorNode);
			}
		}
		if (stack.size() != 1) {
			throw new IllegalArgumentException("Invalid postfix expression: " + postfix);
		}
		return stack.pop();

	}

	public int evaluate(EquationNode root, Map<String, Integer> variables) {

		if (root != null) {
			if (root.getLeft() == null && root.getRight() == null) {
				if (isVariable(root.getValue())) {
					return variables.get(root.getValue());
				} else {
					return Integer.valueOf(root.getValue());
				}
			}

			int left = evaluate(root.getLeft(), variables);
			int right = evaluate(root.getRight(), variables);

			if (root.getValue().equals("+")) {
				return left + right;
			}
			if (root.getValue().equals("-")) {
				return left - right;
			}
			if (root.getValue().equals("*")) {
				return left * right;
			}
			if (root.getValue().equals("^")) {
				return (int) Math.pow(left, right);
			}

			if (root.getValue().equals("/") && right != 0) {
				return left / right;
			}

		}

		return 0;
	}

	private boolean isOperand(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
	}

	public boolean isVariable(String str) {
		return str.matches("[a-zA-Z]");
	}

	public String formatExpression(String expression) {
		return expression.replaceAll("([a-zA-Z])(?=[a-zA-Z])", "$1*") // Add * between alphabet and alphabet
				.replaceAll("(\\d)([a-zA-Z])", "$1*$2") // Add * between digit and alphabet
				.replaceAll("([a-zA-Z])(\\d)", "$1*$2") // Add * between alphabet and digit
				.replaceAll("\\s+", "") // Remove white spaces
				.replaceAll("(\\d)\\(", "$1*(") // Digit followed by '('
				.replaceAll("\\)(\\d)", ")*$1") // ')' followed by a digit
				.replaceAll("([a-zA-Z])\\(", "$1*(") // Variable followed by '('
				.replaceAll("\\)([a-zA-Z])", ")*$1"); // ')' followed by a variable
	}

	public String revertFormattedExpression(String expression) {

		return expression.replaceAll("(\\d)\\*(?=[a-zA-Z])", "$1").replaceAll("([a-zA-Z])\\*(?=[a-zA-Z])", "$1")
				.replaceAll("([a-zA-Z])\\*(?=\\d)", "$1").replaceAll("\\*\\(", "(").replaceAll("\\)\\*", ")");
	}

	public int prec(char c) {
		if (c == '^')
			return 3;
		else if (c == '/' || c == '*')
			return 2;
		else if (c == '+' || c == '-')
			return 1;
		else
			return -1;
	}
}
