package com.mobilesensing.system;

import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import BroadCastServer.MulticastServer;

import com.latlong.AddressConverter;
import com.latlong.GoogleResponse;
import com.latlong.Result;

import constants.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageHandler.
 */
public class MessageHandler implements SOAPHandler<SOAPMessageContext> {

	/**
	 * Close.
	 * 
	 * @param arg0
	 *            the arg0
	 */
	public void close(MessageContext arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handle fault.
	 * 
	 * @param arg0
	 *            the arg0
	 * @return true, if successful
	 */
	public boolean handleFault(SOAPMessageContext arg0) {
		return false;
	}

	/**
	 * Handles the input message and invokes the multicast server.
	 * 
	 * @param context
	 *            the context
	 * @return true, if successful
	 */
	public boolean handleMessage(SOAPMessageContext context) {
		SOAPMessage soapMsg = context.getMessage();

		String address = null;

		SOAPEnvelope soapEnv;
		try {
			SOAPBody body = soapMsg.getSOAPPart().getEnvelope().getBody();

			String methodName = body.getFirstChild().getNodeName();

			if (methodName.equalsIgnoreCase(Constants.METHOD_NAME_RECEIVE)) {
				String latitude = body.getFirstChild().getAttributes()
						.getNamedItem(Constants.LATITUDE).getTextContent();
				String longitude = body.getFirstChild().getAttributes()
						.getNamedItem(Constants.LONGITUDE).getTextContent();
				String mac = body.getFirstChild().getAttributes()
						.getNamedItem(Constants.MACADDRESS).getTextContent();

				GoogleResponse res1;
				try {
					res1 = new AddressConverter().convertFromLatLong(latitude
							+ "," + longitude);
					if (res1.getStatus().equals(Constants.OK)) {
						for (Result result : res1.getResults()) {
							address = result.getFormatted_address();
							MobileSensingSystem.UpdateDB(latitude, longitude,
									address, mac);
							boolean alert = MobileSensingSystem
									.checkDB(address);
							String message = "Medium traffic alert in "
									+ address;
							if (alert) {
								MulticastServer multicast = new MulticastServer(
										message, latitude, longitude);
								new Thread(multicast).start();
							}
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (SOAPException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Gets the headers.
	 * 
	 * @return the headers
	 */
	public Set<QName> getHeaders() {
		return null;
	}

}