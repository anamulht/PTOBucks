package org.teacherbucks.fragments;

import org.teacherbucks.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QRVoucherFragment extends Fragment {
	
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
		
		return view;
	}
	
	/*
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		getActivity().onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			final FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, new HomeFragment()).commit();

		default:
			return false;
		}
	}*/

}
