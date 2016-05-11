package com.ptobucks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import com.ptobucks.holder.LogInDataHolder;
import com.ptobucks.parser.LogInParser;
import com.ptobucks.utils.AlertMessage;
import com.ptobucks.utils.Constant;
import com.ptobucks.utils.SharedPreferencesHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.ptobucks.R;

public class SplashActivity extends Activity {

	private Context context;
	protected static final int TIMER_RUNTIME = 500; // in ms --> 10s
	protected boolean mbActive;
	ProgressDialog dialog;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		context = this;
		
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		editor = sharedPreferences.edit();

		if (!SharedPreferencesHelper.isOnline(this)) {
			AlertMessage.showMessage(this, "Attention!",
					"Please Connect to Internet to Use this Application");

		} else {
			initUi();


		}

	}

	private void initUi() {

		final Thread timerThread = new Thread() {
			@Override
			public void run() {
				mbActive = true;
				try {
					int waited = 0;
					while (mbActive && (waited < TIMER_RUNTIME)) {
						sleep(5);
						if (mbActive) {
							waited += 5;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					onContinue();
				}
			}
		};
		timerThread.start();
	}

	protected void onResume() {
		
		super.onResume();
		overridePendingTransition(0, 0);
	}

	public void onContinue() {
		// perform any final actions here

		runOnUiThread(new Runnable() {

			public void run() {
				
				new SyncAsyncTask().execute("");
				
				/*long nowTime = System.currentTimeMillis();
				long differenceTime = nowTime - LogInDataHolder.getLogInData().getTokenTime();
				
				if(differenceTime < 60000){//1800000 21600000
					
				}else{
					Intent intent = new Intent(SplashActivity.this,
							LoginChooserActivity.class);
					startActivity(intent);
					finish();
				}*/

			}
		});

	}

	String results = "";
	String keword_response = "";




	public void onDestroy() {

	

		super.onDestroy();

	}
	
	class SyncAsyncTask extends AsyncTask<String, String, String> {

		private boolean b;

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				String token = sharedPreferences.getString("tb_token", "");
				response = httpclient.execute(new HttpGet(
						Constant.baseURL + "api/v1/sync?token=" + token));
				
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					responseString = out.toString();

					b =LogInParser.connect(context, responseString, true, token);

					out.close();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			dialog.dismiss();
			if(b){
				editor.putString("tb_token", LogInDataHolder.getLogInData().getToken());
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Intent intent = new Intent(SplashActivity.this,
						LoginChooserActivity.class);
				startActivity(intent);
				finish();
			}

		}

	}

}
