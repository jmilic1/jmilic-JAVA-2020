package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program demo which demonstrates a PrimListModel to show a certain number of
 * prime numbers. There are two Prime Number Lists which are updated
 * simultaneously. By clicking the button "sljedeći" both lists are updated.
 * 
 * @author Jura Milić
 *
 */
public class PrimDemo extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a PrimDemo
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}

	/**
	 * Initializes the gui for PrimDemo
	 */
	private void initGUI() {
		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());

		PrimListModel listModel = new PrimListModel();

		JList<Integer> list1 = new JList<Integer>(listModel);
		JList<Integer> list2 = new JList<Integer>(listModel);

		JScrollPane pane1 = new JScrollPane(list1);
		JScrollPane pane2 = new JScrollPane(list2);

		JPanel panel = new JPanel();
		cont.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout());

		panel.add(pane1, 0, 0);
		panel.add(pane2, 0, 1);

		JButton button = new JButton("sljdedeći");

		button.addActionListener(e -> listModel.next());
		cont.add(button, BorderLayout.SOUTH);
	}

	/**
	 * Start of program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrimDemo().setVisible(true);
		});
	}
}
