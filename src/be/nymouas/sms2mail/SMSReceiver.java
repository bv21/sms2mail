package be.nymouas.sms2mail;

/*
 *  This broadcast receiver is execute when a SMS is received !
 *  
 *  This class is based on the tutorial : http://www.androidcompetencycenter.com/2008/12/android-api-sms-handling/
 *  
 */

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Retrieve settings 
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String email=preferences.getString("email", "");
		String gmail=preferences.getString("gmail","");
		String gmailpwd=preferences.getString("gmailpwd","");
		Boolean isenabled=preferences.getBoolean("enable", false);
		//check if settings are ok and if the application is enabled
		if (isenabled && (email.length()>0) && (gmail.length()>0) && (gmailpwd.length()>0))
		{
			Bundle bundle = intent.getExtras();        
			SmsMessage[] msgs = null;			
	        if (bundle != null)
	        {
	            //---retrieve the SMS message received---
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            msgs = new SmsMessage[pdus.length];	            
	            if (msgs.length>0)
	            {          
	            	//retrieve sms
	            	msgs[0] = SmsMessage.createFromPdu((byte[])pdus[0]);
	            	StringBuilder str = new StringBuilder(8092);
	            	String address=msgs[0].getOriginatingAddress(); //from address/phone number
	            	String name=findcontact(address,context); //determine contact from the phone number 
	            	//e-mail subject
	     	        String subject=context.getResources().getString(R.string.sms_subject)+" "+(name.length()>0?name+" ("+address+")":address); 	     	        	     	        
	     	        str.append(msgs[0].getMessageBody().toString());	     	        
	            	if (msgs.length>1) //very long sms (several sms --> append) !!
	            	{
	            		for (int i=1; i<msgs.length; i++){
	            			msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	            			if (address.compareTo(msgs[i].getOriginatingAddress())!=0) //if all sms are not from the same address
	            			{
	            				address=msgs[i].getOriginatingAddress();
	            				name=findcontact(address,context);
	            				str.append(context.getResources().getString(R.string.sms_subject)).append(" ");
	            				if (name.length()>0) str.append(name).append(" (").append(address).append(")");	            					
	            				else str.append(address);
	            				str.append(" :\n");
	            			}	            			
	            			str.append(msgs[i].getMessageBody().toString());	            			
	            		}
	            	}           	
	            	//send e-mail now
	            	SendMailThread mailThread=new SendMailThread(gmail,gmailpwd,email,subject,str.toString());
	            	mailThread.start();
	            	/*try {
	            		GMailSender sender = new GMailSender(gmail,gmailpwd);    
	            		sender.sendMail(subject,str.toString(),gmail+"@gmail.com",email);  
	            	} catch (Exception e) {  
	            		StringBuilder msg=new StringBuilder("");
	            		msg.append(subject).append(" not sent");
	            		if (e.getMessage()!=null) msg.append(" because of error : ").append(e.getMessage());           		 
	            	    Log.d(TAG, msg.toString()); //log error  
	            	}*/
	              	
	            }
	        }

		}
	}
	
	//Find contact from the phone number 
	private String findcontact(String phoneNumber,Context context)
	{
		ContentResolver resolver = context.getContentResolver();
		String name="";
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cur=resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME},null,null,null);
		if (cur.moveToFirst()) {
			int phoneColumn = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name=cur.getString(phoneColumn);
		}
		return name;
	}
}
