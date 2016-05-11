package com.ptobucks.fragments;

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
import com.ptobucks.MainActivity;
import com.ptobucks.R;
import com.ptobucks.holder.LogInDataHolder;
import com.ptobucks.holder.SettingsDataHolder;
import com.ptobucks.parser.AppSettingsParser;
import com.ptobucks.utils.Constant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DataPreferencesFragment extends Fragment {

	ProgressDialog dialog;

	private boolean isReceiptNo;
	private boolean isReceiptImage;
	private Context parentContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_manage_data_pref, container, false);

		parentContext = container.getContext();

		LinearLayout receiptNoSelection = (LinearLayout) view.findViewById(R.id.data_pref_receipt_no_selection_area);
		LinearLayout receiptImageSelection = (LinearLayout) view.findViewById(R.id.data_pref_camera_selection_area);

		final View indicatorReceiptNo = (View) view.findViewById(R.id.indicator_data_pref_receipt_no);
		final View indicatorReceiptImage = (View) view.findViewById(R.id.indicator_data_pref_camera);

		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);

		isReceiptNo = SettingsDataHolder.getSettings().isReceiptNo();
		isReceiptImage = SettingsDataHolder.getSettings().isReceiptImage();

		if (isReceiptNo) {
			indicatorReceiptNo.setBackgroundResource(R.color.green);
		} else {
			indicatorReceiptNo.setBackgroundResource(R.color.red);
		}

		if (isReceiptImage) {
			indicatorReceiptImage.setBackgroundResource(R.color.green);
		} else {
			indicatorReceiptImage.setBackgroundResource(R.color.red);
		}

		receiptNoSelection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isReceiptNo) {
					isReceiptNo = false;
					indicatorReceiptNo.setBackgroundResource(R.color.red);
				} else {
					isReceiptNo = true;
					indicatorReceiptNo.setBackgroundResource(R.color.green);
				}
			}
		});

		receiptImageSelection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isReceiptImage) {
					isReceiptImage = false;
					indicatorReceiptImage.setBackgroundResource(R.color.red);
				} else {
					isReceiptImage = true;
					indicatorReceiptImage.setBackgroundResource(R.color.green);
				}
			}
		});

		((Button) view.findViewById(R.id.button_data_pref_submit)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new DataPrefUpdateAsyncTask().execute("");
			}
		});

		return view;

	}

	class DataPrefUpdateAsyncTask extends AsyncTask<String, String, String> {
		private boolean isSet;

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constant.baseURL + "api/v1/settings/update" + "?token="
						+ LogInDataHolder.getLogInData().getToken());

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

				if (isReceiptNo)
					nameValuePairs.add(new BasicNameValuePair("invoice_no", "1"));
				else
					nameValuePairs.add(new BasicNameValuePair("invoice_no", "0"));
				
				if (isReceiptImage)
					nameValuePairs.add(new BasicNameValuePair("invoice_image", "1"));
				else
					nameValuePairs.add(new BasicNameValuePair("invoice_image", "0"));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				isSet = AppSettingsParser.connect(getActivity(), json_string);

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
			if (isSet) {
				((MainActivity) parentContext).setActionBarTitle(LogInDataHolder.getLogInData().getCompany().getTitle());
				((MainActivity) parentContext).setBackKeyFlag(false);

				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
				Toast.makeText(getActivity(), "Data Preferenecs Saved", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Failed to Save !", Toast.LENGTH_LONG).show();
			}
		}
	}

}
