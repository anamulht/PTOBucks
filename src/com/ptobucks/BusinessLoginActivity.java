package com.ptobucks;

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
import com.ptobucks.holder.LogInDataHolder;
import com.ptobucks.parser.LogInParser;
import com.ptobucks.utils.AlertMessage;
import com.ptobucks.utils.Constant;
import com.ptobucks.utils.SharedPreferencesHelper;
import com.ptobucks.utils.Utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ptobucks.R;

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
		
		setupUI(findViewById(R.id.biz_login_layout));//biz_login_layout

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();

		String vendorEmail = sharedPreferences.getString("tb_login_biz", "");
		String vendorPassword = sharedPreferences.getString("tb_login_biz_pass", "");

		vendorNo = (EditText) findViewById(R.id.biz_login_vendor_num);
		vendorNo.setText(vendorEmail);
		password = (EditText) findViewById(R.id.biz_login_password);
		password.setText(vendorPassword);

		loginFailedMsg = (TextView) findViewById(R.id.biz_login_failed_msg);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);

		((TextView) findViewById(R.id.text_view_forgot_pass_biz)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				// Uri.parse("http://104.131.229.197/password/email"));
				// startActivity(browserIntent);

				Intent intent = new Intent(BusinessLoginActivity.this, ResetPasswordActivity.class);
				startActivity(intent);

			}
		});

	}

	public void goToBusinessLoginAction(View view) {
		/*
		 * Intent intent = new Intent(BusinessLoginActivity.this,
		 * MainActivity.class); intent.putExtra("userG", 0);
		 * startActivity(intent);
		 */

		if (!Utils.isValidEmail(vendorNo.getText().toString())) {

			Toast.makeText(this, "Enter a Valid Email address", Toast.LENGTH_LONG).show();

		} else {

			if (TextUtils.isEmpty(password.getText().toString())) {
				Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
			} else {
				if (!SharedPreferencesHelper.isOnline(this)) {
					AlertMessage.showMessage(this, "Network Error !", "Please connect to Internet.");

				} else {
					new loadAsyncTask().execute("text");
				}
			}

		}

	}

	class loadAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			loginFailedMsg.setVisibility(View.GONE);
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

				login = LogInParser.connect(BusinessLoginActivity.this, json_string, false, "");

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
				if (LogInDataHolder.getLogInData().getUser().getType().equalsIgnoreCase("employee")) {
					Toast.makeText(BusinessLoginActivity.this, "Try in Employee Login", Toast.LENGTH_LONG).show();
				} else {
					//intent.putExtra("userG", 0);
					editor.putString("tb_login_biz", vendorNo.getText().toString());
					editor.putString("tb_login_biz_pass", password.getText().toString());
					editor.putString("tb_token", LogInDataHolder.getLogInData().getToken());
					editor.commit();
					startActivity(intent);
				}

			} else {
				loginFailedMsg.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(BusinessLoginActivity.this);
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	private void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}
