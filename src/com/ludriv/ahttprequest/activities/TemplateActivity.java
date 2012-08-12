package com.ludriv.ahttprequest.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class TemplateActivity extends Activity
{
	protected final int debugToastLength = Toast.LENGTH_SHORT;
	
	public boolean checkInternetConnection()
	{
		if (checkCallingOrSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED)
		{
			Toast.makeText(this, "[debug] " + Manifest.permission.INTERNET + " : not granted", debugToastLength).show();
			return false;
		}
		else if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED)
		{
			Toast.makeText(this, "[debug] " + Manifest.permission.ACCESS_NETWORK_STATE + " : not granted", debugToastLength).show();
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    boolean isConnected = (netInfo != null && netInfo.isConnected());
	    
	    if (!isConnected)
	    {
	    	Toast.makeText(this, "No internet connection", debugToastLength).show();
	    	return false;
	    }
	    return true;
	}
	
	// 
	
	public void shareLink(String link)
	{
		final Intent MessIntent = new Intent(Intent.ACTION_SEND);
    	MessIntent.setType("text/plain");
    	MessIntent.putExtra(Intent.EXTRA_TEXT, link);
    	startActivity(Intent.createChooser(MessIntent, "Share with..."));
	}
}
