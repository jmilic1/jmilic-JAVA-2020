package hr.fer.zemris.java.dao.sql;

import java.sql.Connection;

/**
 * Provider which allows getting a single connection with the database.
 * 
 * @author Jura Milić
 *
 */
public class SQLConnectionProvider {

	/**
	 * Collection parameters for the local thread.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Sets the given connection to provider.
	 * 
	 * @param con given connection
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Returns the connection in ThreadLocal
	 * 
	 * @return connection in ThreadLocal
	 */
	public static Connection getConnection() {
		return connections.get();
	}

}