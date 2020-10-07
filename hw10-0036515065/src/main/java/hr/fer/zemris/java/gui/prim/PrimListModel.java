package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Class which represents an implementation of ListModel which adds prime
 * numbers to it's list.
 * 
 * @author Jura Milić
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/**
	 * List of prime numbers
	 */
	private List<Integer> prims;
	/**
	 * List of listeners listening to the PrimListModel
	 */
	private List<ListDataListener> listeners;

	/**
	 * Constructs a PrimListModel with the number 1 as it's first element.
	 */
	public PrimListModel() {
		prims = new ArrayList<Integer>();
		listeners = new ArrayList<ListDataListener>();
		prims.add(Integer.valueOf(1));
	}

	@Override
	public int getSize() {
		return prims.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return prims.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	/**
	 * Adds the next prime number to the list and notifies the listeners
	 */
	public void next() {
		Integer current = prims.get(prims.size() - 1);
		Integer newPrim = null;

		newPrim = current;
		boolean found = false;
		while (!found) {
			newPrim++;
			for (int i = 2; i <= newPrim; i++) {
				if (i == newPrim) {
					found = true;
				} else {
					if (newPrim % i == 0) {
						break;
					}
				}

			}
		}

		prims.add(newPrim);

		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, newPrim, newPrim);
		for (ListDataListener l : listeners) {
			l.intervalAdded(event);
		}
	}

}
