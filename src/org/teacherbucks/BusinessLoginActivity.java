package org.teacherbucks;

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
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.parser.LogInParser;
import org.teacherbucks.utils.Constant;

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class BusinessLoginActivity extends Activity {

	ProgressDialog dialog;
	EditText vendorNo;
	EditText password;
	boolean login;
	TextView loginFailedMsg;
	
	SharedPreferences sharedPreferences;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_login);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActionBar().hide();
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();
		
		String vendorEmail = sharedPreferences.getString("tb_login_biz", "");

		vendorNo = (EditText) findViewById(R.id.biz_login_vendor_num);
		vendorNo.setText(vendorEmail);
		password = (EditText) findViewById(R.id.biz_login_password);
		loginFailedMsg = (TextView) findViewById(R.id.biz_login_failed_msg);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
			

	}

	public void goToBusinessLoginAction(View view) {
		/*
		 * Intent intent = new Intent(BusinessLoginActivity.this,
		 * MainActivity.class); intent.putExtra("userG", 0);
		 * startActivity(intent);
		 */
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
				HttpPost httppost = new HttpPost(Constant.baseURL + "api/v1/auth/login");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				// emp3@admin.com password
				nameValuePairs.add(new BasicNameValuePair("email", vendorNo.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				login = LogInParser.connect(BusinessLoginActivity.this, json_string);

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
			if (login) {
				Intent intent = new Intent(BusinessLoginActivity.this, MainActivity.class);
				//if (LogInDataHolder.getLogInData().getUser().getType().equalsIgnoreCase("employee")) {
					//intent.putExtra("userG", 1);
				//} else {
					intent.putExtra("userG", 0);
					editor.putString("tb_login_biz", vendorNo.getText().toString());
					editor.commit();
				//}
				startActivity(intent);
			} else {
				loginFailedMsg.setVisibility(View.VISIBLE);
			}
		}
	}
}
