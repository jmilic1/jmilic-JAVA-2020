package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Interface for modeling multiple documents in JNotepad++. Implementations of
 * this model must have registered listeners and a reference to the current
 * document.
 * 
 * @author Jura Milić
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Creates new document in model
	 * 
	 * @return fresh new document
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * @return current document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Load a document into the model from given path.
	 * 
	 * @param path given path
	 * @return loaded document
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Save a given document with the given path
	 * 
	 * @param model   given document
	 * @param newPath given save path for document
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Closes a given document and removes it from the model
	 * 
	 * @param model given document
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Register given listener to this model
	 * 
	 * @param l given listener
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Unregister given listener from model
	 * 
	 * @param l given listener
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Returns the number of documents currently inside the model
	 * 
	 * @return number of documents
	 */
	int getNumberOfDocuments();

	/**
	 * Finds the document at given index
	 * 
	 * @param index given index
	 * @return found document
	 */
	SingleDocumentModel getDocument(int index);
}
