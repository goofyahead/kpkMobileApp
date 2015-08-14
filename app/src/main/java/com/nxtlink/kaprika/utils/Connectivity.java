package com.nxtlink.kaprika.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.util.Log;

import com.nxtlink.kaprika.base.KaprikaApplication;

public class Connectivity {

	private static final String DEBUG_TAG = Connectivity.class.getName();

	public static String getDataFromUrl(String myurl) throws IOException {
		InputStream is = null;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d(DEBUG_TAG, "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			return new String(total);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public static void getFileFromUrlAndSave(String myUrl, String fileName) {
		InputStream input = null;
		OutputStream output = null;

		try {
			int count;
			URL url = new URL(myUrl);
			URLConnection conection = url.openConnection();
			conection.connect();
			// input stream to read file - with 8k buffer
			input = new BufferedInputStream(url.openStream(), 8192);
			// Output stream to write file
			output = KaprikaApplication.getAppContext().openFileOutput(fileName, Context.MODE_WORLD_READABLE);
			byte data[] = new byte[1024];
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}
			// flushing output
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// closing streams
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
