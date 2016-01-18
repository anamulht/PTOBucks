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
import org.teacherbucks.BusinessLoginActivity.loadAsyncTask;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.parser.LogInParser;

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
import android.widget.EditText;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class EmployeeLoginActivity extends Activity {

	ProgressDialog dialog;
	EditText empUsername;
	EditText password;
	boolean login;
	TextView loginFailedMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_login);
		getActionBar().hide();

		empUsername = (EditText) findViewById(R.id.emp_login_username);
		password = (EditText) findViewById(R.id.emp_login_password);
		loginFailedMsg = (TextView) findViewById(R.id.emp_login_failed_msg);
		
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
	}

	public void goToEmployeeLoginAction(View view) {
		/*
		 * Intent intent = new Intent(EmployeeLoginActivity.this,
		 * MainActivity.class); intent.putExtra("userG", 1);
		 * startActivity(intent);
		 */
		new loadAsyncTask().execute("text");
		// Toast.makeText(this, "Employee", Toast.LENGTH_LONG).show();
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
				// emp3@admin.com password
				nameValuePairs.add(new BasicNameValuePair("email", empUsername.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				login = LogInParser.connect(EmployeeLoginActivity.this, json_string);

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
				Intent intent = new Intent(EmployeeLoginActivity.this, MainActivity.class);
				if (LogInDataHolder.getLogInData().getUser().getType().equalsIgnoreCase("employee")) {
					intent.putExtra("userG", 1);
				} else {
					intent.putExtra("userG", 0);
				}
				startActivity(intent);
			} else {
				loginFailedMsg.setVisibility(View.VISIBLE);
			}
		}
	}
}
