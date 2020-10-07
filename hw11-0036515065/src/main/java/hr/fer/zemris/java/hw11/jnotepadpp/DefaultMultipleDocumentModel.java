package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Default implementation for MultipleDocumentModel made possible with
 * JTabbedPane. All of the documents have one tab assorted to them. Model tracks
 * all of the documents, registered listeners and the current document.
 * 
 * @author Jura Milić
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	private static final long serialVersionUID = 1L;
	/**
	 * Default name for new and unnamed files.
	 */
	private static final String DEFAULT_FILE_NAME = "unnamed";

	/**
	 * List of documents
	 */
	private List<SingleDocumentModel> docs;
	/**
	 * Registered listeners
	 */
	private List<MultipleDocumentListener> listeners;
	/**
	 * Reference to the current document
	 */
	private SingleDocumentModel currentDoc;

	/**
	 * Constructs a DefaultMultipleDocumentModel and registers a change listener to
	 * the tabs which notifies all of the registered listeners that the current
	 * document was swapped with another document.
	 */
	public DefaultMultipleDocumentModel() {
		docs = new ArrayList<SingleDocumentModel>();
		listeners = new ArrayList<MultipleDocumentListener>();
		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				SingleDocumentModel oldDoc = currentDoc;
				currentDoc = docs.get(getSelectedIndex());

				for (MultipleDocumentListener listener : listeners) {
					listener.currentDocumentChanged(oldDoc, currentDoc);
				}
			}
		});
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return docs.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return openDocument(null, "");
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDoc;
	}

	/**
	 * @throws NullPointerException if given path was null
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if (path == null) {
			throw new NullPointerException("Given path was null!");
		}

		for (SingleDocumentModel doc : docs) {
			if (doc.getFilePath() == null) {
				continue;
			}
			if (doc.getFilePath().equals(path)) {
				currentDoc = doc;
				setSelectedIndex(docs.indexOf(currentDoc));
				return doc;
			}
		}

		String text;
		try {
			text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to read file!");
		}

		return openDocument(path, text);
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if (newPath != null) {
			for (SingleDocumentModel doc : docs) {
				if (!model.equals(doc) && newPath.equals(doc.getFilePath())) {
					throw new IllegalArgumentException("Unable to save file!");
				}
			}
		}

		Path oldPath = model.getFilePath();

		if (newPath != null) {
			model.setFilePath(newPath);

			if (oldPath != null) {
				oldPath.toFile().delete();
			}
		}
		byte[] data = model.getTextComponent().getText().getBytes();

		try {
			Files.write(model.getFilePath(), data);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to write to file!");
		}

		setIconAt(docs.indexOf(model), findIcon("icons/green-plus.png"));
		model.setModified(false);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {

		if (docs.size() == 1) {
			createNewDocument();
		}
		int index = docs.indexOf(model);
		docs.remove(index);

		removeTabAt(index);
		currentDoc = docs.get(0);

		for (MultipleDocumentListener listener : listeners) {
			listener.documentRemoved(model);
		}
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return docs.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return docs.get(index);
	}

	/**
	 * Opens a document from given path with given document text. If the document
	 * was not already opened, a listener is made and registered to the new
	 * document.
	 * 
	 * @param path given path
	 * @param text given text
	 * @return opened document
	 */
	private SingleDocumentModel openDocument(Path path, String text) {
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, text);
		docs.add(newDoc);

		JScrollPane sp = new JScrollPane(newDoc.getTextComponent());

		if (path == null) {
			addTab(DEFAULT_FILE_NAME, sp);
			setToolTipTextAt(docs.indexOf(newDoc), DEFAULT_FILE_NAME);
		} else {
			addTab(newDoc.getFilePath().getFileName().toString(), sp);
			setToolTipTextAt(docs.indexOf(newDoc), newDoc.getFilePath().toString());
		}
		setSelectedIndex(docs.indexOf(newDoc));
		setIconAt(docs.indexOf(currentDoc), findIcon("icons/green-plus.png"));

		currentDoc.addSingleDocumentListener(new SingleDocumentListener() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				int index = docs.indexOf(model);
				if (model.isModified()) {
					setIconAt(index, findIcon("icons/red-minus.png"));
				} else {
					setIconAt(index, findIcon("icons/green-plus.png"));
				}
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				int index = docs.indexOf(model);
				if (model.getFilePath() == null) {
					setTitleAt(index, DEFAULT_FILE_NAME);
					setToolTipTextAt(index, DEFAULT_FILE_NAME);
				} else {
					setTitleAt(index, model.getFilePath().getFileName().toString());
					setToolTipTextAt(index, model.getFilePath().toString());
				}
			}
		});

		for (MultipleDocumentListener listener : listeners) {
			listener.documentAdded(currentDoc);
		}
		return currentDoc;
	}

	/**
	 * Returns an icon from given path
	 * 
	 * @param path given path
	 * @return found icon
	 */
	private ImageIcon findIcon(String path) {
		InputStream is = this.getClass().getResourceAsStream(path);
		if (is == null) {
			throw new NullPointerException();
		}
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to read icon!");
		}
		return new ImageIcon(bytes);
	}
}
