package colley.chisholm.diploma.android_proximity_alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {
	
	private static final int NOTIFICATION_ID = 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//	    Ringtone r = RingtoneManager.getRingtone(context, notification);
//	    r.play();
		
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		if (entering) {
			Log.d(getClass().getSimpleName(), "entering");
		} else {
			Log.d(getClass().getSimpleName(), "exiting");
		}
		
		
		
		
//		double lat = Double.parseDouble(intent.getExtras().getString("lat"));
//		double lng = Double.parseDouble(intent.getExtras().getString("long"));
		
		String s = "You have entered " + intent.getStringExtra("lat") + " " + intent.getStringExtra("long");

		
		Notification.Builder mBuilder =
		        new Notification.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("PROXIMITY ALERT")
		        .setContentText(s)
		        .setAutoCancel(true)
		        .setSound(sound);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, AlertActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(AlertActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		
		//notificationManager.notify(NOTIFICATION_ID, mBuilder);
		
		
	}
	
	
	
//	private Notification createNotification(Context context) {
//		Notification.Builder mBuilder =
//		        new Notification.Builder(context)
//		        .setSmallIcon(R.drawable.ic_launcher)
//		        .setContentTitle("My notification")
//		        .setContentText("Hello World!");
//		// Creates an explicit intent for an Activity in your app
//		Intent resultIntent = new Intent(context, AlertActivity.class);
//
//		// The stack builder object will contain an artificial back stack for the
//		// started Activity.
//		// This ensures that navigating backward from the Activity leads out of
//		// your application to the Home screen.
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(AlertActivity.class);
//		// Adds the Intent that starts the Activity to the top of the stack
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//		        stackBuilder.getPendingIntent(
//		            0,
//		            PendingIntent.FLAG_UPDATE_CURRENT
//		        );
//		mBuilder.setContentIntent(resultPendingIntent);
//		NotificationManager mNotificationManager =
//		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		// mId allows you to update the notification later on.
//		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//	}

}
