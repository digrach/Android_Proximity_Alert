package colley.chisholm.diploma.android_proximity_alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {
	
	private static final int NOTIFICATION_ID = 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		if (entering) {
			Log.d(getClass().getSimpleName(), "entering");
		} else {
			Log.d(getClass().getSimpleName(), "exiting");
		}
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);  
////		Notification notification = createNotification();
////		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
////		notification.setLatestEventInfo(context,"Proximity Alert!", "You are near your point of interest.", pendingIntent);
//		
//		Notification noti = new Notification.Builder(context)
//        .setContentTitle("Proximity Alert!")
//        .setContentText("You are near your point of interest.")
//        //.setSmallIcon(R.drawable.new_mail)
//        //.setLargeIcon(aBitmap)
//        .build();
		
		Notification.Builder mBuilder =
		        new Notification.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("My notification")
		        .setContentText("Hello World!");
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
