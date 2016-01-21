package org.teacherbucks.fragments;

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
import org.teacherbucks.MainActivity;
import org.teacherbucks.R;
import org.teacherbucks.adapter.EmployeeListAdapter;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.model.Employee;
import org.teacherbucks.parser.AllEmployeeParser;
import org.teacherbucks.parser.EmployeeBlockUnblockParser;
import org.teacherbucks.utils.Constant;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ManageEmployeeFragment extends Fragment {

	private ListView employeeListView;
	ProgressDialog dialog;

	EmployeeListAdapter empAdapter;

	private AlertDialog optionsAlert;

	private Employee selectedEmp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_manage_employee, container, false);

		employeeListView = (ListView) view.findViewById(R.id.employee_list);

		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);

		LinearLayout addEmployeLayout = (LinearLayout) view.findViewById(R.id.add_employee_select);
		addEmployeLayout.setClickable(true);

		addEmployeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final FragmentManager fragmentManager = getFragmentManager();
				((MainActivity) getActivity()).setActionBarTitle("Add Employee");
				fragmentManager.beginTransaction().replace(R.id.frame_container, new AddEmployeeFragment()).commit();

			}
		});

		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);

		View v = inflater.inflate(R.layout.employee_list_on_click_popup, null);

		((LinearLayout) v.findViewById(R.id.emp_activate)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				optionsAlert.dismiss();
				if (selectedEmp.getStatus() == 1) {
					Toast.makeText(getActivity(), "Employee already Activated.", Toast.LENGTH_SHORT).show();
				} else {
					new EmployeeBlockUnblock().execute("u");
				}

			}
		});

		((LinearLayout) v.findViewById(R.id.emp_deactivate)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				optionsAlert.dismiss();
				if (selectedEmp.getStatus() == 0) {
					Toast.makeText(getActivity(), "Employee already Deactivated.", Toast.LENGTH_SHORT).show();
				} else {
					new EmployeeBlockUnblock().execute("b");
				}

			}
		});

		((LinearLayout) v.findViewById(R.id.emp_edit)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				optionsAlert.dismiss();
				final FragmentManager fragmentManager = getFragmentManager();
				((MainActivity) getActivity()).setActionBarTitle("Edit Employee");
				Fragment fragment = new EditEmployeeFragment();
				if (selectedEmp != null) {
					Bundle bundles = new Bundle();
					bundles.putString("emp", selectedEmp.getId());
					fragment.setArguments(bundles);
				}
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			}
		});

		((LinearLayout) v.findViewById(R.id.emp_delete)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//optionsAlert.dismiss();
				Toast.makeText(getActivity(), "This option is Disabled !", Toast.LENGTH_SHORT).show();
			}
		});

		builder.setView(v);

		optionsAlert = builder.create();

		employeeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				optionsAlert.show();
				selectedEmp = new Employee();
				selectedEmp = empAdapter.getItem(arg2);
				// Toast.makeText(getActivity(),
				// ((Employee)empAdapter.getItem(arg2)).getImage(),Toast.LENGTH_LONG).show();
				// optionsAlert.getWindow().setBackgroundDrawable(new
				// ColorDrawable(android.graphics.Color.TRANSPARENT));
			}
		});

		new GetEmployeeListAsyncTast().execute("param");
		return view;

	}

	class GetEmployeeListAsyncTast extends AsyncTask<String, String, String> {

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
				response = httpclient.execute(new HttpGet(
						Constant.baseURL + "api/v1/employees?token=" + LogInDataHolder.getLogInData().getToken()));
				System.out.println("url: " + Constant.baseURL + "api/v1/employees?token="
						+ LogInDataHolder.getLogInData().getToken());
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					responseString = out.toString();

					AllEmployeeParser.connect(getActivity(), responseString);

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
			empAdapter = new EmployeeListAdapter(getActivity());
			employeeListView.setAdapter(empAdapter);
			empAdapter.notifyDataSetChanged();

		}

	}

	class EmployeeBlockUnblock extends AsyncTask<String, String, String> {
		boolean bol;

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			System.out.println("block unblock" + arg0[0]);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				HttpGet httpGet;
				if (arg0[0].equals("b")) {
					response = httpclient.execute(new HttpGet(Constant.baseURL + "api/v1/employees/block/"
							+ selectedEmp.getId() + "?token=" + LogInDataHolder.getLogInData().getToken()));
				} else {
					response = httpclient.execute(new HttpGet(Constant.baseURL + "api/v1/employees/unblock/"
							+ selectedEmp.getId() + "?token=" + LogInDataHolder.getLogInData().getToken()));
				}
				
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					responseString = out.toString();

					bol = EmployeeBlockUnblockParser.connect(getActivity(), responseString);

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
			selectedEmp = null;
			if (bol) {
				new GetEmployeeListAsyncTast().execute("params");
			} else {
				Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_LONG);
			}

		}

	}
}
