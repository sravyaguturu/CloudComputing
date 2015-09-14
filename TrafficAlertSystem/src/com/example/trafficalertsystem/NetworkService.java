package com.example.trafficalertsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

/**
 * The Class NetworkService handles the interaction with the server.
 */
public class NetworkService extends IntentService {

	/** The pack. */
	private DatagramPacket pack = null;

	/** The network result. */
	private int networkResult = Activity.RESULT_CANCELED;

	/** The intent. */
	private Intent intent;

	/** The m handler. */
	Handler mHandler = null;

	/** The context. */
	Context context = this;

	/** The multi socket. */
	MulticastSocket multiSocket = null;

	/** The activity. */
	public static MainActivity activity;

	/** The receive messages. */
	public static boolean receiveMessages = true;

	/** The send messages. */
	public static boolean sendMessages = true;

	/**
	 * Instantiates a new network service.
	 */
	public NetworkService() {
		super("NetworkService");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		this.intent = intent;

		while (receiveMessages) {
			handleMulticastConnection();

		}

	}

	/**
	 * Handles multicast connection.
	 * 
	 * @return the string builder
	 */
	private StringBuilder handleMulticastConnection() {
		StringBuilder message = new StringBuilder();
		boolean isMessage = false;
		try {
			multiSocket = new MulticastSocket(Constants.PORT_NO);
			multiSocket.setInterface(InetAddress.getLocalHost());

			multiSocket.joinGroup(InetAddress.getByName(Constants.GROUP_IP));
			byte buf[] = new byte[1024];
			pack = new DatagramPacket(buf, buf.length);
			multiSocket.receive(pack);
			multiSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Splits the data packet received from the server and extracts the
		// needed info

		StringBuilder IpandPort = new StringBuilder();
		if (pack != null) {
			byte[] packetData = pack.getData();

			for (int i = 0; i < packetData.length; i++) {
				char digit = (char) packetData[i];
				if (Character.isDigit(digit) && !isMessage) {
					IpandPort.append(digit);
				} else if ((digit == '.' || digit == ':') && !isMessage) {
					IpandPort.append(digit);
				} else if (digit == '$') {
					break;

				} else {
					isMessage = true;
					message.append(digit);
				}
			}
			networkResult = Activity.RESULT_OK;

			final String[] strs = message.toString().split("#");

			final String finalMessage = strs[0];

			String isAlertInRange = checkRadius(strs[1], strs[2]);

			if (isMessage) {
				isMessage = false;

				if ((isAlertInRange.equalsIgnoreCase(Constants.SAME) && !MainActivity.messageSent)
						|| (isAlertInRange.equalsIgnoreCase(Constants.IN))) {

					mHandler = new Handler(getMainLooper());

					mHandler.post(new Runnable() {
						@Override
						public void run() {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getApplicationContext());
							builder.setTitle(Constants.TRAFFIC_ALERT);
							builder.setMessage(finalMessage);

							builder.setPositiveButton(Constants.OK,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
										}
									})
									.setNegativeButton(
											Constants.STOP_ALERT,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
													receiveMessages = false;
													MainActivity.startAlerts
															.setVisibility(View.VISIBLE);
													MainActivity.t2
															.setVisibility(View.VISIBLE);

												}
											})
									.setNeutralButton(
											Constants.VIEW_MAPS,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {

													activity.displayMaps();
													MainActivity
															.displayCoordinates(
																	Double.parseDouble(strs[1]),
																	Double.parseDouble(strs[2]),
																	"");
												}

											});

							AlertDialog dialog = builder.create();
							dialog.getWindow()
									.setType(
											WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

						}
					});

				}
			}

		}

		return IpandPort;
	}

	/**
	 * Checks whether the given locations is inside 20 Mi radius from the
	 * current location.
	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @return the string - SAME, IN, OUT - based on whether the location is in
	 *         the radius or out
	 */
	private String checkRadius(final String latitude, final String longitude) {

		float radius = 20.0f;
		LocationManager mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

		List<String> providers = mgr.getProviders(true);
		for (String provider : providers) {

			Location current = mgr.getLastKnownLocation(provider);
			Location received = new Location("");
			received.setLatitude(Double.valueOf(latitude));
			received.setLongitude(Double.parseDouble(longitude));
			float distance = received.distanceTo(current)
					* Constants.MILES_CONVERTER;

			if (distance == 0.0f) {
				return Constants.SAME;
			}
			if (distance < radius) {
				return Constants.IN;
			}

		}

		return Constants.OUT;
	}

}
