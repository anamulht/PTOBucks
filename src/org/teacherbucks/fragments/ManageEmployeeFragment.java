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
import org.teacherbucks.R;
import org.teacherbucks.adapter.EmployeeListAdapter;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.parser.AllEmployeeParser;
import org.teacherbucks.utils.Constant;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class ManageEmployeeFragment extends Fragment {
	
	private ListView employeeListView;
	ProgressDialog dialog;
	
	EmployeeListAdapter empAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_manage_employee, container, false);
		
		employeeListView = (ListView) view.findViewById(R.id.employee_list);
		
		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		
		new getEmployeeListAsyncTast().execute("param");
		return view;

	}
	
	
	class getEmployeeListAsyncTast extends AsyncTask<String, String, String>{
		
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
	            response = httpclient.execute(new HttpGet(Constant.baseURL + "api/v1/employees?token=" + LogInDataHolder.getLogInData().getToken()));
	            System.out.println("url: " + Constant.baseURL + "api/v1/employees?token=" + LogInDataHolder.getLogInData().getToken());
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                responseString = out.toString();
	                
	                AllEmployeeParser.connect(getActivity(), responseString);
	                
	                out.close();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	            //TODO Handle problems..
	        } catch (IOException e) {
	            //TODO Handle problems..
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
}
