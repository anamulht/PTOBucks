package org.teacherbucks.fragments;

import org.teacherbucks.MainActivity;
import org.teacherbucks.R;
import org.teacherbucks.R.id;
import org.teacherbucks.R.layout;

//import android.support.v7.app.ActionBar;
import android.app.ActionBar;
import android.support.v7.appcompat.*;
import android.util.Log;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	TextView Email;
	TextView SMS;
	TextView UserName;
	TextView QRCode;
	
	Context parentContext;
	
	/*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
	
	@Override
	public void onStart()
	{
	    super.onStart();
	    
	    if(getActivity() instanceof MainActivity){
			Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getContext(), "NO", Toast.LENGTH_LONG).show();
		}

	}*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		
		Email = (TextView) view.findViewById(R.id.text_view_email_delivery);
		SMS = (TextView) view.findViewById(R.id.text_view_sms_delivery);
		UserName = (TextView) view.findViewById(R.id.text_view_username_delivery);
		QRCode = (TextView) view.findViewById(R.id.text_view_qr_delivery);		
		
		//Toast.makeText(container.getContext(), "YES", Toast.LENGTH_LONG).show();
		
		parentContext = container.getContext();
		
		Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final FragmentManager fragmentManager = getFragmentManager();
				setBackKeyFlagTrue();
				((MainActivity) parentContext).setActionBarTitle("Email");
				//((MainActivity) getContext()).setBackKeyFlag(true);
				fragmentManager.beginTransaction().replace(R.id.frame_container, new EmailVoucherFragment()).commit();
			}
		});
		
		SMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final FragmentManager fragmentManager = getFragmentManager();
				setBackKeyFlagTrue();
				((MainActivity) parentContext).setActionBarTitle("SMS");
				fragmentManager.beginTransaction().replace(R.id.frame_container, new SMSVoucherFragment()).commit();
			}
		});
		
		UserName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final FragmentManager fragmentManager = getFragmentManager();
				setBackKeyFlagTrue();
				((MainActivity) parentContext).setActionBarTitle("Username");
				fragmentManager.beginTransaction().replace(R.id.frame_container, new UsernameVoucherFragment()).commit();
			}
		});
		
		QRCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final FragmentManager fragmentManager = getFragmentManager();
				setBackKeyFlagTrue();
				((MainActivity) parentContext).setActionBarTitle("QR Code");
				fragmentManager.beginTransaction().replace(R.id.frame_container, new QRVoucherFragment()).commit();
			}
		});
		
		//setListenersToVoucherDeliveryMethods();
		
		return view;
		
	}
	
	private void setBackKeyFlagTrue(){
		((MainActivity) parentContext).setBackKeyFlag(true);
	}
	
	/*private void setListenersToVoucherDeliveryMethods(){
		//voucher via email
		Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToVoucherDelivery(0);
			}
		});
		
		//voucher via sms
		SMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToVoucherDelivery(1);
			}
		});
		
		//voucher via username
		UserName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToVoucherDelivery(2);
			}
		});
		
		//voucher via qr code
		QRCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToVoucherDelivery(3);
			}
		});
	}*/
	
	/*public void goToVoucherDelivery(int position){
		Fragment fragment = null;
		
		switch (position) {
		case 0:
			fragment = new EmailVoucherFragment();
			
			break;
		case 1:
			fragment = new SMSVoucherFragment();
			break;
		case 2:
			fragment = new UsernameVoucherFragment();
			break;
		case 3:
			fragment = new QRVoucherFragment();

		default:
			break;
		}
		
		if(fragment != null){
			Toast.makeText(getContext(), "7u6yuir5fg", Toast.LENGTH_LONG).show();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.add(R.id.fragment_container, fragment);
			fragmentTransaction.show(fragment);
			fragmentTransaction.commit();
			/*getFragmentManager().beginTransaction()
	          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
	          .show(fragment)
	          .commit();
			//final FragmentManager fragmentManager = getFragmentManager();
			//fragmentManager.beginTransaction().replace(R.id.frame_container, new EmailVoucherFragment()).commit();
			FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.frame_container,new EmailVoucherFragment() );
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
		} else {
			Log.e("HomeActivity", "Error in creating fragment");
		}
		
	}*/
	
}
