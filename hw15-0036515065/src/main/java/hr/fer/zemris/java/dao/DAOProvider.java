package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.dao.jpa.JPADAOImpl;

/**
 * Singleton which provides a DAO implementation.
 *
 */
public class DAOProvider {

	/**
	 * DAO which is initialized only once
	 */
	private static DAO dao = new JPADAOImpl();

	/**
	 * Returns the dao
	 * 
	 * @return dao
	 */
	public static DAO getDAO() {
		return dao;
	}
}