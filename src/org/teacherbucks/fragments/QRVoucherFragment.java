package org.teacherbucks.fragments;

import java.io.File;
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
import org.teacherbucks.MainActivity;
import org.teacherbucks.R;
import org.teacherbucks.adapter.PromotionSpinnerAdapter;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.holder.PromotionHolder;
import org.teacherbucks.model.Promotion;
import org.teacherbucks.parser.VoucherCreateParser;
import org.teacherbucks.utils.Constant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class QRVoucherFragment extends Fragment {
	
	private PromotionSpinnerAdapter spinnerAdapter;
	private Spinner promotionSpinner;
	
	Promotion selectedPromotion;
	ProgressDialog dialog;

	static final int REQUEST_IMAGE_CAPTURE = 1;

	boolean voucherCreated;
	
	private Button buttonSubmit;
	private Button buttonScanRcpt;

	
	EditText saleAmount;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_qr_voucher, container, false);

		buttonSubmit = (Button) view.findViewById(R.id.qr_delv_submit);
		buttonScanRcpt = (Button) view.findViewById(R.id.qr_delv_scn_rcpt);
		
		saleAmount = (EditText) view.findViewById(R.id.qr_sale_amount);
		
		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		
		buttonSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new CreateVoucherAsyncTask().execute("text");
				
			}
		});
		
		buttonScanRcpt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				takePhoto();
				
			}
		});
		
		spinnerAdapter = new PromotionSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
				PromotionHolder.getAllPromotionList());
		promotionSpinner = (Spinner) view.findViewById(R.id.qr_promotion_spinner);
		promotionSpinner.setAdapter(spinnerAdapter);

		promotionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				selectedPromotion = spinnerAdapter.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});
		
		return view;
	}
	
	class CreateVoucherAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constant.baseURL + "api/v1/vouchers/" + selectedPromotion.getId()
						+ "?token=" + LogInDataHolder.getLogInData().getToken());
				System.out.println("url: " + Constant.baseURL + "api/v1/vouchers/" + selectedPromotion.getId()
						+ "?token=" + LogInDataHolder.getLogInData().getToken());
				// http://104.131.229.197/api/v1/vouchers/1?token=eyJ0eXAiOiJKV1QiL
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				double sale_amount = Double.parseDouble(saleAmount.getText().toString());
				double voucher_value;
				if (selectedPromotion.getType_of_offer().equalsIgnoreCase("cash")) {
					voucher_value = Double.parseDouble(selectedPromotion.getPer_offer_amount());
				} else {
					voucher_value = ((Double.parseDouble(selectedPromotion.getPer_offer_amount()) * sale_amount) / 100);
				}
				nameValuePairs.add(new BasicNameValuePair("sale_total", sale_amount + ""));
				nameValuePairs.add(new BasicNameValuePair("voucher_value", voucher_value + ""));

				System.out.println(sale_amount + "----" + voucher_value);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				voucherCreated = VoucherCreateParser.connect(getActivity(), json_string);

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
			if (voucherCreated) {
				final FragmentManager fragmentManager = getFragmentManager();
				Fragment fragment = new QRCodeFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("response", false);
				fragment.setArguments(bundle);
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			} else {
				Toast.makeText(getActivity(), "Failed to Create Voucher !", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        ((MainActivity) getActivity()).setImageUri(Uri.fromFile(photo));
        getActivity().startActivityForResult(intent, 100);
    }

}
