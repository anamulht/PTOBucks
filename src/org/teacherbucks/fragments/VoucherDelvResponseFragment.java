package org.teacherbucks.fragments;

import org.teacherbucks.MainActivity;
import org.teacherbucks.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VoucherDelvResponseFragment extends Fragment {

	Context parentContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_voucher_delv_response, container, false);

		parentContext = container.getContext();
		boolean response = false;
		
		Bundle bundle = this.getArguments();
		response = bundle.getBoolean("response");

		Button buttonOK = (Button) view.findViewById(R.id.image_voucher_delv_go_back);
		Button buttonTryAgain = (Button) view.findViewById(R.id.button_voucher_delv_response);
		
		TextView textViewResponseMessage = (TextView) view.findViewById(R.id.text_voucher_delv_status);
		ImageView imageResonse = (ImageView) view.findViewById(R.id.image_voucher_delv_response);
				
		buttonOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((MainActivity) parentContext).setActionBarTitle("The Super Store");
				((MainActivity) parentContext).setBackKeyFlag(false);

				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
			}
		});

		if (response) {
			imageResonse.setImageResource(R.drawable.icon_tick_128);
			textViewResponseMessage.setText("Voucher Delivered Successfully.");
			textViewResponseMessage.setTextColor(Color.parseColor("#40B240"));
			buttonTryAgain.setVisibility(View.GONE);
			
		} else {
			imageResonse.setImageResource(R.drawable.icon_cross_128);
			textViewResponseMessage.setText("Voucher Delivery Failed !");
			textViewResponseMessage.setTextColor(Color.parseColor("#FF0000"));
			buttonTryAgain.setVisibility(View.VISIBLE);
			buttonTryAgain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}
			});
		}

		return view;
	}
}
