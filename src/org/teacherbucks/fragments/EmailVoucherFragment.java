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
import org.teacherbucks.MainActivity;
import org.teacherbucks.R;
import org.teacherbucks.adapter.PromotionSpinnerAdapter;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.holder.PromotionHolder;
import org.teacherbucks.holder.VoucherHolder;
import org.teacherbucks.model.Promotion;
import org.teacherbucks.parser.VoucherCreateParser;
import org.teacherbucks.utils.Constant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class EmailVoucherFragment extends Fragment {

	private PromotionSpinnerAdapter spinnerAdapter;
	private Spinner promotionSpinner;

	Promotion selectedPromotion;
	ProgressDialog dialog;

	static final int REQUEST_IMAGE_CAPTURE = 1;

	boolean voucherCreated;
	boolean voucherDelivered;

	private Button buttonDelv;
	private Button buttonSubmit;
	private Button buttonScanRcpt;

	EditText saleAmount;
	EditText customerEmail;

	private AlertDialog delvSucAlert;
	private AlertDialog delvFailAlert;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_email_voucher, container, false);

		buttonSubmit = (Button) view.findViewById(R.id.email_delv_submit);
		buttonScanRcpt = (Button) view.findViewById(R.id.email_delv_scn_rcpt);

		buttonDelv = (Button) view.findViewById(R.id.email_delv_delivery);

		saleAmount = (EditText) view.findViewById(R.id.email_sale_amount);
		customerEmail = (EditText) view.findViewById(R.id.email_customers_email);

		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);

		buttonSubmit.setClickable(true);
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new CreateVoucherAsyncTask().execute("text");
			}
		});

		/*
		 * buttonSubmit.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * Intent takePictureIntent = new
		 * Intent(MediaStore.ACTION_IMAGE_CAPTURE); if
		 * (takePictureIntent.resolveActivity(getActivity().getPackageManager())
		 * != null) { startActivityForResult(takePictureIntent,
		 * REQUEST_IMAGE_CAPTURE); }
		 * 
		 * } });
		 */

		final AlertDialog.Builder builderDelvSuc = new AlertDialog.Builder(getActivity());
		builderDelvSuc.setCancelable(false);

		View sucessView = inflater.inflate(R.layout.alert_success, null);
		builderDelvSuc.setView(sucessView);

		final AlertDialog.Builder builderDelvFail = new AlertDialog.Builder(getActivity());
		builderDelvFail.setCancelable(false);

		View failView = inflater.inflate(R.layout.alert_failed, null);
		builderDelvSuc.setView(failView);

		builderDelvSuc.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				delvSucAlert.dismiss();
				((MainActivity) getActivity())
						.setActionBarTitle(LogInDataHolder.getLogInData().getCompany().getTitle());
				((MainActivity) getActivity()).setBackKeyFlag(false);

				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
			}
		});

		builderDelvFail.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				delvFailAlert.dismiss();
				((MainActivity) getActivity())
						.setActionBarTitle(LogInDataHolder.getLogInData().getCompany().getTitle());
				((MainActivity) getActivity()).setBackKeyFlag(false);

				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
			}
		});

		delvSucAlert = builderDelvSuc.create();
		delvFailAlert = builderDelvFail.create();

		buttonDelv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (customerEmail.getText().toString().equals("") || customerEmail.getText().toString().equals(null)) {
					Toast.makeText(getActivity(), "Enter Customer's Email Address.", Toast.LENGTH_SHORT).show();
				} else {
					new VoucherDeliveryAsyncTask().execute("text");
				}
			}
		});

		spinnerAdapter = new PromotionSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
				PromotionHolder.getAllPromotionList());
		promotionSpinner = (Spinner) view.findViewById(R.id.email_promotion_spinner);
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

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { super.onActivityResult(requestCode, resultCode, data);
	 * switch (requestCode) { case 100: if (resultCode == Activity.RESULT_OK) {
	 * Uri selectedImage = imageUri;
	 * getActivity().getContentResolver().notifyChange(selectedImage, null);
	 * ContentResolver cr = getActivity().getContentResolver(); Bitmap bitmap;
	 * try { bitmap = android.provider.MediaStore.Images.Media .getBitmap(cr,
	 * selectedImage);
	 * 
	 * } catch (Exception e) {
	 * 
	 * } } } }
	 */

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
				Toast.makeText(getActivity(), "Voucher Created, Press Delivery.", Toast.LENGTH_SHORT).show();
				buttonSubmit.setVisibility(View.GONE);
				buttonScanRcpt.setVisibility(View.GONE);
				buttonDelv.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "Failed to Create Voucher !", Toast.LENGTH_LONG).show();
			}
		}
	}

	class VoucherDeliveryAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constant.baseURL + "/api/v1/vouchers/delivery/"
						+ VoucherHolder.getVoucher().getId() + "?token=" + LogInDataHolder.getLogInData().getToken());

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

				nameValuePairs.add(new BasicNameValuePair("type", "email"));
				nameValuePairs.add(new BasicNameValuePair("email", customerEmail.getText().toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				String json_string = EntityUtils.toString(response.getEntity());

				voucherDelivered = VoucherCreateParser.connect(getActivity(), json_string);

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
			if (voucherDelivered) {
				delvSucAlert.show();
			} else {
				delvFailAlert.show();
			}
		}
	}

}
