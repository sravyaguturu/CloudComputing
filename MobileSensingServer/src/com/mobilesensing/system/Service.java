package com.mobilesensing.system;

import javax.xml.ws.Endpoint;

// TODO: Auto-generated Javadoc
/**
 * The Class Service.
 */
public class Service {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		Endpoint.publish("http://0.0.0.0:9999/MobileService",
				new MobileSensingSystem());

	}
}
