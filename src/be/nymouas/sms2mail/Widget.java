package be.nymouas.sms2mail;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;


//Widget class for creation and update 
public class Widget extends AppWidgetProvider {
	
	public static final String TAG=Constants.DEBUG_TAG; //DEBUG Tag
	
	public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //special intent when the enable is changed ! (allow update widgets outside and inside this class)
        if (Constants.ENABLE_CHANGED_MESSAGE.equals(intent.getAction())) {        	
            Bundle extras = intent.getExtras();            
            if (extras != null && extras.containsKey(Constants.ENABLE_EXTRA)) {
            	boolean isEnabled = extras.getBoolean(Constants.ENABLE_EXTRA);    
                Log.d(TAG,"ENABLE_CHANGED_MESSAGE isEnabled : "+String.valueOf(isEnabled));
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                int[] widgetIds = manager.getAppWidgetIds(new ComponentName(context, Widget.class));                
                showWidget(context, manager, widgetIds, isEnabled); //update the widget
            }        	
        } else //special intent when enable has to be changed (i.e. when the widget is clicked)  
        	if (Constants.CHANGE_ENABLE_REQUEST.equals(intent.getAction()))
	        {
	        	Log.d(TAG,"CHANGE_ENABLE_REQUEST");
	        	//retrieve enable value from the preferences
	        	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    		Boolean isenabled=preferences.getBoolean("enable", false);
	    		SharedPreferences.Editor editor = preferences.edit();   	      	
	   	      	editor.putBoolean("enable", !isenabled); //toggle the enable value !
	   	      	editor.commit();
	   	      	
	   	      	//sent intent to update the widgets (see previous intent)
	        	Intent message = new Intent(Constants.ENABLE_CHANGED_MESSAGE);
	            message.putExtra(Constants.ENABLE_EXTRA, !isenabled);              
	            context.sendBroadcast(message);
	        }
    }


	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Boolean isenabled=preferences.getBoolean("enable", false);
		//create the widget
		showWidget(context, appWidgetManager, appWidgetIds, isenabled);
	}
	
	private void showWidget(Context context, AppWidgetManager manager, int[] widgetIds, boolean isEnabled) {
		//create the widget 
        RemoteViews views = createRemoteViews(context, isEnabled);
        manager.updateAppWidget(widgetIds, views);
    }
	
	private RemoteViews createRemoteViews(Context context, boolean isEnabled) {        
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    	int iconId = isEnabled ? R.drawable.wdg_on : R.drawable.wdg_off; //select the icon from enable              
        views.setImageViewResource(R.id.WidgetButton, iconId);   
        
        //intent to send when the widget is clicked (toggle between enable and disable)
        Intent msg = new Intent(Constants.CHANGE_ENABLE_REQUEST);
        PendingIntent intent = PendingIntent.getBroadcast(context, -1 /* not used */, msg, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.WidgetButton, intent);
        return views;
    }
	
	

}
