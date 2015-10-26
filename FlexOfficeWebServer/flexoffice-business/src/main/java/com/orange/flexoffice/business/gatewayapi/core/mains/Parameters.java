package com.orange.flexoffice.business.gatewayapi.core.mains;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * Parameters : global parameters
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * Global parameters for :
 *  - data access
 *  - clustering
 *  - nearest neighbors methods
 * 
 */

public class Parameters {
	
	/**
	 * URL to connect to the SQL database
	 */
	public static String SQL_url = "jdbc:mysql://l-rpb/e_reperio_nf_stephane";

	/**
	 * Login to connect to the SQL database
	 */
	public static String SQL_login = "root";
	
	/**
	 * Password to connect to the SQL database
	 */
	public static String SQL_password = "!pl$ett?";

	/**
	 * SQL table of ratings
	 */
	public static String SQL_ratings_table = "learning__fold1";
	
	/**
	 * SQL table of similarities
	 */
	public static String SQL_similarities_table = "matrix_test_100";

	/**
	 * SQL table of items
	 */
	public static String SQL_items_table = "stats_rows__learning__fold1";

	/**
	 * SQL table of users
	 */
	public static String SQL_users_table = "stats_columns__learning__fold1";

	/**
	 * Directory for the executables
	 */
	public static String engineExec = "./";

	/**
	 * Directory to store the models
	 */
	public static String engineModelDirectory = "/home/syig7390/engineModel/";
	
	/**
	 * Directory to store the process execution results
	 */
	public static String engineCheckResult = "/home/syig7390/";
	
	/**
	 * File for Netflix Qualifying test
	 */
	public static String netflixQualifyingFile = "/home/SharedData/engineProto/hybridEngine/netflixData/qualifying.txt";
	
	/**
	 * File for Netflix Probe test
	 */
	public static String netflixProbeFile = "/home/SharedData/engineProto/hybridEngine/netflixData/probe.txt";
	
	/**
	 * File for Netflix RMSE perl script
	 */
	public static String netflixRMSEFile = "/home/SharedData/engineProto/hybridEngine/netflixData/rmse.pl";
	
	
	/**
	 * Indicates if traces on console are active or not
	 */
	public static boolean traceOn = true;
	
	/**
	 * Min of the rating scale
	 */
	public static int minRatingScale = -100;
	
	/**
	 * Max of the rating scale
	 */
	public static int maxRatingScale = 100;
	
	/**
	 * Number of multi-starts of the clustering
	 */
	public static int clusteringMultiStartNumber = 3;
	
	/**
	 * Number of iterations for one run of clustering
	 */
	public static int clusteringIterationNumber = 5;

	/**
	 * Number of item clusters
	 */
	public static int itemClusterNumber = 50;

	/**
	 * Number of user clusters
	 */
	public static int userClusterNumber = 170;

	/**
	 * Number of neighbors for nearest neighbors methods
	 */
	public static int neighborNumber = 100;
	
	/**
	 * Sample size for profile recommendations
	 */
	public static int profileRecoSampleNumber = 100;
	
	/**
	 * Number of profile recommendations
	 */
	public static int profileRecoNumber = 20;

	/**
	 * Sample size for risky recommendations
	 */
	public static int riskyRecoSampleNumber = 60;

	/**
	 * Number of risky recommendations
	 */
	public static int riskyRecoNumber = 20;
	
	//-----------------------------------------------------------------------------------
	//
	// Handling the parameters with files
	//
	//-----------------------------------------------------------------------------------
		
	public static Properties properties;
	public static String propertiesFileName = "properties";
	
