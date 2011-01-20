package be.nymouas.sms2mail;

/*
 * MAIN Activity : show preference/settings for e-mail & gmail configuration 
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class SMS2Mail extends PreferenceActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        //create the preference window to configure the application
        addPreferencesFromResource(R.xml.preferences);        
    }
    
    //execute after the button home is pressed  
    protected void onStop()
    {
    	super.onStop();
    	RefreshWidgets();  //refresh the widgets   	
    	this.finish();		//close the application to clear memory since it is only a preference activity !
    }
    
    //execute after the button back is pressed (quit)
    protected void onDestroy()
    {    	
    	super.onDestroy();
    	RefreshWidgets();  //refresh the widgets
    }
  
    //menu creation (by pressing the menu button)
    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Menu creation     	 
    	MenuInflater inflater = getMenuInflater();    		 
    	// the inflater parses the xml menu    		 
    	inflater.inflate(R.menu.mainmenu, menu);    		 
    	return true;    	
    }
    
    //action when a menu is selected
    @Override    
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
    	
    	case R.id.itemQuit: //menu quit 
    		RefreshWidgets();   //refresh menu
    		this.finish();	  //close the application
    		break;
    		
    	case R.id.itemHelp:
    		ShowHelp(); //show help menu
    		break;
    	
    	case R.id.itemTestConfig:  //menu test config		
    		TestConfig(); //test the configuration  
    		break;
    	}
    	return true;
    }
    
    //refresh the widgets
    private void RefreshWidgets()
    {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isenabled=preferences.getBoolean("enable", false);
		//sent the intent to update the menu 
		Intent message = new Intent(Constants.ENABLE_CHANGED_MESSAGE);
        message.putExtra(Constants.ENABLE_EXTRA, isenabled);              
        this.sendBroadcast(message);
    } 
    
    //Show help menu
    private void ShowHelp()
    {
    	Toast.makeText(this, getResources().getString(R.string.help_txt), Toast.LENGTH_SHORT).show();
    }
    
    //Test the configuration by sending an e-mail 
    private void TestConfig() 
    {     
    	//run the test config in a thread in order to show a progress bar
    	TestMailThread testmail=new TestMailThread(this);
    	testmail.start();   	
    }
         
}