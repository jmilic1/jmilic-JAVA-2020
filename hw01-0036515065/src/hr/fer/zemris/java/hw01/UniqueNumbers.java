package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program creates ordered binary tree, adds values to it and outputs its
 * values. First ordered from smallest to largest and then ordered from largest
 * to smallest.
 * 
 * @author Jura Milić
 */
public class UniqueNumbers {
	/**
	 * Structure of tree
	 */
	public static class TreeNode {
		/**
		 * left node
		 */
		TreeNode left;

		/**
		 * right node
		 */
		TreeNode right;

		/**
		 * value of current node
		 */
		int value;
	}

	/**
	 * Method that is executed when the program starts. Values will be read until
	 * the word "kraj" is typed into console.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		TreeNode root = new TreeNode();

		while (true) {
			System.out.format("Unesite broj > ");
			if (sc.hasNextInt()) {
				int value = sc.nextInt();

				if (containsValue(root, value)) {
					System.out.println("Broj već postoji. Preskačem.");
				} else {
					root = addNode(root, value);

					System.out.println("Dodano.");
				}
			} else {
				String text = sc.next();

				if (!(text.equals("kraj"))) {
					System.out.format("'%s' nije cijeli broj.%n", text);
				} else {
					break;
				}
			}
		}
		System.out.format("Ispis od najmanjeg: ");
		inOrder(root);
		System.out.format("%nIspis od najveceg: ");
		inOrderReverse(root);

		sc.close();
	}

	/**
	 * Adds number to tree. Recursively searches for an empty node in tree and adds
	 * the value to the node's value variable.
	 * 
	 * @param node     root of tree
	 * @param newValue number we want to add
	 * @return new tree with an extra node that contains new value
	 */
	public static TreeNode addNode(TreeNode node, int newValue) {
		if (node == null) {
			node = new TreeNode();
		}

		if (node.value == 0) {
			node.value = newValue;
		} else {
			if (node.value > newValue) {
				if (node.left == null) {
					node.left = new TreeNode();
				}
				node.left = addNode(node.left, newValue);
			}
			if (node.value < newValue) {
				if (node.right == null) {
					node.right = new TreeNode();
				}
				node.right = addNode(node.right, newValue);
			}
		}
		return node;
	}

	/**
	 * Counts how many nodes tree contains. Recursively goes through all nodes and
	 * increments the final number by 1 for each one.
	 * 
	 * @param node root of tree
	 * @return number of nodes
	 */
	public static int treeSize(TreeNode node) {
		if (node == null) {
			return 0;
		}

		int numberOfNodes = 1;

		if (node.left != null) {
			numberOfNodes = numberOfNodes + treeSize(node.left);
		}

		if (node.right != null) {
			numberOfNodes = numberOfNodes + treeSize(node.right);
		}

		return numberOfNodes;
	}

	/**
	 * Checks if tree contains a value. Recursively goes through each node and
	 * checks if the current node has value.
	 * 
	 * @param node  root of tree
	 * @param value number that will be searched for
	 * @return boolean does the tree contain value
	 */
	public static boolean containsValue(TreeNode node, int value) {
		boolean contains = false;

		if (node.value == value) {
			return true;
		}

		if (node.left != null && !(contains)) {
			contains = containsValue(node.left, value);
		}

		if (node.right != null && !(contains)) {
			contains = containsValue(node.right, value);
		}

		return contains;
	}

	/**
	 * Prints the values of tree from smallest to largest. Recursively goes through
	 * each node and outputs value.
	 * 
	 * @param node root of tree
	 */
	public static void inOrder(TreeNode node) {
		if (node.left != null) {
			inOrder(node.left);
		}

		System.out.format("%d ", node.value);

		if (node.right != null) {
			inOrder(node.right);
		}
	}

	/**
	 * Prints the values of tree from largest to smallest. Recursively goes through
	 * each node and outputs value.
	 * 
	 * @param node root of tree
	 */
	public static void inOrderReverse(TreeNode node) {
		if (node.right != null) {
			inOrderReverse(node.right);
		}

		System.out.format("%d ", node.value);

		if (node.left != null) {
			inOrderReverse(node.left);
		}
	}

}
