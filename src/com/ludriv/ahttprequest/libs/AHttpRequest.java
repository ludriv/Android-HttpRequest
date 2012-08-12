package com.ludriv.ahttprequest.libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AHttpRequest
{
	private Context _context;
	private int _tag;
	
	private String _url;
	private HashMap<String, Object> _postValues;
	private boolean _isAsynchronous;
	private boolean _displayLoader;
	private String _loadingText;
	
	private OnAHttpRequestListener _listener;
	
	
	/* Constructors */
	public AHttpRequest(Context context)
	{
		_context = context;
		_postValues = new HashMap<String, Object>();
		_isAsynchronous = false;
		_displayLoader = false;
		_loadingText = "Loading";
		
		_listener = null;
	}
	
	public AHttpRequest(Context context, String url)
	{
		this(context);
		_url = url;
	}
	
	/* Setters */
	public void setTag(int tag)
	{
		_tag = tag;
	}
	
	public void setUrl(String url)
	{
		_url = url;
	}
	
	public void isAsynchronous(boolean isAsynchronous)
	{
		_isAsynchronous = isAsynchronous;
	}
	
	public void setOnHttpRequestListener(OnAHttpRequestListener listener)
	{
		_listener = listener;
	}
	
	public void isDisplayLoader(boolean displayLoader)
	{
		_displayLoader = displayLoader;
	}
	
	public void setLoadingText(String loadingText)
	{
		_loadingText = loadingText;
	}
	
	/* Getters */
	
	public int getTag()
	{
		return _tag;
	}
	
	/* */
	
	public void addPostValue(String key, Object value)
	{
		_postValues.put(key, value);
	}
	
	
	public void start() throws ClientProtocolException, IOException
	{
		final DefaultHttpClient httpClient = new DefaultHttpClient();
    	final HttpPost httpPost = new HttpPost(_url);
    	
    	if (!_postValues.isEmpty())
    	{
    		ArrayList<NameValuePair> paramsArray = new ArrayList<NameValuePair>();
        	for (String key : _postValues.keySet())
        		paramsArray.add(new BasicNameValuePair(key, String.valueOf(_postValues.get(key))));
        	httpPost.setEntity(new UrlEncodedFormEntity(paramsArray));
    	}
		
    	if (_isAsynchronous)
    	{
    		AsyncTask<Void, Void, Void> aTask = new AsyncTask<Void, Void, Void>()
    		{
    			ProgressDialog dialog;
    			int statusCode;
    			String response;
    			
    			@Override
    			protected void onPreExecute()
    			{
    				super.onPreExecute();
    				if (_displayLoader)
    					dialog = ProgressDialog.show(_context, "", _loadingText);
    			}
    			
    			protected Void doInBackground(Void... params) 
    			{
    				try
					{
						HttpResponse httpResponse = httpClient.execute(httpPost);
						statusCode = httpResponse.getStatusLine().getStatusCode();
						
						if (statusCode == HttpStatus.SC_OK)
						{
				        	response = getHttpContent(httpResponse);
						}
					}
					catch (ClientProtocolException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
    				
    				return null;
    			};
    			
    			@Override
    			protected void onPostExecute(Void result)
    			{
    				super.onPostExecute(result);
    				if (_displayLoader)
    					dialog.dismiss();
    				
    				if (_listener != null)
    				{	
    					if (statusCode == HttpStatus.SC_OK)
    						_listener.onAHttpRequestSuccess(AHttpRequest.this, response);
    					else
    						_listener.onAHttpRequestFail(AHttpRequest.this, statusCode);
    				}
    			}
    		};
    		aTask.execute();
    	}
    	else
    	{
    		HttpResponse httpResponse = httpClient.execute(httpPost);
    		int statusCode = httpResponse.getStatusLine().getStatusCode();
    		
    		if (statusCode == HttpStatus.SC_OK)
    		{
    			String response = getHttpContent(httpResponse);
    			if (_listener != null)
					_listener.onAHttpRequestSuccess(AHttpRequest.this, response);
    		}
    		else
    		{
    			if (_listener != null)
					_listener.onAHttpRequestFail(AHttpRequest.this, statusCode);
    		}
    	}
	}
	
	private String getHttpContent(HttpResponse httpResponse) throws IllegalStateException, IOException
	{
    	HttpEntity httpEntity = httpResponse.getEntity();
    	InputStream inputStream = httpEntity.getContent();
    	
    	BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
    	String line;
    	StringBuilder stringBuilder = new StringBuilder();
		
    	while ((line = bufferReader.readLine()) != null) 
    		stringBuilder.append(line);
  
    	bufferReader.close();
    	line = null;
    	
    	return stringBuilder.toString();
	}
}
