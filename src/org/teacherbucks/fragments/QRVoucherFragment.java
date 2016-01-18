package org.teacherbucks.fragments;

import org.teacherbucks.R;
import org.teacherbucks.adapter.PromotionSpinnerAdapter;
import org.teacherbucks.holder.PromotionHolder;
import org.teacherbucks.model.Promotion;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

public class QRVoucherFragment extends Fragment {
	
	private PromotionSpinnerAdapter spinnerAdapter;
	private Spinner promotionSpinner;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_qr_voucher, container, false);

		Button buttonSubmit = (Button) view.findViewById(R.id.qr_delv_submit);
		Button buttonScanRcpt = (Button) view.findViewById(R.id.qr_delv_scn_rcpt);
		
		buttonSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final FragmentManager fragmentManager = getFragmentManager();
				Fragment fragment = new VoucherDelvResponseFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("response", false);
				fragment.setArguments(bundle);
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				
			}
		});
		
		spinnerAdapter = new PromotionSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
				PromotionHolder.getAllPromotionList());
		promotionSpinner = (Spinner) view.findViewById(R.id.qr_promotion_spinner);
		promotionSpinner.setAdapter(spinnerAdapter);

		promotionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				Promotion promotionObj = spinnerAdapter.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});
		
		return view;
	}

}
