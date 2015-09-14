package com.example.trafficalertsystem;

import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * The Class SoapManager sends the request to the server.
 */
public class SoapManager {

	/** The namespace. */
	String NAMESPACE; // Namespace of the Soap web service

	/** The url. */
	private String URL;

	/** The method name. */
	private String METHOD_NAME;

	/**
	 * Instantiates a new soap manager.
	 * 
	 * @param _namespace
	 *            the _namespace
	 * @param _url
	 *            the _url
	 * @param method_name
	 *            the method_name
	 */
	public SoapManager(String _namespace, String _url, String method_name) {
		NAMESPACE = _namespace;
		URL = _url;
		METHOD_NAME = method_name;
	}

	/**
	 * Send soap request.
	 * 
	 * @param request
	 *            the request
	 */
	public void sendSoapRequest(SoapObject request) {
		// method and wsUrl(web service Url) are both found in the WSDL file

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// set dotNet to true since the test web service I used was .NET
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		String SOAP_ACTION = "\"" + NAMESPACE + METHOD_NAME + "\"";
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send and receive the soap request/response to/from the server
	 * 
	 * @param request
	 *            the request
	 */
	public void sendAndReceive(SoapObject request) {
		// method and wsUrl(web service Url) are both found in the WSDL file

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		String SOAP_ACTION = "\"" + NAMESPACE + METHOD_NAME + "\"";
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Vector resp = (Vector) envelope.getResponse();
			int i = 0;
			while (resp.get(i) != null) {
				MainActivity.trafficAlert.add(resp.get(i).toString());
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
