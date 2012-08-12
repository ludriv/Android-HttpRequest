package com.ludriv.ahttprequest.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.ludriv.ahttprequest.R;
import com.ludriv.ahttprequest.libs.AHttpRequest;
import com.ludriv.ahttprequest.libs.OnAHttpRequestListener;
import com.ludriv.ahttprequest.utils.StringUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends TemplateActivity implements OnClickListener, OnAHttpRequestListener
{
	TextView logTextView;
	Button asyncButton;
	Button syncButton;
	Button clearButton;
	
	static final int ASYNC_TAG = 0;
	static final int SYNC_TAG = 1;
	
	String url = "http://maps.googleapis.com/maps/api/geocode/xml?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=false";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		asyncButton = (Button) findViewById(R.id.asyncButton);
		asyncButton.setOnClickListener(this);
		
		syncButton = (Button) findViewById(R.id.syncButton);
		syncButton.setOnClickListener(this);
		
		clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(this);
		
		logTextView = (TextView) findViewById(R.id.logTextView);
		
	}
	
	@Override
	public void onClick(View v)
	{
		if (v.equals(asyncButton))
		{
			if (checkInternetConnection())
				asyncLoad();
		}
		else if (v.equals(syncButton))
		{
			if (checkInternetConnection())
				syncLoad();
		}
		else if (v.equals(clearButton))
		{
			logTextView.setText("");
		}
	}
	
	public void asyncLoad()
	{
		AHttpRequest aRequest = new AHttpRequest(this);
		aRequest.setUrl(url);
		aRequest.isAsynchronous(true);
		aRequest.isDisplayLoader(true);
		aRequest.setLoadingText("Loading in progress...");
		aRequest.setOnHttpRequestListener(this);
		aRequest.setTag(ASYNC_TAG);
		
		try
		{
			aRequest.start();
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void syncLoad()
	{
		// views are blocked while execution of this function

		AHttpRequest aRequest = new AHttpRequest(this);
		aRequest.setUrl(url);
		aRequest.isAsynchronous(false);
		aRequest.setOnHttpRequestListener(this);
		aRequest.setTag(SYNC_TAG);
		
		try
		{
			aRequest.start();
			
			// see behavior of synchronous request with this instruction : 
			Thread.sleep(2000);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onAHttpRequestSuccess(AHttpRequest request, String response)
	{
		int requestTag = request.getTag();
		if (requestTag == ASYNC_TAG)
		{
			logTextView.setText("Response from Asynchronous method:\n\n" + response);
		}
		else if (requestTag == SYNC_TAG)
		{
			logTextView.setText("Response from Synchronous method:\n\n" + response);
		}
	}

	@Override
	public void onAHttpRequestFail(AHttpRequest request, int code)
	{
		Toast.makeText(this, "request fail: " + code, debugToastLength).show();
	}
}
