package org.teacherbucks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.teacherbucks.parser.LogInParser;

public class BusinessLoginActivity extends Activity {
	
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_login);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActionBar().hide();
		
		dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        
	}

	public void goToBusinessLoginAction(View view) {
		/*Intent intent = new Intent(BusinessLoginActivity.this, MainActivity.class);
		intent.putExtra("userG", 0);
		startActivity(intent);*/
		new loadAsyncTask().execute("text");
	}
	
	
	
	class loadAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://104.131.229.197/api/v1/auth/login");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("email", "emp3@admin.com"));
		        nameValuePairs.add(new BasicNameValuePair("password", "password"));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
		        String json_string = EntityUtils.toString(response.getEntity());
				
		        LogInParser.connect(BusinessLoginActivity.this, json_string);

			} catch (Exception e) {
			}
			return null;

		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			dialog.dismiss();
			Intent intent = new Intent(BusinessLoginActivity.this, MainActivity.class);
			intent.putExtra("userG", 0);
			startActivity(intent);

		}

	}
	
}