	/**
	 * Specify the values of the parameters
	 */
	public static void setProperties () {

		properties = new Properties();
		
		properties.setProperty("SQL_url",SQL_url);
		properties.setProperty("SQL_login",SQL_login);
		properties.setProperty("SQL_password",SQL_password);
		properties.setProperty("SQL_ratings_table",SQL_ratings_table);
		properties.setProperty("SQL_similarities_table",SQL_similarities_table);
		properties.setProperty("SQL_items_table",SQL_items_table);
		properties.setProperty("SQL_users_table",SQL_users_table);
		properties.setProperty("engineExec",engineExec);
		properties.setProperty("engineModelDirectory",engineModelDirectory);
		properties.setProperty("engineCheckResult",engineCheckResult);
		properties.setProperty("netflixQualifyingFile",netflixQualifyingFile);
		properties.setProperty("netflixProbeFile",netflixProbeFile);
		properties.setProperty("netflixRMSEFile",netflixRMSEFile);
		properties.setProperty("traceOn",""+traceOn);
		properties.setProperty("minRatingScale",""+minRatingScale);
		properties.setProperty("maxRatingScale",""+maxRatingScale);
		properties.setProperty("clusteringMultiStartNumber",""+clusteringMultiStartNumber);
		properties.setProperty("clusteringIterationNumber",""+clusteringIterationNumber);
		properties.setProperty("itemClusterNumber",""+itemClusterNumber);
		properties.setProperty("userClusterNumber",""+userClusterNumber);
		properties.setProperty("neighborNumber",""+neighborNumber);
		properties.setProperty("profileRecoSampleNumber",""+profileRecoSampleNumber);
		properties.setProperty("profileRecoNumber",""+profileRecoNumber);
		properties.setProperty("riskyRecoSampleNumber",""+riskyRecoSampleNumber);
		properties.setProperty("riskyRecoNumber",""+riskyRecoNumber);

		try {
			FileOutputStream out = new FileOutputStream(propertiesFileName);
			properties.store(out,"Parameters data");
			out.close();
		} catch (IOException e) {
			System.out.println("Mains - Parameters - setProperties");
			System.out.println("  => IO exception when writing on file " + propertiesFileName);
			e.printStackTrace();
		}
	}
	
	/**
	 * Load the values of the parameters
	 */
	public static void loadProperties () {

		properties = new Properties();
		
		try {
			FileInputStream in = new FileInputStream(propertiesFileName);
			properties.load(in);
			in.close();
		

		Parameters.SQL_url = properties.getProperty("SQL_url");
		Parameters.SQL_login = properties.getProperty("SQL_login");
		Parameters.SQL_password = properties.getProperty("SQL_password");
		Parameters.SQL_ratings_table = properties.getProperty("SQL_ratings_table");
		Parameters.SQL_similarities_table = properties.getProperty("SQL_similarities_table");
		Parameters.SQL_items_table = properties.getProperty("SQL_items_table");
		Parameters.SQL_users_table = properties.getProperty("SQL_users_table");
		Parameters.engineExec = properties.getProperty("engineExec");
		Parameters.engineModelDirectory = properties.getProperty("engineModelDirectory");
		Parameters.engineCheckResult = properties.getProperty("engineCheckResult");
		Parameters.netflixQualifyingFile = properties.getProperty("netflixQualifyingFile");
		Parameters.netflixProbeFile = properties.getProperty("netflixProbeFile");
		Parameters.netflixRMSEFile = properties.getProperty("netflixRMSEFile");
		Parameters.traceOn = new Boolean(properties.getProperty("traceOn"));
		Parameters.minRatingScale = new Integer(properties.getProperty("minRatingScale"));
		Parameters.maxRatingScale = new Integer(properties.getProperty("maxRatingScale"));
		Parameters.clusteringIterationNumber = new Integer(properties.getProperty("clusteringIterationNumber"));
		Parameters.clusteringMultiStartNumber = new Integer(properties.getProperty("clusteringMultiStartNumber"));
		Parameters.itemClusterNumber = new Integer(properties.getProperty("itemClusterNumber"));
		Parameters.userClusterNumber = new Integer(properties.getProperty("userClusterNumber"));
		Parameters.neighborNumber = new Integer(properties.getProperty("neighborNumber"));
		Parameters.profileRecoSampleNumber = new Integer(properties.getProperty("profileRecoSampleNumber"));
		Parameters.profileRecoNumber = new Integer(properties.getProperty("profileRecoNumber"));
		Parameters.riskyRecoSampleNumber = new Integer(properties.getProperty("riskyRecoSampleNumber"));
		Parameters.riskyRecoNumber = new Integer(properties.getProperty("riskyRecoNumber"));
		} catch (IOException e) {
			System.out.println("Mains - Parameters - loadProperties");
			System.out.println("  => IO exception when reading from file " + propertiesFileName);
			e.printStackTrace();
		}
	}
	
	public static float normalizeRating (float rating) {
		return 1 + 4 * (rating - Parameters.minRatingScale) / (Parameters.maxRatingScale - Parameters.minRatingScale);
	}
	
	public static double normalizeRating (double rating) {
		return 1 + 4 * (rating - Parameters.minRatingScale) / (Parameters.maxRatingScale - Parameters.minRatingScale);
	}
	/**
	 * Create the file of properties
	 */
	public static void main (String[] args) {
		setProperties();
	}
}
