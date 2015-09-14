package com.mobilesensing.system;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.codehaus.jackson.map.ObjectMapper;

import com.latlong.GoogleResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class MobileSensingSystem.
 */
@HandlerChain(file = "handler-chain.xml")
@WebService
public class MobileSensingSystem {

	/** The connection. */
	static Connection connection;


	/**
	 * 	// Update the Db with the GPS Co-ordinates and MAC

	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param address
	 *            the address
	 * @param mac
	 *            the mac
	 */
	public static void UpdateDB(String latitude, String longitude,
			String address, String mac) {
		try {

			Class.forName(Constants.CLASS);
			connection = DriverManager
					.getConnection(Constants.CONNECTION);

			Statement statement = connection.createStatement();
			ResultSet rs = null;
			/* get all records from the Employee table */
			String query = "select count(*) from userDB where location ='"
					+ address + "' and macAddress ='" + mac + "'";
			rs = statement.executeQuery(query);
			rs.next();
			int count = rs.getInt(1);
			if (count == 0) {
				String insertQuery = "insert into userDB (latitude,longitude,location,macaddress,alerttime) values('"
						+ latitude
						+ "','"
						+ longitude
						+ "','"
						+ address
						+ "','"
						+ mac
						+ "',to_date('"
						+ getCurrentTimeStamp()
						+ "', 'yyyy/mm/dd hh24:mi:ss'))";
				statement.executeUpdate(insertQuery);

			} else {
				String updateQuery = "update  userDB set alerttime=to_date('"
						+ getCurrentTimeStamp()
						+ "', 'yyyy/mm/dd hh24:mi:ss') where location ='"
						+ address + "' and macAddress ='" + mac + "'";
				statement.executeUpdate(updateQuery);
			}
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gets the current time stamp.
	 * 
	 * @return the current time stamp
	 */
	private static String getCurrentTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date today = new java.util.Date();
		return dateFormat.format(today.getTime());

	}

	/**
	 *	// Convert the lat long to human readable address

	 * 
	 * @param latlongString
	 *            the latlong string
	 * @return the google response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@WebMethod
	public GoogleResponse convertFromLatLong(String latlongString)
			throws IOException {

		URL url = new URL(URL + "?latlng="
				+ URLEncoder.encode(latlongString, "UTF-8") + "&sensor=false");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		GoogleResponse response = (GoogleResponse) mapper.readValue(in,
				GoogleResponse.class);
		in.close();
		return response;
	}

	/**
	 * 	// Check the Db with the GPS Co-ordinates and MAC address unicity

	 * 
	 * @param address
	 *            the address
	 * @return true, if successful
	 */
	public static boolean checkDB(String address) {
		boolean trafficAlert = false;
		try {

			Class.forName(Constants.CLASS);
			connection = DriverManager
					.getConnection(Constants.CONNECTION);
			Statement statement = connection.createStatement();
			ResultSet rs = null;
			String query = "select count(*) from userDB where location ='"
					+ address + "'";
			rs = statement.executeQuery(query);
			rs.next();
			int count = rs.getInt(1);
			if (count >= 1) {
				String query1 = "select count(*) from archive where location ='"
						+ address + "'";
				rs = statement.executeQuery(query1);
				rs.next();
				int count1 = rs.getInt(1);
				if (count1 == 0) {
					String insertQuery = "insert into archive (location,severity,alerttime) values('"
							+ address
							+ "','HIGH',to_date('"
							+ getCurrentTimeStamp()
							+ "', 'yyyy/mm/dd hh24:mi:ss'))";
					statement.executeUpdate(insertQuery);
					trafficAlert = true;
				} else {
					String updateQuery1 = "update  archive set alerttime=to_date('"
							+ getCurrentTimeStamp()
							+ "', 'yyyy/mm/dd hh24:mi:ss') where location ='"
							+ address + "'";
					statement.executeUpdate(updateQuery1);
				}
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return trafficAlert;

	}

	/**
	 * 	// retrieves all the traffic alerts in the past hour

	 * 
	 * @return the recent traffic alerts
	 */
	@WebMethod
	public String[] getRecentTrafficAlerts() {
		String[] returnVal = new String[2];
		try {
			Class.forName(Constants.CLASS);
			connection = DriverManager
					.getConnection(Constants.CONNECTION);
			Statement statement = connection.createStatement();
			ResultSet rs = null;
			String query = "select count(*) from archive";
			String data;
			rs = statement.executeQuery(query);
			rs.next();
			int count = rs.getInt(1);
			int i = 0;
			if (count > 0) {

				String query1 = "SELECT * FROM (select * from archive order by alerttime desc) WHERE rownum <= 2";
				rs = statement.executeQuery(query1);
				while (rs.next()) {
					data = rs.getString("location") + "# "
							+ rs.getString("AlertTime");
					returnVal[i] = data;
					i++;
					System.out.println(data);
				}

			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnVal;
	}

}