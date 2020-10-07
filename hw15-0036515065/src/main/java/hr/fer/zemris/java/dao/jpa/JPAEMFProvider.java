package hr.fer.zemris.java.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * EntityManagerFactory provider
 *
 */
public class JPAEMFProvider {

	/**
	 * The entity manager factory.
	 */
	public static EntityManagerFactory emf;

	/**
	 * Get the entity manager factory.
	 * 
	 * @return EntityManagerFactory
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Set the entity manager factory
	 * 
	 * @param emf given EntityManagerFactory
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}