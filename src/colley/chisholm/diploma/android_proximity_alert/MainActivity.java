package colley.chisholm.diploma.android_proximity_alert;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(getClass().getSimpleName(),"onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(getClass().getSimpleName(),"onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(getClass().getSimpleName(),"onOptionsItemSelected");
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		static LocationManager lm;
		static Activity activity;
		private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
		private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
//		private static final String PROX_ALERT_INTENT = "colley.chisholm.diploma.android_proximity_alert.AlertActivity";
		private static final String PROX_ALERT_INTENT = "colley.chisholm.diploma.android_proximity_alert";
		private static final long POINT_RADIUS = 1000; // in Meters
		private static final long PROX_ALERT_EXPIRATION = -1;
		private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
		private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
		private static final NumberFormat nf = new DecimalFormat("##.########");
		private static TextView txtLat;
		private static TextView txtLong;
		private static Button btnSaveCoords;
		private static Button btnFindCoords;
		
		//public LocationClient mLocationClient;
		
		private void addingMock() {
//			mLocationClient.connect();
//			mLocationClient.setMockMode(true);
		}

		private void addProximityAlert(double latitude, double longitude) {
			Log.d(getClass().getSimpleName(),"addProximityAlert");
			Intent intent = new Intent(PROX_ALERT_INTENT);
			intent.putExtra("lat", latitude);
			intent.putExtra("long", longitude);
			
			Toast.makeText(activity, "Putting " + Double.toString(latitude) + " " + Double.toString(longitude), Toast.LENGTH_SHORT).show();
			
			PendingIntent proximityIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
			
			lm.addProximityAlert(latitude, longitude, POINT_RADIUS, PROX_ALERT_EXPIRATION, proximityIntent);
			IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
			activity.registerReceiver(new ProximityIntentReceiver(), filter); /////////////////////////////////////////////////

		}

		private void saveProximityAlertPoint() {
			Log.d(getClass().getSimpleName(),"saveProximityAlertPoint");
			Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (l == null) {
				Toast.makeText(activity, "No last location. Aborting...", Toast.LENGTH_SHORT).show();
				return;
			}
			saveCoordinatesInPreferences((float)l.getLatitude(),(float)l.getLongitude());
			addProximityAlert(l.getLatitude(), l.getLongitude());
		}

		private void populateCoordinatesFromLastKnownLocation() {
			Log.d(getClass().getSimpleName(),"populateCoordinatesFromLastKnownLocation");
			Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (l != null) {
				txtLat.setText(nf.format(l.getLatitude()));
				txtLong.setText(nf.format(l.getLongitude()));
			}
		}

		private void saveCoordinatesInPreferences(float latitude, float longitude) {
			Log.d(getClass().getSimpleName(),"saveCoordinatesInPreferences");
			SharedPreferences prefs = getActivity().getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = prefs.edit();
			prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
			prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
			prefsEditor.commit();
		}

		private static Location retrievelocationFromPreferences() {
			Log.d("Fragment","retrievelocationFromPreferences");
			SharedPreferences prefs = activity.getSharedPreferences(activity.getClass().getSimpleName(), Context.MODE_PRIVATE);
			Location l = new Location("POINT_LOCATION");
			l.setLatitude(prefs.getFloat(POINT_LATITUDE_KEY, 0));
			l.setLongitude(prefs.getFloat(POINT_LONGITUDE_KEY, 0));
			return l;
		}


		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.d(getClass().getSimpleName(),"onCreateView");
			activity = getActivity();
			lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, mLocationListener);

			View rootView = inflater.inflate(R.layout.fragment_main, container, false);

			txtLat = (TextView)rootView.findViewById(R.id.txtlattext);
			txtLong = (TextView)rootView.findViewById(R.id.txtlongtext);
			btnSaveCoords = (Button)rootView.findViewById(R.id.btnsavecoords);

			btnSaveCoords.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					saveProximityAlertPoint();
				}
			});

			btnFindCoords = (Button)rootView.findViewById(R.id.btnfindcoords);

			btnFindCoords.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					populateCoordinatesFromLastKnownLocation();
				}
			});
			
			addingMock();

			return rootView;
		}

		final static LocationListener mLocationListener = new LocationListener() { // http://developer.android.com/reference/android/location/LocationListener.html

			@Override
			public void onLocationChanged(Location location) {
				Location pointLocation = retrievelocationFromPreferences();
				float distance = location.distanceTo(pointLocation);
				Toast.makeText(activity, "Distance from Point:" + distance, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

		};




	}

}
