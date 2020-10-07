package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Default implementation for SingleDocumentModel. This implementation models a
 * single document with it's text visible and editable on JTextArea.
 * 
 * @author Jura Milić
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/**
	 * JTextArea of this document
	 */
	private JTextArea jArea;
	/**
	 * Path of this document
	 */
	private Path path;
	/**
	 * Modified flag
	 */
	private boolean modified;
	/**
	 * Listeners registered to this document
	 */
	private List<SingleDocumentListener> listeners;

	/**
	 * Constructs a DefaultSingleDocumentModel with given path and text. A document
	 * listener is added to JTextArea which sets modified flag if a change occured.
	 * 
	 * @param path given path
	 * @param text given text
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		this.path = path;
		jArea = new JTextArea(text);
		listeners = new ArrayList<SingleDocumentListener>();
		jArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(false);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return jArea;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	/**
	 * @throws NullPointerException if given path was null
	 */
	@Override
	public void setFilePath(Path path) {
		if (path == null) {
			throw new NullPointerException("Given path was null!");
		}

		this.path = path;

		for (SingleDocumentListener listener : listeners) {
			listener.documentFilePathUpdated(this);
		}
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;

		if (isModified()) {
			for (SingleDocumentListener listener : listeners) {
				listener.documentModifyStatusUpdated(this);
			}
		}
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
}
