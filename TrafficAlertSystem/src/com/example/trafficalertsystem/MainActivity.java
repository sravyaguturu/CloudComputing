package com.example.trafficalertsystem;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * The Class MainActivity.
 */
public class MainActivity extends ActionBarActivity {

	/** The start driving. */
	public static Button startDriving;

	/** The t1. */
	TextView t1;

	/** The get alerts. */
	public static Button getAlerts;

	/** The t2. */
	public static TextView t2;

	/** The message sent. */
	public static boolean messageSent = false;

	/** The main context. */
	public Context mainContext = this;

	/** The activity. */
	public MainActivity activity = this;

	/** The start alerts. */
	public static Button startAlerts;

	/** The table layout. */
	public static TableLayout tableLayout;

	/** The google map. */
	public static GoogleMap googleMap;

	/** The text array. */
	int[][] textArray = { { R.id.TextView21, R.id.TextView23 },
			{ R.id.TextView31, R.id.TextView33 }, };

	/** The traffic alert. */
	public static List<String> trafficAlert = new ArrayList<String>();

	/**
	 * Displays the map as the background of the application.
	 */
	public void displayMaps() {
		Intent openMain = new Intent();
		openMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(openMain);

	}
	/**
	 * initializes all the variables to the corresponding resources
	 */
	
	public void initializeAall()
	{
		startDriving = (Button) findViewById(R.id.button1);
		startAlerts = (Button) findViewById(R.id.button2);
		getAlerts = (Button) findViewById(R.id.button3);
		tableLayout = (TableLayout) findViewById(R.id.table);
		try {
			initializeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	/**
	 * On create.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SpeedCalculatorService.activity = this;
		NetworkService.activity = this;
	    initializeAall();
		tableLayout.setVisibility(View.INVISIBLE);
		startAlerts.setVisibility(View.INVISIBLE);
		t1 = (TextView) findViewById(R.id.text_view1);
		t2 = (TextView) findViewById(R.id.text_view2);
		t2.setVisibility(View.INVISIBLE);
		
		// listener class for START ALERTS button
		
		startAlerts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startAlerts.setVisibility(View.INVISIBLE);
				t2.setVisibility(View.INVISIBLE);
				NetworkService.receiveMessages = true;

			}

		});
		
		// listener class for GET alerts button

		getAlerts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new getRecentTraffic().execute();

			}

		});
		
		// listener class for START DRIVING button


		startDriving.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent mServiceIntent = new Intent(mainContext,
						SpeedCalculatorService.class);
				Intent mServiceIntent1 = new Intent(mainContext,
						NetworkService.class);
				
				//toggle between start and stop driving

				if (startDriving.getText().toString()
						.equalsIgnoreCase(Constants.START_DRIVING)) {
					mainContext.startService(mServiceIntent);

					mainContext.startService(mServiceIntent1);
					NetworkService.receiveMessages = true;
					SpeedCalculatorService.sendMessages = true;
					startDriving.setText(Constants.STOP_DRVING);

				} else if (startDriving.getText().toString()
						.equalsIgnoreCase(Constants.STOP_DRVING)) {
					NetworkService.receiveMessages = false;
					SpeedCalculatorService.sendMessages = true;
					startDriving.setText(Constants.START_DRIVING);

				}

			}
		});

	}

	/**
	 * Initializes the map as the background of the application.
	 */
	private void initializeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);

			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						Constants.TOAST_MAP_UNAVAILABLE, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	/**
	 * On resume.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		initializeMap();
	}

	/**
	 * Resets the driving staus.
	 */
	public static void resetDrivingStaus() {

		NetworkService.receiveMessages = false;
		SpeedCalculatorService.sendMessages = true;
		startDriving.setText(Constants.START_DRIVING);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	/**
	 * On create options menu.
	 *
	 * @param menu the menu
	 * @return true, if successful
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	/**
	 * On options item selected.
	 *
	 * @param item the item
	 * @return true, if successful
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Displays the retrieved traffic list in a table

	/**
	 * Display the traffic list nearer to the current location.
	 */
	public void displayTrafficList() {

		int i = 0;
		for (String area : trafficAlert) {

			String[] strs = area.split("#");
			TextView t1 = (TextView) findViewById(textArray[i][0]);

			TextView t3 = (TextView) findViewById(textArray[i][1]);
			t1.setText(strs[0]);

			t3.setText(strs[1].substring(11));
			i++;

			displayCoordinates(Double.parseDouble(strs[2]),
					Double.parseDouble(strs[3]), strs[0]);

		}
		tableLayout.setVisibility(View.VISIBLE);

	}

	/**
	 * Displays the coordinates of a particular locations in the map.
	 * 
	 * @param lat
	 *            the lat
	 * @param longi
	 *            the longi
	 * @param location
	 *            the location
	 */
	public static void displayCoordinates(double lat, double longi,
			String location) {
		LatLng coordinates = new LatLng(lat, longi);
		CameraUpdate currentlocation = CameraUpdateFactory.newLatLngZoom(
				coordinates, 5);
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(lat, longi)).title(location);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(coordinates).zoom(50).bearing(90).tilt(30).build();
		MainActivity.googleMap.addMarker(marker);
		MainActivity.googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		MainActivity.googleMap.animateCamera(currentlocation);

	}

	/**
	 * The Class getRecentTraffic retrieves the traffic from the server.
	 */
	private class getRecentTraffic extends AsyncTask<String, Void, Void> {

		

		/** The resp. */
		private String resp;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected Void doInBackground(String... params) {
			SoapManager soapManager = new SoapManager(Constants.NAMESPACE, Constants.URL,
					Constants.METHOD_NAME);
			SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME);
			soapManager.sendAndReceive(request);

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					displayTrafficList();

				}
			});

		}

	}

	/**
	 * Gets the activity.
	 * 
	 * @return the activity
	 */
	public MainActivity getactivity() {
		return this;
	}

}
