package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface for basic listeners of a single document in JNotepad++.
 * 
 * @author Jura Milić
 *
 */
public interface SingleDocumentListener {
	/**
	 * This method is executed whenever the given document was modified.
	 * 
	 * @param model given document
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * This method is executed whenever the given document's path was changed.
	 * 
	 * @param model given document
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}