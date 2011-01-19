package be.nymouas.sms2mail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class TestMailThread extends Thread {

	private Activity activity;
	private ProgressDialog progressdialog;
	private String toastmsg="";
	
	public TestMailThread(Activity activity_) {		
		activity=activity_;		
		progressdialog=ProgressDialog.show(activity,"",activity.getResources().getString(R.string.test_bar),true,false);		
	}
	
	@Override
	public void run()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);		
		String gmail=preferences.getString("gmail","");		
		String gmailpwd=preferences.getString("gmailpwd","");
		String email=preferences.getString("email", "");
		//check if the configuration is ok 
		if ((gmail.length()>0) && (gmailpwd.length()>0) && (email.length()>0))
		{			
			try {			
				//send the test e-mail and show the result
        		GMailSender sender = new GMailSender(gmail,gmailpwd);    
        		sender.sendMail(activity.getResources().getString(R.string.test_mail_subject),activity.getResources().getString(R.string.test_mail_body),gmail+"@gmail.com",email);        		
        		toastmsg=activity.getResources().getString(R.string.test_ok).replace("GMAIL",gmail).replace("EMAIL",email);        		
        	} catch (Exception e) {        		
        		String msg=e.getMessage();
        		if (msg==null) msg="";        		      
        		toastmsg=activity.getResources().getString(R.string.test_notok).replace("GMAIL", gmail).replace("MSG",msg);        		
        	}
		}	
		// the configuration is not set correctly !
		else toastmsg=activity.getResources().getString(R.string.test_nodata);
		
		progressdialog.dismiss();		
		
		//needed to show a toast from a thread !
		activity.runOnUiThread(new Runnable() {
		    public void run() {
		        Toast.makeText(activity, toastmsg, Toast.LENGTH_LONG).show();
		    }
		});
	}

}
