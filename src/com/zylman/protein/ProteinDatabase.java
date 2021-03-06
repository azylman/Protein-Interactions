package com.zylman.protein;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ProteinDatabase {
	
	//did we need to create the tables?
	private boolean createdTables;
	
	//database session properties
	private String host;
	private String databaseName;
	private String userName;
	private String password;
	private Connection connection;
	
	//database properties
	private static final String PROTEINS_TABLE_NAME = "proteins";
	private static final String INTERACTIONS_TABLE_NAME = "interactions";
	private static final String SEQUENCES_TABLE_NAME = "sequences";
	private List<String> tables = new ArrayList<String>(Arrays.asList(PROTEINS_TABLE_NAME, INTERACTIONS_TABLE_NAME, SEQUENCES_TABLE_NAME));
	private Map<String, Boolean> tableStatus = new HashMap<String, Boolean>();
	
	//column info
	private static final int FEATURE_VECTOR_COLUMN_SIZE = 5000;
	
	public ProteinDatabase(String host, String databaseName, String user, String pass) throws ProteinException {
		try {	
			establishConnectionToDatabase(host, databaseName, user, pass);

			for (String table : tables) {
				tableStatus.put(table, false);
			}
			
			createdTables = false;
			assessTableStatus();
			for (Map.Entry<String, Boolean> table : tableStatus.entrySet()) {
				if (!table.getValue()) {
					createdTables = true;
					createTable(table.getKey());
				}
			}
		} catch (Exception ex) {
			throw new ProteinException(ex,"Could not initialize database.");
		}
	}
	
	public boolean neededToCreateTables() {
		return createdTables;
	}
	
	public void addProteinList(Iterable<Protein> proteins) throws ProteinException {
		int proteinsAdded = 0;
		for (Protein protein : proteins) {
			addProtein(protein);
			if (proteinsAdded++ % 1000 == 0) {
				System.out.println("Added " + proteinsAdded + " proteins to the database so far");
			}
		}
	}
	
	public void addInteractionList(Iterable<Interaction> interactions) throws ProteinException {
		for (Interaction interaction : interactions) {
			addInteraction(interaction);
		}
	}
	
	public void addProtein(Protein protein) throws ProteinException {
		if (hasTable(PROTEINS_TABLE_NAME)) {
			try {
				Statement s = getStatement(false);
				
				// We're going to try it without storing the feature vector to hopefully save on memory space
				//protein.createFeatureVector();

				FeatureVector fv = new FeatureVector(protein.getSequence());
				
				List<Double> hydrophobicityList = fv.getHydrophobicity();
				List<Double> hydrophocilityList = fv.getHydrophicility();
				List<Double> volumeList = fv.getVolume();
				List<Double> polarityList = fv.getPolarity();
				List<Double> polarizabilityList = fv.getPolarizability();
				List<Double> SASAList = fv.getSASA();
				List<Double> NCIList = fv.getNCI();
				
				String dipId = protein.getId();
				String sequence = protein.getSequence();
				String hydrophobicity = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(hydrophobicityList));
				String hydrophocility = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(hydrophocilityList));
				String volume = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(volumeList)); 
				String polarity = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(polarityList));
				String polarizability = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(polarizabilityList));
				String SASA = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(SASAList));
				String NCI = convertStringListToSemicolonDelimitedString(convertDoubleListToStringList(NCIList));
				
				String insert = "INSERT INTO " + PROTEINS_TABLE_NAME + " VALUES("
						+ convertStringsToCommaDelimitedString(dipId, hydrophobicity, hydrophocility, volume, polarity, polarizability, SASA, NCI)
						+ ")";
				
				//System.out.println(insert);
				s.execute(insert);
				
				insert = "INSERT INTO " + SEQUENCES_TABLE_NAME + " VALUES("
						+ convertStringsToCommaDelimitedString(dipId, sequence)
						+ ")";
				
				s.execute(insert);
				
				s.close();
			} catch (SQLException ex) {
				System.out.println("SQL Exception on protein " + protein.getId() + " (" + protein.getSequence() + "): " + ex.getMessage());
				throw new ProteinException(
						ex,
						"ProteinDatabase.addProtein(): SQL exception encounted while trying to enter following protein into database: \n" + protein.toString());
			}
		} else {
			throw new ProteinException("ProteinDatabase.addProtein(): Missing one or more required tables");
		}
	}
	
	public void addInteraction(Interaction interaction) throws ProteinException {
		if (hasTable(INTERACTIONS_TABLE_NAME)) {
			try {
				Statement s = getStatement(false);
				
				String insert = "INSERT INTO " + INTERACTIONS_TABLE_NAME + " VALUES("
						+ convertStringsToCommaDelimitedString(interaction.getFirst(), interaction.getSecond(), Integer.toString(interaction.getClassification() ? 1 : 0))
						+ ")";
				
				System.out.println(insert);
				s.execute(insert);
				s.close();
			} catch (SQLException ex) {
				throw new ProteinException(
						ex,
						"ProteinDatabase.addInteraction(): SQL exception encounted while trying to enter following interaction into database: \n" + interaction.toString());
			}
		} else {
			throw new ProteinException("ProteinDatabase.addInteraction(): Missing one or more required tables");
		}
	}
	
	Map<String, FeatureVector> getProteins() throws ProteinException {
		Map<String, FeatureVector> result = new HashMap<String, FeatureVector>();
		
		Statement s = getStatement(false);
		try {
			ResultSet r = s.executeQuery("SELECT * FROM " + PROTEINS_TABLE_NAME);
			while (r.next()) {
				String dipIdString = r.getString(1);
				String hydrophobicityString = r.getString(2);
				String hydrophocilityString = r.getString(3);
				String volumeString = r.getString(4);
				String polarityString = r.getString(5);
				String polarizabilityString = r.getString(6);
				String SASAString = r.getString(7);
				String NCIString = r.getString(8);
				
				List<Double> hydrophobicity = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(hydrophobicityString));
				List<Double> hydrophocility = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(hydrophocilityString));
				List<Double> volume = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(volumeString));
				List<Double> polarity = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(polarityString));
				List<Double> polarizability = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(polarizabilityString));
				List<Double> SASA = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(SASAString));
				List<Double> NCI = convertStringListToDoubleList(convertSemicolonDelimitedStringToStringList(NCIString));
				result.put(dipIdString, new FeatureVector(hydrophobicity, hydrophocility, volume, polarity, polarizability, SASA, NCI));
			}
			
			return result;
			
		} catch (SQLException ex) {
			throw new ProteinException(
					ex,
					"ProteinDatabase.getProteins(): SQL exception encountered while trying to get proteins from the database");
		}
	}
	
	List<Interaction> getInteractions() throws ProteinException {
		List<Interaction> result = new ArrayList<Interaction>();
		
		Statement s = getStatement(false);
		try {
			ResultSet r = s.executeQuery("SELECT * FROM " + INTERACTIONS_TABLE_NAME);
			while (r.next()) {
				String dipId1 = r.getString(1);
				String dipId2 = r.getString(2);
				Boolean classification = r.getBoolean(3);
				
				result.add(new Interaction(dipId1, dipId2, classification));
			}
			
			return result;
		} catch (SQLException ex) {
			throw new ProteinException(
					ex,
					"ProteinDatabase.getInteractions(): SQL exception encountered while trying to get interactions from the database");
		}
	}
	
	private void establishConnectionToDatabase(String host, String databaseName, String user, String pass) throws ProteinException {
		this.host = host;
		this.databaseName = databaseName;
		this.userName = user;
		this.password = pass;
		connection = getNewConnection();	
	}
	
	private Connection getNewConnection() throws ProteinException {
		try {
			String url = "jdbc:mysql://" + host + ":3306/"+ databaseName;
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			Properties props = new Properties();
			props.setProperty("user", userName);
			props.setProperty("password", password);
			props.setProperty("characterEncoding", "utf-8");
			props.setProperty("connectionCollation", "utf8_unicode_ci");
			return connection = DriverManager.getConnection(url, props);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new ProteinException(e);
		}
	}
	
	/**
	 * Check to see which tables have been created and set status indicators accordingly
	 */
	private void assessTableStatus() throws ProteinException {
		System.out.println("EarmarkDatabase.assessTableStatus(): Checking tables...");
		
		try {
			Statement s = this.getStatement(false);
			ResultSet r = s.executeQuery("SHOW TABLES");
			List<String> foundTables = new ArrayList<String>();

			while(r.next()) {
				String name = r.getString(1);
				foundTables.add(name);
				
				if (!tableStatus.containsKey(name)) {
					System.out.println("\tWARNING: unrecognized table found: " + name);
				} else {
					tableStatus.put(name, true);
					System.out.println("\tFound table '" + name + "'.");
				}
			}
			
			for (String table : tables) {
				if (!tableStatus.get(table)) System.out.println("\tWARNING: Could not find table '" + table + "' in database.");
			}
			
			s.close();
		} catch (SQLException e) {
			throw new ProteinException(e,"EarmarkDatabase.assessTableStatus():  SQL error attempting to check tables");
		}
	}
	
	private void createTable(String tableName) throws ProteinException {
		if (tableName.equals(PROTEINS_TABLE_NAME)) {
			createProteinTable();
		} else if (tableName.equals(INTERACTIONS_TABLE_NAME)) {
			createInteractionsTable();
		} else if (tableName.equals(SEQUENCES_TABLE_NAME)) {
			createSequenceTable();
		}
	}
	
	private boolean createProteinTable() throws ProteinException {
		if (hasTable(PROTEINS_TABLE_NAME)) {
			System.err.println("Could not create '" + PROTEINS_TABLE_NAME + "' table, because table already exists in database");
			return false;
		}
		
		try {
			final String featureColumnSize = Integer.toString(FEATURE_VECTOR_COLUMN_SIZE);
			
			@SuppressWarnings("serial")
			Map<String, String> tableFields = new LinkedHashMap<String, String>() {{
				put("dip_id", "VARCHAR(128)");
				put("hydrophobicity", "VARCHAR(" + featureColumnSize + ")");
				put("hydrophocility", "VARCHAR(" + featureColumnSize + ")");
				put("volume", "VARCHAR(" + featureColumnSize + ")");
				put("polarity", "VARCHAR(" + featureColumnSize + ")");
				put("polarizability", "VARCHAR(" + featureColumnSize + ")");
				put("SASA", "VARCHAR(" + featureColumnSize + ")");
				put("NCI", "VARCHAR(" + featureColumnSize + ")");
			}};
			
			String create = "CREATE TABLE " + PROTEINS_TABLE_NAME + "(";
			for (Map.Entry<String, String> field : tableFields.entrySet()) {
				create += field.getKey() + " " + field.getValue() + ", ";
			}
			
			create = create.substring(0, create.length() - 2);
			create += ")";
			
			Statement s = this.getStatement(false);
			s.execute(create);
			s.execute("ALTER TABLE " + PROTEINS_TABLE_NAME + " ADD PRIMARY KEY (dip_id)");
			
			s.close();
			System.out.println("ProteinDatabase.createProteinTable(): successfully created protein table.");
			this.tableStatus.put(PROTEINS_TABLE_NAME, true);
			
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new ProteinException(e, "ProteinDatabase.createProteinTable(): SQL error in trying to create protein table");
		}
	}
	
	private boolean createInteractionsTable() throws ProteinException {
		if (hasTable(INTERACTIONS_TABLE_NAME)) {
			System.err.println("Could not create '" + INTERACTIONS_TABLE_NAME + "' table, because table already exists in database");
			return false;
		}
		
		try {
			@SuppressWarnings("serial")
			Map<String, String> tableFields = new LinkedHashMap<String, String>() {{
				put("dip_id1", "VARCHAR(128)");
				put("dip_id2", "VARCHAR(128)");
				put("classification", "BOOLEAN");
			}};
			
			String create = "CREATE TABLE " + INTERACTIONS_TABLE_NAME + "(";
			for (Map.Entry<String, String> field : tableFields.entrySet()) {
				create += field.getKey() + " " + field.getValue() + ", ";
			}
			
			create = create.substring(0, create.length() - 2);
			create += ")";
			
			Statement s = this.getStatement(false);
			s.execute(create);
			s.execute("ALTER TABLE " + INTERACTIONS_TABLE_NAME + " ADD INDEX (dip_id1)");
			s.execute("ALTER TABLE " + INTERACTIONS_TABLE_NAME + " ADD INDEX (dip_id2)");
			
			s.close();
			System.out.println("ProteinDatabase.createInteractionsTable(): successfully created interactions table.");
			this.tableStatus.put(INTERACTIONS_TABLE_NAME, true);
			
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new ProteinException(e, "ProteinDatabase.createInteractionsTable(): SQL error in trying to create interactions table");
		}
	}
	
	private boolean createSequenceTable() throws ProteinException {
		if (hasTable(SEQUENCES_TABLE_NAME)) {
			System.err.println("Could not create '" + INTERACTIONS_TABLE_NAME + "' table, because table already exists in database");
			return false;
		}
		
		try {
			@SuppressWarnings("serial")
			Map<String, String> tableFields = new LinkedHashMap<String, String>() {{
				put("dip_id", "VARCHAR(128)");
				put("sequence", "VARCHAR(60000)");
			}};
			
			String create = "CREATE TABLE " + SEQUENCES_TABLE_NAME + "(";
			for (Map.Entry<String, String> field : tableFields.entrySet()) {
				create += field.getKey() + " " + field.getValue() + ", ";
			}
			
			create = create.substring(0, create.length() - 2);
			create += ")";
			
			Statement s = this.getStatement(false);
			s.execute(create);
			s.execute("ALTER TABLE " + SEQUENCES_TABLE_NAME + " ADD INDEX (dip_id)");
			
			s.close();
			System.out.println("ProteinDatabase.createSequenceTable(): successfully created interactions table.");
			this.tableStatus.put(SEQUENCES_TABLE_NAME, true);
			
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new ProteinException(e, "ProteinDatabase.createSequenceTable(): SQL error in trying to create interactions table");
		}
	}

	private static List<String> convertDoubleListToStringList(List<Double> list) {
		List<String> output = new ArrayList<String>();
		for (Double entry : list) {
			output.add(entry.toString());
		}
		return output;
	}
	
	private static List<Double> convertStringListToDoubleList(List<String> list) {
		List<Double> output = new ArrayList<Double>();
		for (String string : list) {
			output.add(Double.parseDouble(string));
		}
		return output;
	}
	
	private static String convertStringsToCommaDelimitedString(String... strings) {
		StringBuilder out = new StringBuilder();
		for (String string : strings) {
			out.append("'");
			out.append(string);
			out.append("'");
			out.append(",");
		}
		out.deleteCharAt(out.length() - 1);
		return out.toString();
	}
	
	private static String convertStringListToSemicolonDelimitedString(Collection<String> collection)
	{
		if ((collection == null) || (collection.size() == 0)) return "";
		
		StringBuilder builder = new StringBuilder();
		
		Iterator<String> iterator = collection.iterator();
		String term = iterator.next();
		builder.append(term);
		while (iterator.hasNext())
		{
			term = iterator.next();
			builder.append(";"+term);
		}
		
		return builder.toString();	
	}
	
	private static List<String> convertSemicolonDelimitedStringToStringList(String list) {
		List<String> result = new ArrayList<String>();
		
		String[] strings = list.split(";");
		for (String string : strings) {
			if (!string.isEmpty()) {
				result.add(string);
			}
		}
		
		return result;
	}

	private Statement getStatement(boolean editable) throws ProteinException
	{
		try 
		{
			if (!editable){
				return connection.createStatement();
			}else{
				return connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			}
			
		} catch (SQLException e) {
			throw new ProteinException(e, "Could not create statement");
		}
	}
	
	private boolean hasTable(String tableName)
	{
		if (tableStatus.containsKey(tableName))
			return tableStatus.get(tableName);
		else
			return false;
	}
}
