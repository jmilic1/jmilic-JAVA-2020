package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.JToolBar;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * Program which offers basic notepad functionality.
 * 
 * @author Jura Milić
 *
 */
public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Default title of program
	 */
	private static final String TITLE = "JNotepad++";

	/**
	 * JTabbedPane model for viewing documents.
	 */
	private MultipleDocumentModel docs;
	/**
	 * Provider of localization
	 */
	private FormLocalizationProvider provider;

	/**
	 * Length of current document
	 */
	private JLabel length;
	/**
	 * Basic info of current document.
	 */
	private JLabel info;
	/**
	 * Current time
	 */
	private JLabel time;

	/**
	 * Clock for updating the time label
	 */
	private Timer clock;

	/**
	 * Creates a new blank document.
	 */
	private Action newDocument;
	/**
	 * Opens a document.
	 */
	private Action openDocument;
	/**
	 * Saves a document
	 */
	private Action saveDocument;
	/**
	 * Saves a document as a new path
	 */
	private Action saveAsDocument;
	/**
	 * Closes document.
	 */
	private Action closeDocument;
	/**
	 * Cuts document
	 */
	private Action cutDocument;
	/**
	 * Copies document
	 */
	private Action copyDocument;
	/**
	 * Pastes into document
	 */
	private Action pasteDocument;
	/**
	 * Shows info about document
	 */
	private Action infoDocument;
	/**
	 * Exits the program
	 */
	private Action exit;

	/**
	 * Selected text is changed to upper case
	 */
	private Action upper;
	/**
	 * Selected text is changed to lower case
	 */
	private Action lower;
	/**
	 * Selected text has it's case inverted
	 */
	private Action invert;

	/**
	 * Sorts the selected lines in ascending order.
	 */
	private Action ascend;
	/**
	 * Sorts the selected lines in descending order
	 */
	private Action descend;
	/**
	 * Removes selected lines which are copies so only one remains.
	 */
	private Action unique;

	/**
	 * Constructs a JNotepad++
	 */
	public JNotepadPP() {
		setLocation(0, 0);
		setSize(1000, 600);
		setTitle(TITLE);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				for (SingleDocumentModel doc : docs) {
					if (doc.isModified()) {
						Path path = doc.getFilePath();
						int answer = -1;
						if (path == null) {
							answer = JOptionPane.showConfirmDialog(JNotepadPP.this,
									provider.getString("close_unsaved") + "unnamed?", TITLE,
									JOptionPane.YES_NO_CANCEL_OPTION);
						} else {
							answer = JOptionPane.showConfirmDialog(JNotepadPP.this,
									provider.getString("close_unsaved") + path.getFileName().toString() + "?", TITLE,
									JOptionPane.YES_NO_CANCEL_OPTION);
						}

						if (answer == JOptionPane.YES_OPTION) {
							saveDocument.actionPerformed(null);
						}
						if (answer == JOptionPane.NO_OPTION) {
							continue;
						}
						if (answer == JOptionPane.CANCEL_OPTION) {
							return;
						}
					}
				}

				clock.cancel();
				dispose();
			}
		});

		initGUI();
	}

	/**
	 * Initializes the gui for JNotepad++
	 */
	private void initGUI() {
		DefaultMultipleDocumentModel model = new DefaultMultipleDocumentModel();
		docs = model;

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		Container container = new Container();
		container.setLayout(new BorderLayout());
		container.add(model, BorderLayout.CENTER);

		cp.add(container, BorderLayout.CENTER);

		provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		clock = new Timer(true);
		clock.schedule(new TimerTask() {

			private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			@Override
			public void run() {
				Date now = new Date();

				String str = format.format(now);
				time.setText(str);
			}
		}, 100, 1 * 1000);

		createActions();
		createMenus();
		createToolbars();

		docs.addMultipleDocumentListener(new MultipleDocumentListener() {

			private CaretListener caretListener = new CaretListener() {

				@Override
				public void caretUpdate(CaretEvent e) {
					updateStats();
				}
			};

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				updateStats();
				updateTitle();
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				if (docs.getCurrentDocument().equals(model)) {
					model.getTextComponent().addCaretListener(caretListener);
				}
				updateTitle();
				updateStats();
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (previousModel == null && currentModel == null) {
					throw new NullPointerException("PreviousModel and CurrentModel were both Null!");
				}
				if (previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(caretListener);
				}
				currentModel.getTextComponent().addCaretListener(caretListener);
				updateStats();
				updateTitle();
			}
		});

		docs.createNewDocument();
	}

	/**
	 * Creates the menu bar.
	 */
	private void createMenus() {
		JMenuBar menubar = new JMenuBar();

		LJMenu menu = new LJMenu("file", provider);
		JMenuItem item = new JMenuItem(newDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control N"));
		item.setMnemonic(KeyEvent.VK_N);
		menu.add(item);

		item = new JMenuItem(openDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control O"));
		item.setMnemonic(KeyEvent.VK_O);
		menu.add(item);

		item = new JMenuItem(saveDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control S"));
		item.setMnemonic(KeyEvent.VK_S);
		menu.add(item);

		item = new JMenuItem(saveAsDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control alt S"));
		item.setMnemonic(KeyEvent.VK_S);
		menu.add(item);

		item = new JMenuItem(closeDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control W"));
		item.setMnemonic(KeyEvent.VK_W);
		menu.add(item);

		item = new JMenuItem(infoDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control I"));
		item.setMnemonic(KeyEvent.VK_I);
		menu.add(item);

		item = new JMenuItem(exit);
		item.setAccelerator(KeyStroke.getKeyStroke("control E"));
		item.setMnemonic(KeyEvent.VK_E);
		menu.add(item);
		menubar.add(menu);

		menu = new LJMenu("edit", provider);
		item = new JMenuItem(cutDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control X"));
		item.setMnemonic(KeyEvent.VK_X);
		menu.add(item);

		item = new JMenuItem(copyDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control C"));
		item.setMnemonic(KeyEvent.VK_C);
		menu.add(item);

		item = new JMenuItem(pasteDocument);
		item.setAccelerator(KeyStroke.getKeyStroke("control V"));
		item.setMnemonic(KeyEvent.VK_V);
		menu.add(item);
		menubar.add(menu);

		menu = new LJMenu("languages", provider);
		JMenuItem menuItem = new JMenuItem("hr");
		menuItem.addActionListener(a -> LocalizationProvider.getInstance().setLanguage("hr"));
		menu.add(menuItem);

		menuItem = new JMenuItem("en");
		menuItem.addActionListener(a -> LocalizationProvider.getInstance().setLanguage("en"));
		menu.add(menuItem);

		menuItem = new JMenuItem("de");
		menuItem.addActionListener(a -> LocalizationProvider.getInstance().setLanguage("de"));
		menu.add(menuItem);

		menubar.add(menu);

		menu = new LJMenu("toolbar", provider);
		LJMenu subMenu = new LJMenu("chCase", provider);
		item = new JMenuItem(upper);
		subMenu.add(item);

		item = new JMenuItem(lower);
		subMenu.add(item);

		item = new JMenuItem(invert);
		subMenu.add(item);
		menu.add(subMenu);

		subMenu = new LJMenu("sort", provider);
		item = new JMenuItem(ascend);
		subMenu.add(item);

		item = new JMenuItem(descend);
		subMenu.add(item);
		menu.add(subMenu);

		item = new JMenuItem(unique);
		menu.add(item);

		menubar.add(menu);

		setJMenuBar(menubar);
	}

	/**
	 * Creates actions
	 */
	private void createActions() {
		newDocument = new LocalizableAction("new", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				docs.createNewDocument();
				updateStats();
			}
		};

		openDocument = new LocalizableAction("open", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(provider.getString("open_title"));

				if (fc.showOpenDialog(JNotepadPP.this) == JFileChooser.APPROVE_OPTION) {
					Path path = fc.getSelectedFile().toPath();

					docs.loadDocument(path);
					updateStats();
				}
			}
		};

		saveDocument = new LocalizableAction("save", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (docs.getCurrentDocument().getFilePath() == null) {
					saveAsDocument.actionPerformed(e);
				} else {
					docs.saveDocument(docs.getCurrentDocument(), null);
					updateTitle();
					updateStats();
				}
			}
		};

		saveAsDocument = new LocalizableAction("saveAs", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(provider.getString("saveAs_title"));

				while (true) {
					int answer = fc.showSaveDialog(JNotepadPP.this);
					if (answer == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();

						boolean found = false;
						if (file.exists()) {
							found = true;
							answer = JOptionPane.showConfirmDialog(JNotepadPP.this,
									provider.getString("overwrite_question"), provider.getString("overwrite_title"),
									JOptionPane.YES_NO_OPTION);
							if (answer == JOptionPane.OK_OPTION) {
								saveHelper(file.toPath());
								return;
							}
							if (answer == JOptionPane.NO_OPTION) {
								return;
							}
						}

						if (!found) {
							saveHelper(file.toPath());
							return;
						}
					}
					if (answer == JFileChooser.CANCEL_OPTION) {
						return;
					}
				}
			}

			private void saveHelper(Path path) {
				docs.saveDocument(docs.getCurrentDocument(), path);
				updateTitle();
				updateStats();
			}
		};

		closeDocument = new LocalizableAction("close", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (docs.getCurrentDocument().isModified()) {
					saveDocument.actionPerformed(e);
				}

				docs.closeDocument(docs.getCurrentDocument());
				updateStats();
			}
		};

		cutDocument = new LocalizableAction("cut", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Action cut = new DefaultEditorKit.CutAction();

				cut.actionPerformed(e);
			}
		};

		copyDocument = new LocalizableAction("copy", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Action copy = new DefaultEditorKit.CopyAction();

				copy.actionPerformed(e);
			}
		};

		pasteDocument = new LocalizableAction("paste", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Action paste = new DefaultEditorKit.PasteAction();

				paste.actionPerformed(e);
			}
		};

		infoDocument = new LocalizableAction("info", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = docs.getCurrentDocument().getTextComponent().getText();

				int length = 0;
				int nonLength = 0;
				int lines = 0;

				for (char c : text.toCharArray()) {
					length++;
					if (c != ' ' && c != '\t' && c != '\n') {
						nonLength++;
					}
					if (c == '\n') {
						lines++;
					}
				}

				if (length != 0) {
					lines++;
				}

				JOptionPane.showMessageDialog(JNotepadPP.this,
						provider.getString("your_doc") + length + " " + provider.getString("chars") + nonLength + " "
								+ provider.getString("non_blank") + lines + " " + provider.getString("lines"));
			}
		};

		exit = new LocalizableAction("exit", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JNotepadPP.this.dispatchEvent(new WindowEvent(JNotepadPP.this, WindowEvent.WINDOW_CLOSING));
			}
		};

		upper = new LocalizableAction("upper", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextComponent jArea = docs.getCurrentDocument().getTextComponent();
				Document document = jArea.getDocument();

				Caret caret = jArea.getCaret();

				int len = Math.abs(caret.getMark() - caret.getDot());
				int offset = Math.min(caret.getMark(), caret.getDot());

				String text;
				try {
					text = document.getText(offset, len);
					document.remove(offset, len);
					document.insertString(offset, text.toUpperCase(), null);
				} catch (BadLocationException e1) {
					throw new IllegalArgumentException("Unable to change document!");
				}
				updateStats();
			}
		};

		lower = new LocalizableAction("lower", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextComponent jArea = docs.getCurrentDocument().getTextComponent();
				Document document = jArea.getDocument();

				Caret caret = jArea.getCaret();

				int len = Math.abs(caret.getMark() - caret.getDot());
				int offset = Math.min(caret.getMark(), caret.getDot());

				String text;
				try {
					text = document.getText(offset, len);
					document.remove(offset, len);
					document.insertString(offset, text.toLowerCase(), null);
				} catch (BadLocationException e1) {
					throw new IllegalArgumentException("Unable to change document!");
				}
				updateStats();
			}
		};

		invert = new LocalizableAction("invert", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextComponent jArea = docs.getCurrentDocument().getTextComponent();
				Document document = jArea.getDocument();

				Caret caret = jArea.getCaret();

				int len = Math.abs(caret.getMark() - caret.getDot());
				int offset = Math.min(caret.getMark(), caret.getDot());

				String text;
				try {
					text = document.getText(offset, len);
					document.remove(offset, len);

					StringBuilder sb = new StringBuilder();
					for (char c : text.toCharArray()) {
						if (Character.isUpperCase(c)) {
							sb.append(Character.toLowerCase(c));
						} else {
							if (Character.isLowerCase(c)) {
								sb.append(Character.toUpperCase(c));
							} else {
								sb.append(c);
							}
						}
					}

					document.insertString(offset, sb.toString(), null);
				} catch (BadLocationException e1) {
					throw new IllegalArgumentException("Unable to change document!");
				}

				updateStats();
			}
		};

		ascend = new LocalizableAction("ascend", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sort(false);
			}
		};

		descend = new LocalizableAction("descend", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sort(true);
			}
		};

		unique = new LocalizableAction("unique", provider) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea jArea = docs.getCurrentDocument().getTextComponent();
				int start = jArea.getCaretPosition();
				int end = jArea.getCaret().getMark();

				if (start > end) {
					int temp = end;
					end = start;
					start = temp;
				}

				int index = start;
				char[] data = jArea.getText().toCharArray();
				while (index > 0) {
					if (data[index] == '\n') {
						break;
					}
					index--;
				}

				if (index == 0) {
					start = index;
				} else {
					start = index + 1;
				}

				index = end;
				while (index < data.length) {
					if (data[index] == '\n') {
						break;
					}
					index++;
				}

				end = index;

				String str = "";
				try {
					str = jArea.getText(start, end - start);
				} catch (BadLocationException ex) {
					throw new IllegalArgumentException("Unable to read text!");
				}

				String[] lines = str.split("\\r?\\n");

				List<String> newLines = new ArrayList<String>();
				for (String line : lines) {
					if (!newLines.contains(line)) {
						newLines.add(line);
					}
				}

				Document document = docs.getCurrentDocument().getTextComponent().getDocument();

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < newLines.size() - 1; i++) {
					sb.append(newLines.get(i)).append('\n');
				}
				if (newLines.size() != 0) {
					sb.append(newLines.get(newLines.size() - 1));
				}

				try {
					document.remove(start, end - start);
					document.insertString(start, sb.toString(), null);
				} catch (BadLocationException e1) {
					throw new IllegalArgumentException("Unable to remove from document!");
				}
			}
		};
	}

	/**
	 * Creates the tool bar and status bar
	 * 
	 * @par
	 */
	private void createToolbars() {
		JToolBar toolbar = new JToolBar(provider.getString("toolbar"));
		toolbar.setFloatable(true);

		getContentPane().add(toolbar, BorderLayout.PAGE_START);

		toolbar.add(new JButton(newDocument));
		toolbar.add(new JButton(openDocument));
		toolbar.add(new JButton(saveDocument));
		toolbar.add(new JButton(saveAsDocument));
		toolbar.add(new JButton(closeDocument));
		toolbar.add(new JButton(cutDocument));
		toolbar.add(new JButton(copyDocument));
		toolbar.add(new JButton(pasteDocument));
		toolbar.add(new JButton(infoDocument));
		toolbar.add(new JButton(exit));

		JToolBar statusbar = new JToolBar(provider.getString("statusbar"));
		statusbar.setFloatable(true);
		getContentPane().add(statusbar, BorderLayout.PAGE_END);

		statusbar.setLayout(new BorderLayout());

		length = new JLabel();
		info = new JLabel();
		time = new JLabel();

		statusbar.add(length, BorderLayout.WEST);

		JPanel panel = new JPanel();
		panel.add(info);
		panel.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
		statusbar.add(panel, BorderLayout.CENTER);

		statusbar.add(time, BorderLayout.EAST);

		updateStats();
	}

	/**
	 * Updates the basic stats to be show in status bar
	 */
	private void updateStats() {
		try {
			length.setText("Length: "
					+ Integer.toString(docs.getCurrentDocument().getTextComponent().getText().length()) + " ");
		} catch (NullPointerException ex) {
			length.setText("Length: " + 1 + " ");
		}

		int line = 1;
		int column = 1;
		int selection = 0;
		try {
			JTextArea jArea = docs.getCurrentDocument().getTextComponent();
			String text = jArea.getText();

			for (int i = 0; i < jArea.getCaretPosition(); i++) {
				column++;
				if (text.charAt(i) == '\n') {
					line++;
					column = 1;
				}
			}

			selection = Math.abs(jArea.getCaretPosition() - jArea.getCaret().getMark());
		} catch (NullPointerException ex) {
		}

		if (selection != 0) {
			enableTools();
		} else {
			disableTools();
		}
		info.setText("Ln: " + line + " Col: " + column + " Sel: " + selection);
	}

	/**
	 * Updates the title of window
	 */
	private void updateTitle() {
		Path path = docs.getCurrentDocument().getFilePath();
		if (path == null) {
			setTitle(TITLE);
		} else {
			setTitle(docs.getCurrentDocument().getFilePath().toString() + " - " + TITLE);
		}
	}

	/**
	 * Enables tools which require selected text to work.
	 */
	private void enableTools() {
		upper.setEnabled(true);
		lower.setEnabled(true);
		invert.setEnabled(true);
		ascend.setEnabled(true);
		descend.setEnabled(true);
		unique.setEnabled(true);
	}

	/**
	 * Disables tools which require selected text to work.
	 */
	private void disableTools() {
		upper.setEnabled(false);
		lower.setEnabled(false);
		invert.setEnabled(false);
		ascend.setEnabled(false);
		descend.setEnabled(false);
		unique.setEnabled(false);
	}

	/**
	 * Sorts the selected lines in order according to given boolean.
	 * 
	 * @param descending if true descending order is used, ascending is used
	 *                   otherwise
	 */
	private void sort(boolean descending) {
		JTextArea jArea = docs.getCurrentDocument().getTextComponent();
		int start = jArea.getCaretPosition();
		int end = jArea.getCaret().getMark();

		if (start > end) {
			int temp = end;
			end = start;
			start = temp;
		}

		int index = start;
		char[] data = jArea.getText().toCharArray();
		while (index > 0) {
			if (data[index] == '\n') {
				break;
			}
			index--;
		}
		if (index == 0) {
			start = index;
		} else {
			start = index + 1;
		}

		index = end;
		while (index < data.length) {
			if (data[index] == '\n') {
				break;
			}
			index++;
		}

		end = index;

		String str = "";
		try {
			str = jArea.getText(start, end - start);
		} catch (BadLocationException ex) {
			throw new IllegalArgumentException("Unable to read text!");
		}

		String[] lines = str.split("\\r?\\n");

		Locale locale = new Locale(provider.getLanguage());
		Comparator<Object> comparator = null;
		Collator collator = Collator.getInstance(locale);

		if (descending) {
			comparator = collator.reversed();
		} else {
			comparator = collator;
		}

		for (int i = 0; i < lines.length; i++) {
			String temp = lines[i];
			int in = i;
			for (int j = i + 1; j < lines.length; j++) {
				if (comparator.compare(temp, lines[j]) < 0) {
					temp = lines[j];
					in = j;
				}
			}
			if (in != i) {
				lines[in] = lines[i];
				lines[i] = temp;
			}
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < lines.length - 1; i++) {
			sb.append(lines[i]).append('\n');
		}
		sb.append(lines[lines.length - 1]);

		Document document = docs.getCurrentDocument().getTextComponent().getDocument();

		try {
			document.remove(start, end - start);
			document.insertString(start, sb.toString(), null);
		} catch (BadLocationException e1) {
			throw new IllegalArgumentException("Unable to remove from document!");
		}
	}

	/**
	 * Start of program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}
}