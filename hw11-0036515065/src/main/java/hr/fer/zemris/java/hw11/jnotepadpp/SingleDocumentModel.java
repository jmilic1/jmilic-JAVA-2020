package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Interface for a user-modifiable document used in JNotepad++.
 * 
 * @author Jura Milić
 *
 */
public interface SingleDocumentModel {
	/**
	 * @return text of this document
	 */
	JTextArea getTextComponent();

	/**
	 * @return path of this document
	 */
	Path getFilePath();

	/**
	 * Sets document's file path. Implementations of this method should notify all
	 * registered listeners that the path was changed.
	 * 
	 * @param path new path for document
	 */
	void setFilePath(Path path);

	/**
	 * Checks if document contains an unsaved modification.
	 * 
	 * @return true if modified, false otherwise
	 */
	boolean isModified();

	/**
	 * Sets document's modified flag. Implementations of this method should notify
	 * all registered listeners if the flag was set.
	 * 
	 * @param modified new flag status
	 */
	void setModified(boolean modified);

	/**
	 * Adds a SingleDocumentListener to this document's collection of listeners.
	 * 
	 * @param l listener to be added
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Removes a SingleDocumentListener from this document's collection of
	 * listeners.
	 * 
	 * @param l listener to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
