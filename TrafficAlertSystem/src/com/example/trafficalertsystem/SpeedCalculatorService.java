package com.example.trafficalertsystem;

import java.util.List;

import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.SoapObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The Class SpeedCalculatorService.
 */
public class SpeedCalculatorService extends IntentService {

	

	/** The mgr. */
	private LocationManager mgr = null;

	/** The context. */
	Context context = this;

	/** The m handler. */
	Handler mHandler = null;

	/** The send messages. */
	public static boolean sendMessages = true;

	/** The prev location. */
	public static Location prevLocation = null;

	/** The activity. */
	public static MainActivity activity;

	/** The prev time. */
	long prevTime = 0;

	/** The time taken. */
	long timeTaken;

	/** The speed. */
	float speed;

	/** The countof less speed. */
	private static int countofLessSpeed = 0;

	/** The time takenfor calculation. */
	public long timeTakenforCalculation;

	/** The speedfor calculation. */
	public float speedforCalculation;

	/** The timein zero speed. */
	private long timeinZeroSpeed = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onCreate()
	 */
	public void onCreate() {

		super.onCreate();
	}

	/**
	 * Instantiates a new speed calculator service.
	 */
	public SpeedCalculatorService() {
		super("SpeedCalculator");
	}

	/**
	 * Gets the mac address of the device.
	 * 
	 * @param context
	 *            the context
	 * @return the mac address
	 */
	public String getMacAddress(Context context) {
		WifiManager wimanager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		String macAddress = wimanager.getConnectionInfo().getMacAddress();
		if (macAddress == null) {
			macAddress = " ";
		}
		return macAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(final Intent workIntent) {

		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		// check for availability of atleast one of the providers

		List<String> providers = mgr.getProviders(true);
		for (String provider : providers) {
			LocationListener locationListener = new LocationListener() {

				public void onLocationChanged(final Location location) {

					float distance = 0.0f;
					if (prevLocation != null) {

						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {

								MainActivity.displayCoordinates(
										location.getLatitude(),
										location.getLongitude(), "Test");
								
							}
						});
						distance = location.distanceTo(prevLocation) * Constants.MILES_CONVERTER;

						timeTaken = (System.currentTimeMillis() - prevTime) / 1000;

						speed = (distance / timeTaken) * (36000);

						if (speed < 10.0) {

							activity.runOnUiThread(new Runnable() {

								// displayes a small toast of the current speed
								@Override
								public void run() {
									Toast.makeText(
											getApplicationContext(),
											"Speed :: " + String.valueOf(speed),
											Toast.LENGTH_LONG).show();								

								}
							});
							//if the speed is less than 2Mi then count the # of times the device is in the low speed
							
							if (speed < 2.0) {
								timeinZeroSpeed = timeinZeroSpeed + timeTaken;
								// speed=0;
							} else {
								countofLessSpeed++;
								timeinZeroSpeed = 0;
							}
						} else {
							timeinZeroSpeed = 0;
							countofLessSpeed = 0;
						}

					}

					prevLocation = location;

					prevTime = System.currentTimeMillis();
					
					// if the speed is less than 2 miles for 10s, then a traffic is detected and the message is sent to the server

					if (countofLessSpeed > 10) {

						new NetworkTask(prevLocation.getLatitude(),
								prevLocation.getLongitude(),
								getMacAddress(context)).execute();

						countofLessSpeed = 0;

					}

					if (timeinZeroSpeed > 100) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								MainActivity.resetDrivingStaus();

							}
						});

					}

				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}

				public void onProviderEnabled(String provider) {

				}

				public void onProviderDisabled(String provider) {

				}

			};

			mgr.requestLocationUpdates(provider, 30 * 1000, 0, locationListener);

			Looper.loop();
		}

	}

	// Message Formatter

	/**
	 * The Class NetworkTask.
	 */
	private class NetworkTask extends AsyncTask<String, Void, Void> {

		/** The latitude. */
		private double latitude;

		/** The longitude. */
		private double longitude;

		/** The mac address. */
		private String macAddress;

		/**
		 * Instantiates a new network task which sends a soap request to the server
		 * 
		 * @param latitude
		 *            the latitude
		 * @param longitude
		 *            the longitude
		 * @param macAddress
		 *            the mac address
		 */
		public NetworkTask(double latitude, double longitude, String macAddress) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.macAddress = macAddress;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(String... params) {
			SoapManager soapManager = new SoapManager(Constants.NAMESPACE, Constants.URL,
					Constants.METHOD_NAME_RECEIVE);
			SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_RECEIVE);
			
			//setting the neeeded attributes

			AttributeInfo attr = new AttributeInfo();
			attr.setName(Constants.LATITUDE);
			attr.setValue(latitude);
			attr.setType(double.class);

			AttributeInfo attr1 = new AttributeInfo();
			attr1.setName(Constants.LONGITUDE);
			attr1.setValue(longitude);
			attr1.setType(double.class);

			AttributeInfo attr2 = new AttributeInfo();
			attr2.setName(Constants.MACADDRESS);
			attr2.setValue(macAddress);
			attr2.setType(String.class);

			request.addAttribute(attr);
			request.addAttribute(attr1);
			request.addAttribute(attr2);

			
			soapManager.sendSoapRequest(request);
			MainActivity.messageSent = true;
			return null;
		}
	}

}