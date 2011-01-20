package be.nymouas.sms2mail;

/*
 * Constants for the application (new intent constants & other ones)
 */

public class Constants {
	//intent to update the widgets due to a change in the enable value
	public static final String ENABLE_CHANGED_MESSAGE = "be.nymouas.sms2mail.intent.action.ENABLE_CHANGED";
	
	//content the current value of ENABLE (for widgets) used by the the intent ENABLE_CHANGED_MESSAGE
	public static final String ENABLE_EXTRA = "be.nymouas.sms2mail.intent.extra.ENABLE"; 
	
	//intent to toggle the enable value !
    public static final String CHANGE_ENABLE_REQUEST = "be.nymouas.sms2mail.intent.action.CHANGE_REQUEST";
    
    //default debug TAG
    public static final String DEBUG_TAG="SMS2MAIL";
}	
