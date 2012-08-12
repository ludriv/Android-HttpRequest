package com.ludriv.ahttprequest.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FileUtils
{
	public static boolean localFileExists(Context context, String fileName)
	{
		return context.getFileStreamPath(fileName).exists();
	}
	
	public static String contentFromFile(Context context, String fileName) throws FileNotFoundException, IOException
	{
		StringBuilder builder = new StringBuilder();
		FileInputStream fis = context.openFileInput(fileName);
		
		byte[] buffer = new byte[1024];
		while (fis.read(buffer) != -1)
			builder.append(new String(buffer));
		fis.close();
		
		return builder.toString();
	}
	
	public static void writeIntoFile(Context context, String content, String fileName) throws FileNotFoundException, IOException
	{
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		fos.write(content.getBytes());
		fos.close();
	}
	
}
