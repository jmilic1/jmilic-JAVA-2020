package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.dao.sql.SQLDAO;

/**
 * Singleton for providing a SQLDAO.
 * 
 * @author Jura Milić
 *
 */
public class DAOProvider {
	/**
	 * The initialized dao.
	 */
	private static DAO dao = new SQLDAO();

	/**
	 * Gets the singleton dao.
	 * 
	 * @return dao
	 */
	public static DAO getDao() {
		return dao;
	}
}