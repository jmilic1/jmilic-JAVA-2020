package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface for a simple listener which observes the current document, whether
 * a document was added or whether a document was removed in JNotepad++.
 * 
 * @author Jura Milić
 *
 */
public interface MultipleDocumentListener {
	/**
	 * This method should be executed whenever the current document was swapped.
	 * 
	 * @param previousModel previous document
	 * @param currentModel  new current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * This method should be executed whenever a new document was added.
	 * 
	 * @param model added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * This method should be executed whenever a document was removed.
	 * 
	 * @param model removed document
	 */
	void documentRemoved(SingleDocumentModel model);
}
