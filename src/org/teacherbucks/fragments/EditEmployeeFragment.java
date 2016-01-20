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
import org.teacherbucks.parser.AddEmployeeParser;
import org.teacherbucks.utils.Constant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditEmployeeFragment extends Fragment {
	
	private EditText firstName;
	private EditText lastName;
	private EditText cellPhone;
	private EditText email;
	private EditText password;
	private Button btnSubmit;
	
	ProgressDialog dialog;
	boolean isEmployeeAdded;
	
	private AlertDialog sucAlert;
	private AlertDialog failAlert;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_edit_employee, container, false);
		
		firstName = (EditText) view.findViewById(R.id.edit_emp_first_name);
		lastName = (EditText) view.findViewById(R.id.edit_emp_last_name);
		cellPhone = (EditText) view.findViewById(R.id.edit_emp_cell_phn);
		email = (EditText) view.findViewById(R.id.edit_emp_email);
		password = (EditText) view.findViewById(R.id.edit_emp_pass);
		btnSubmit = (Button) view.findViewById(R.id.edit_emp_submit);
		
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
		((TextView) suc.findViewById(R.id.alert_suc_msg)).setText("Saved");
		
		View fail = inflater.inflate(R.layout.alert_failed, null);
		((TextView) fail.findViewById(R.id.alert_failed_msg)).setText("Failed");
		
		builderDelvSuc.setView(suc);
		builderDelvFail.setView(fail);
		
		sucAlert = builderDelvSuc.create();
		failAlert = builderDelvFail.create();
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new EditEmployeeAsynTast().execute("param");
				
			}
		});
		
		return view;
	}
	
	
	
	class EditEmployeeAsynTast extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constant.baseURL + "/api/v1/employees/update/{id}" + LogInDataHolder.getLogInData().getToken());
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
				
				nameValuePairs.add(new BasicNameValuePair("first_name", firstName.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("last_name", lastName.getText().toString()));				
				nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("phone", cellPhone.getText().toString()));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				isEmployeeAdded = AddEmployeeParser.connect(getActivity(), json_string);

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
			if (isEmployeeAdded) {
				//Toast.makeText(getActivity(), "added", Toast.LENGTH_LONG).show();
				sucAlert.show();
			} else {
				//Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
				failAlert.show();
			}
			
		}
		
	}

}
