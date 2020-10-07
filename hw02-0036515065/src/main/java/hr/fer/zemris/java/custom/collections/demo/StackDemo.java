package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

public class StackDemo {

	public static void main(String[] args) {
		String expression = args[0];
		ObjectStack stack = new ObjectStack();

		int i = 0;
		int a;
		int b;
		while (i < expression.length()) {
			char c = expression.charAt(i);
			if (c != ' ') {
				if (expression.length() == i + 1) {
					i++;
					try {
						b = (int) stack.pop();
						a = (int) stack.pop();
					} catch (EmptyStackException ex) {
						System.out.println("Given expression is invalid.");
						return;
					}
					switch (c) {
					case '-':
						stack.push(a - b);
						break;
					case '+':
						stack.push(a + b);
						break;
					case '/':
						if (b == 0) {
							System.out.println("Can not divibe by zero.");
							return;
						}
						stack.push(a / b);
						break;
					case '*':
						stack.push(a * b);
						break;
					case '%':
						stack.push(a % b);
						break;
					default:
						System.out.println("Given expression is invalid");
						return;
					}
				} else {
					if (expression.charAt(i + 1) == ' ') {
						try {
							stack.push(Integer.parseInt(String.valueOf(c)));
						} catch (NumberFormatException ex) {
							try {
								b = (int) stack.pop();
								a = (int) stack.pop();
							} catch (EmptyStackException exc) {
								System.out.println("Given expression is invalid");
								return;
							}
							switch (c) {
							case '-':
								stack.push(a - b);
								break;
							case '+':
								stack.push(a + b);
								break;
							case '/':
								if (b == 0) {
									System.out.println("Can not divibe by zero.");
									return;
								}
								stack.push(a / b);
								break;
							case '*':
								stack.push(a * b);
								break;
							case '%':
								stack.push(a % b);
								break;
							default:
								System.out.println("Given expression is invalid");
								return;
							}
						}
						i++;
					} else {
						int j = 0;
						StringBuilder sb = new StringBuilder();
						while (expression.charAt(i + j) != ' ') {
							sb.append(expression.charAt(i + j));
							j++;
						}
						try {
							stack.push(Integer.parseInt(sb.toString()));
						} catch (NumberFormatException ex) {
							System.out.println("Given expression is invalid.");
							return;
						}
						i = i + j;
					}
				}
			} else {
				i++;
			}
		}

		System.out.printf("Expression evaluates to %d.\n", stack.pop());
	}
}
