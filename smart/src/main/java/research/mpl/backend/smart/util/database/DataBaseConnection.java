package research.mpl.backend.smart.util.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import research.mpl.backend.smart.util.TimeUtil;

import org.hsqldb.Server;

public class DataBaseConnection {

	private static final DataBaseConnection connectionINSTANCE = new DataBaseConnection();

	private DataBaseConnection() {}

	public static DataBaseConnection getInstance() {
		return connectionINSTANCE;
	}

	private Connection connection = null;

	public Connection getConnection(Properties databaseConfiguration) {
		if (this.connection == null) {
			try {
				// Load the HSQLDB Database Driver.
				// This gets loaded from the hsqldb-xxx.jar
				Class.forName("org.hsqldb.jdbcDriver");
			} catch (ClassNotFoundException cnfe) {
				System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
				cnfe.printStackTrace();
			}

			try {
				// Connect to the database.
				this.connection = DriverManager.getConnection(
						databaseConfiguration.getProperty("dburl"),
						databaseConfiguration.getProperty("dbuser"),
						databaseConfiguration.getProperty("dbpassword"));
			} catch (SQLException e) {
				System.out.println("ERROR: failed to connect to the databse");
				e.printStackTrace();
			}
		}

		return connection;
	}

	public int insertExperiment(Connection con) {

		PreparedStatement ps = null;
		int nextId = 0;

		try {
			ps = con.prepareStatement("SELECT COUNT(*) FROM EXPERIMENT;");
			ResultSet rs = ps.executeQuery(); // read from database
			rs.next();
			nextId = rs.getInt(1);

			ps = con.prepareStatement("INSERT INTO EXPERIMENT VALUES(?,?)");
			ps.setInt(1, nextId);
			ps.setString(2, "MLP-PSO; 30 particles; 2000 iterations; 5 neurons");
			ps.executeUpdate(); // executes the insert query

		} catch (Exception e) {
			System.out.println("ERROR executing query: ");
			e.printStackTrace();
		} finally {
			try {
				// close the statement
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return nextId;
	}

	public int insertSimulation(int experimentId) {

		PreparedStatement ps = null;
		int nextId = 0;

		try {
			ps = this.connection.prepareStatement("SELECT COUNT(*) FROM SIMULATION;");
			ResultSet rs = ps.executeQuery(); // read from database
			rs.next();
			nextId = rs.getInt(1);

			ps = this.connection.prepareStatement("INSERT INTO SIMULATION VALUES(?,?, ?)");
			ps.setInt(1, nextId);
			ps.setInt(2, experimentId);
			ps.setNull(3, java.sql.Types.DOUBLE);
			ps.executeUpdate(); // executes the insert query

		} catch (Exception e) {
			System.out.println("ERROR executing query: ");
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nextId;
	}

	public int insertIterationResult(int simulationId, double bestResult, String varibleValues) {

		PreparedStatement ps = null;
		int nextId = 0;

		try {
			ps = this.connection.prepareStatement("SELECT COUNT(*) FROM SIMULATION_ITERATION;");
			ResultSet rs = ps.executeQuery(); // read from database
			rs.next();
			nextId = rs.getInt(1);

			ps = this.connection.prepareStatement("INSERT INTO SIMULATION_ITERATION VALUES(?,?,?,?)");
			ps.setInt(1, nextId);
			ps.setInt(2, simulationId);
			ps.setDouble(3, bestResult);
			ps.setString(4, varibleValues);
			ps.executeUpdate(); // executes the insert query

		} catch (Exception e) {
			System.out.println("ERROR executing query: ");
			e.printStackTrace();
		} finally {
			try {
				// close the statement
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nextId;
	}

	public void executeSelectQuery(Connection con) {
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("SELECT EXPERIMENT_ID, PARAMETERS FROM EXPERIMENT");
			ResultSet rs = ps.executeQuery(); // read from database
			while (rs.next()) {
				Integer id = rs.getInt("EXPERIMENT_ID");
				String parameters = rs.getString("PARAMETERS");
				System.out.println("id:" + id + ", name:" + parameters);
			}

		} catch (Exception e) {
			System.out.println("ERROR executing query: ");
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// main method
	public static void main(String[] args) {

		// 'Server' is a class of HSQLDB representing
		// the database server
		Server hsqlServer = null;
		try {
			Properties databaseConfiguration = new Properties();
			InputStream databaseConfigurationFilePath =
                    DataBaseConnection.class.getResourceAsStream("/main/resources/database.properties");
			databaseConfiguration.load(databaseConfigurationFilePath);

			hsqlServer = new Server();

			// HSQLDB prints out a lot of informations when
			// starting and closing, which we don't need now.
			// Normally you should point the setLogWriter
			// to some Writer object that could store the logs.
			hsqlServer.setLogWriter(null);
			hsqlServer.setSilent(true);

			// The actual database will be named 'Experiments_Results' and its
			// settings and data will be stored in files
			// bp_experiments.properties and bp_experiments.script
			hsqlServer.setDatabaseName(0, databaseConfiguration.getProperty("dbname"));
			hsqlServer.setDatabasePath(0, databaseConfiguration.getProperty("dbpath"));

			// Start the database!
			hsqlServer.start();

			DataBaseConnection tut = new DataBaseConnection();

			// 1. getSolutionAt the connection to the database
			final Connection con = tut.getConnection(databaseConfiguration);

			// Run script to create database, tables and initial values
            // (it will be created only if it doesn't exist already)
			ScriptRunner scriptRunner = new ScriptRunner(con, false, true);
			InputStream scriptDB =
                    DataBaseConnection.class.getResourceAsStream("/main/resources/create-script.sql");
			Reader reader = new InputStreamReader(scriptDB);
			scriptRunner.runScript(reader);

			long initTime = System.nanoTime();
			int experimentId = tut.insertExperiment(con);
			long estimatedTime = System.nanoTime() - initTime;
            TimeUtil.printExecutionTime(estimatedTime, "Insert experiment");

			initTime = System.nanoTime();
			int simulationId = tut.insertSimulation(experimentId);
			estimatedTime = System.nanoTime() - initTime;
            TimeUtil.printExecutionTime(estimatedTime, "Insert simulation");

			initTime = System.nanoTime();
			int simulationIteration = tut.insertIterationResult(simulationId, 0.05, "variable values");
			estimatedTime = System.nanoTime() - initTime;
            TimeUtil.printExecutionTime(estimatedTime, "Insert iteration results");

			initTime = System.nanoTime();
			tut.executeSelectQuery(con);
            estimatedTime = System.nanoTime() - initTime;
            TimeUtil.printExecutionTime(estimatedTime, "Select all records from the database");


            System.out.println("Press any key to finish");
			char tmp = (char) System.in.read();
			con.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			// Closing the server
			if (hsqlServer != null) {
				hsqlServer.stop();
				hsqlServer = null;
			}
		}
	}

}
