package be.nymouas.sms2mail;

import android.util.Log;

public class SendMailThread extends Thread {
	static private final String TAG=Constants.DEBUG_TAG;
	
	private String gmailuser;
	private String gmailpwd;	
	private String emailto;
	private String subject;
	private String body;
	
	public SendMailThread(String user, String pwd, String to, String Subject,String Message)
	{
		gmailuser=user;gmailpwd=pwd;emailto=to;subject=Subject;body=Message;
	}
	
	@Override
	public void run()
	{
		try {
    		GMailSender sender = new GMailSender(gmailuser,gmailpwd);    
    		sender.sendMail(subject,body,gmailuser+"@gmail.com",emailto);  
    	} catch (Exception e) {  
    		StringBuilder msg=new StringBuilder("");
    		msg.append(subject).append(" not sent");
    		if (e.getMessage()!=null) msg.append(" because of error : ").append(e.getMessage());           		 
    	    Log.d(TAG, msg.toString()); //log error  
    	}
	 
	}
	
}
