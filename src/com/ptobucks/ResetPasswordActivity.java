package com.ptobucks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.ptobucks.parser.PasswordResetParser;
import com.ptobucks.utils.Constant;
import com.ptobucks.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ptobucks.R;

public class ResetPasswordActivity extends Activity {

	EditText resetEmail;
	ProgressDialog dialog;

	private AlertDialog sucAlert;
	private AlertDialog failAlert;

	boolean reset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		getActionBar().hide();

		setupUI(findViewById(R.id.reset_pass_layout));

		resetEmail = (EditText) findViewById(R.id.reset_pass_email);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);

		final AlertDialog.Builder builderDelvSuc = new AlertDialog.Builder(this);
		final AlertDialog.Builder builderDelvFail = new AlertDialog.Builder(this);

		builderDelvSuc.setCancelable(true);
		builderDelvFail.setCancelable(true);

		View suc = getLayoutInflater().inflate(R.layout.alert_success, null);
		((TextView) suc.findViewById(R.id.alert_suc_msg)).setText("An Email is sent to your email address");

		View fail = getLayoutInflater().inflate(R.layout.alert_failed, null);
		((TextView) fail.findViewById(R.id.alert_failed_msg)).setText("Failed ! Try again");

		builderDelvSuc.setView(suc);
		builderDelvFail.setView(fail);

		sucAlert = builderDelvSuc.create();
		failAlert = builderDelvFail.create();

	}

	public void goToResetPassword(View view) {
		if (Utils.isValidEmail(resetEmail.getText().toString()))
			new ResetPasswordAsyncTask().execute("");
		else
			Toast.makeText(this, "Enter an Valid Email Address", Toast.LENGTH_SHORT).show();
	}

	class ResetPasswordAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constant.baseURL + "api/v1/auth/reset-password");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("email", resetEmail.getText().toString()));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				reset = PasswordResetParser.connect(ResetPasswordActivity.this, json_string);

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
			if (reset) {
				sucAlert.show();

			} else {
				failAlert.show();
			}
		}
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(ResetPasswordActivity.this);
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
