package hr.fer.zemris.java.dbwebapp;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Initializes the database for server by creating table Polls and table
 * PollOptions if necessary. Tables are created if they do not exist and they
 * are populated if table Polls is empty.
 * 
 * @author Jura Milić
 *
 */
@WebListener
public class Initialize implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		String fileName = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");

		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(fileName)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not load properties!");
		}

		String host = properties.getProperty("host");
		Integer port = Integer.valueOf(properties.getProperty("port"));
		String dbName = properties.getProperty("name");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");

		String connectionURL = "jdbc:derby://" + host + ":" + port + "/" + dbName + ";user=" + user + ";password="
				+ password;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error while initializing database!", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		initializeDatabase(cpds, sce);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initializes the database.
	 * 
	 * @param cpds given CombpPooledDataSource
	 * @param sce  given ServletContextEvent
	 */
	private void initializeDatabase(ComboPooledDataSource cpds, ServletContextEvent sce) {
		try (Connection connection = cpds.getConnection()) {

			DatabaseMetaData dbmd = connection.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, "%", null);
			boolean pollsExist = false;
			boolean pollOptionsExist = false;
			while (rs.next()) {
				if (rs.getString(3).equals("Polls".toUpperCase())) {
					pollsExist = true;
				}
				if (rs.getString(3).equals("PollOptions".toUpperCase())) {
					pollOptionsExist = true;
				}
			}

			PreparedStatement pst = null;
			if (!pollsExist) {
				pst = connection.prepareStatement(
						"CREATE TABLE Polls (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(150) NOT NULL, message CLOB(2048) NOT NULL)");
				pst.execute();
			}

			if (!pollOptionsExist) {
				pst = connection.prepareStatement("CREATE TABLE PollOptions"
						+ " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
						+ " optionTitle VARCHAR(100) NOT NULL," + " optionLink VARCHAR(150) NOT NULL,"
						+ " pollID BIGINT," + " votesCount BIGINT," + " FOREIGN KEY (pollID) REFERENCES Polls(id))");
				pst.execute();
			}

			rs = connection.prepareStatement("SELECT * FROM Polls").executeQuery();
			if (!rs.next()) {
				pst = connection.prepareStatement(
						"INSERT INTO POLLS (title, message) VALUES ('Glasanje za omiljeni bend', 'Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!')",
						Statement.RETURN_GENERATED_KEYS);
				pst.execute();

				ResultSet keys = pst.getGeneratedKeys();
				keys.next();

				populateOptionsTable(keys.getLong(1),
						sce.getServletContext().getRealPath("/WEB-INF/glasanje-definicija-bendovi.txt"), connection);

				pst = connection.prepareStatement(
						"INSERT INTO POLLS (title, message) VALUES ('Glasanje za omiljeni film', 'Od sljedećih filmova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!')",
						Statement.RETURN_GENERATED_KEYS);
				pst.execute();

				keys = pst.getGeneratedKeys();
				keys.next();

				populateOptionsTable(keys.getLong(1),
						sce.getServletContext().getRealPath("/WEB-INF/glasanje-definicija-filmovi.txt"), connection);
			}
		}

		catch (SQLException | IOException ex) {
			throw new RuntimeException("Error while initializing database! " + ex.getMessage());
		}
	}

	/**
	 * Populates the data in OptionsTable
	 * 
	 * @param pollID     given pollID
	 * @param definition given path to definition of options
	 * @param connection given connection
	 * @throws IOException  if file could not be read
	 * @throws SQLException if rows could not be added to database
	 */
	private void populateOptionsTable(long pollID, String definition, Connection connection)
			throws IOException, SQLException {
		List<String> entities = Files.readAllLines(Paths.get(definition));

		for (String entity : entities) {
			String[] attributes = entity.split("\t");

			PreparedStatement pst = connection.prepareStatement(
					"INSERT INTO PollOptions (OptionTitle, optionLink, pollID, votesCount) VALUES" + "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			pst.setString(1, attributes[1]);
			pst.setString(2, attributes[2]);
			pst.setLong(3, pollID);
			pst.setLong(4, 0);

			pst.executeUpdate();
		}
	}
}