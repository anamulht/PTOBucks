package org.teacherbucks.fragments;

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
import org.teacherbucks.R;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.parser.EmployeeBlockUnblockParser;
import org.teacherbucks.utils.Constant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmpChangePassword extends Fragment{
	
	ProgressDialog dialog;
	boolean passwordChanged;
	
	EditText oldPass;
	EditText newPass;
	EditText passConfm;
	Button buttonSubmit;
	
	private AlertDialog sucAlert;
	private AlertDialog failAlert;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_emp_change_password, container, false);
		
		buttonSubmit = (Button) view.findViewById(R.id.emp_cp_submit);
		
		oldPass = (EditText) view.findViewById(R.id.emp_cp_old_pass);
		newPass = (EditText) view.findViewById(R.id.emp_cp_new_pass);
		passConfm = (EditText) view.findViewById(R.id.emp_cp_confirm_new_pass);
		
		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		
		final AlertDialog.Builder builderDelvSuc = new AlertDialog.Builder(getActivity());
		final AlertDialog.Builder builderDelvFail = new AlertDialog.Builder(getActivity());
		
		builderDelvSuc.setCancelable(true);
		builderDelvFail.setCancelable(true);
		
		View suc = inflater.inflate(R.layout.alert_success, null);
		((TextView) suc.findViewById(R.id.alert_suc_msg)).setText("Password Changed");
		
		View fail = inflater.inflate(R.layout.alert_failed, null);
		((TextView) fail.findViewById(R.id.alert_failed_msg)).setText("Failed");
		
		builderDelvSuc.setView(suc);
		builderDelvFail.setView(fail);
		
		sucAlert = builderDelvSuc.create();
		failAlert = builderDelvFail.create();
		
		
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!newPass.getText().toString().equals(passConfm.getText().toString())){
					Toast.makeText(getActivity(), "Password doesn't match", Toast.LENGTH_SHORT).show();
				}else{
					new PasswordChangeAsyncTask().execute("");
				}
				
			}
		});
		
		return view;
	}
	
	class PasswordChangeAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constant.baseURL + "api/v1/employees/password-change" + "?token=" + LogInDataHolder.getLogInData().getToken());
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				
				
				nameValuePairs.add(new BasicNameValuePair("old_password", oldPass.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", newPass.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password_confirmation", passConfm.getText().toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				passwordChanged = EmployeeBlockUnblockParser.connect(getActivity(), json_string);

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
			if (passwordChanged) {
				sucAlert.show();
			} else {
				failAlert.show();
			}
		}
	}

}
