package org.teacherbucks.fragments;

import org.teacherbucks.MainActivity;
import org.teacherbucks.R;
import org.teacherbucks.R.id;
import org.teacherbucks.R.layout;
import org.teacherbucks.holder.LogInDataHolder;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, container, false);

		Email = (TextView) view.findViewById(R.id.text_view_email_delivery);
		SMS = (TextView) view.findViewById(R.id.text_view_sms_delivery);
		UserName = (TextView) view.findViewById(R.id.text_view_username_delivery);
		QRCode = (TextView) view.findViewById(R.id.text_view_qr_delivery);
		if (((MainActivity) getActivity()).getUserGroup() == 0) {
			((TextView) view.findViewById(R.id.home_header)).setText(LogInDataHolder.getLogInData().getCompany().getTitle()
					+ "\n" + LogInDataHolder.getLogInData().getCompany().getAddress());
		} else {
			((TextView) view.findViewById(R.id.home_header)).setText(LogInDataHolder.getLogInData().getUser().getFirst_name() + " "
					+ LogInDataHolder.getLogInData().getUser().getLast_name()
					+ "\n" + LogInDataHolder.getLogInData().getUser().getPhone());
		} 
		// Toast.makeText(container.getContext(), "YES",
		// Toast.LENGTH_LONG).show();

		parentContext = container.getContext();

		Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final FragmentManager fragmentManager = getFragmentManager();
				setBackKeyFlagTrue();
				((MainActivity) parentContext).setActionBarTitle("Email");
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
				fragmentManager.beginTransaction().replace(R.id.frame_container, new UsernameVoucherFragment())
						.commit();
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

		return view;

	}

	private void setBackKeyFlagTrue() {
		((MainActivity) parentContext).setBackKeyFlag(true);
	}

}
