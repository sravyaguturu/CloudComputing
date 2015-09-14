package com.example.trafficalertsystem;

// TODO: Auto-generated Javadoc
/**
 * The Class Constants.
 */
public class Constants {
	 // Defines a custom Intent action
    /** The Constant BROADCAST_ACTION. */
 	public static final String BROADCAST_ACTION =
        "com.example.android.threadsample.BROADCAST";
 
    // Defines the key for the status "extra" in an Intent
    /** The Constant EXTENDED_DATA_STATUS. */
    public static final String EXTENDED_DATA_STATUS =
        "com.example.android.threadsample.STATUS";
    
    public static final String START_DRIVING="Start Driving";
    public static final String STOP_DRVING="Stop Driving";
    public static final String TOAST_MAP_UNAVAILABLE="Sorry ! unable to create maps!";
	public static final String NAMESPACE = "http://system.mobilesensing.com/";
	public static final String URL = "http://192.168.0.4:9999/MobileService?wsdl";
	public static final String METHOD_NAME = "getRecentTrafficAlerts";
	public static final String METHOD_NAME_RECEIVE = "receiveAlertsFromDevice";

	public static final String GROUP_IP="225.0.0.0";
	public static final int PORT_NO=1234;
	public static final String OK="OK";
	public static final String SAME="same";
	public static final String IN="in";
	public static final String TRAFFIC_ALERT="Traffic Alert";
	public static final String STOP_ALERT="Stop Alert";
	public static final String VIEW_MAPS="View Maps";
	public static final String OUT="out";
	public static final float MILES_CONVERTER=0.000621371f;
	public static final String LATITUDE="Latitude";
	public static final String LONGITUDE="Longitude";
	public static final String MACADDRESS="MacAddress";
}
