package hr.fer.zemris.java.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.dao.DAOException;

/**
 * Provider which can provide an instance of EntityManager to each thread.
 */
public class JPAEMProvider {

	/**
	 * An assortment of thread-empty variables.
	 */
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Returns an entity manager for the thread.
	 * 
	 * @return entity manager
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if (em == null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Closes the entity manager of this thread and removes it from thread-local
	 * variables.
	 * 
	 * @throws DAOException thrown if transaction could not be commited
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if (em == null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null)
			throw dex;
	}

}