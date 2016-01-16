package org.teacherbucks.fragments;

import org.teacherbucks.MainActivity;
import org.teacherbucks.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class DataPreferencesFragment extends Fragment {

	private boolean isReceiptNo = true;
	private boolean isReceiptImage = false;
	private Context parentContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_manage_data_pref, container, false);

		parentContext = container.getContext();
		
		LinearLayout receiptNoSelection = (LinearLayout) view.findViewById(R.id.data_pref_receipt_no_selection_area);
		LinearLayout receiptImageSelection = (LinearLayout) view.findViewById(R.id.data_pref_camera_selection_area);

		final View indicatorReceiptNo = (View) view.findViewById(R.id.indicator_data_pref_receipt_no);
		final View indicatorReceiptImage = (View) view.findViewById(R.id.indicator_data_pref_camera);
		
		if(isReceiptNo){
			indicatorReceiptNo.setBackgroundResource(R.color.green);
		}else{
			indicatorReceiptNo.setBackgroundResource(R.color.dark);
		}
		
		if(isReceiptImage){
			indicatorReceiptImage.setBackgroundResource(R.color.green);
		}else{
			indicatorReceiptImage.setBackgroundResource(R.color.dark);
		}
		
		receiptNoSelection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isReceiptNo){
					isReceiptNo = false;
					indicatorReceiptNo.setBackgroundResource(R.color.dark);
				}else{
					isReceiptNo = true;
					indicatorReceiptNo.setBackgroundResource(R.color.green);
				}
			}
		});

		receiptImageSelection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isReceiptImage){
					isReceiptImage = false;
					indicatorReceiptImage.setBackgroundResource(R.color.dark);
				}else{
					isReceiptImage = true;
					indicatorReceiptImage.setBackgroundResource(R.color.green);
				}
			}
		});

		((Button) view.findViewById(R.id.button_data_pref_submit)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((MainActivity) parentContext).setActionBarTitle("The Super Store");
				((MainActivity) parentContext).setBackKeyFlag(false);

				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
			}
		});

		return view;

	}

}
