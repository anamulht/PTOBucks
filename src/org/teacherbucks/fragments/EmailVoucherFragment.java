package org.teacherbucks.fragments;

import org.teacherbucks.MainActivity;
import org.teacherbucks.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class EmailVoucherFragment extends Fragment{
	
	public EmailVoucherFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_email_voucher, container, false);
		
		Button buttonSubmit = (Button) view.findViewById(R.id.email_delv_submit);
		Button buttonScanRcpt = (Button) view.findViewById(R.id.email_delv_scn_rcpt);
		
		buttonSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final FragmentManager fragmentManager = getFragmentManager();
				Fragment fragment = new VoucherDelvResponseFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("response", true);
				fragment.setArguments(bundle);
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				
			}
		});

		/*if(getActivity() instanceof MainActivity){
			Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getContext(), "NO", Toast.LENGTH_LONG).show();
		}*/
		
		//((MainActivity) getContext()).setBackKeyFlag(true);

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
