package org.teacherbucks.fragments;

import org.teacherbucks.MainActivity;
import org.teacherbucks.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SalesDataFragment extends Fragment {

	private boolean isPercantage = true;
	private boolean isSetAmount = false;
	private Context parentContext;
	private AlertDialog percentageAlert;
	private AlertDialog setAmountAlert;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_manage_sales_data, container, false);

		parentContext = container.getContext();

		LinearLayout percantageSelection = (LinearLayout) view.findViewById(R.id.sales_data_percantage_selection_area);
		LinearLayout setAmountSelection = (LinearLayout) view.findViewById(R.id.sales_data_set_amount_selection_area);

		final View indicatorPercantage = (View) view.findViewById(R.id.indicator_sales_data_percantage);
		final View indicatorSetAmount = (View) view.findViewById(R.id.indicator_sales_data_set_amount);

		final TextView textPercantageAmount = (TextView) view.findViewById(R.id.textview_manage_sales_data_percentage_amount);
		final TextView textSetAmount = (TextView) view.findViewById(R.id.textview_manage_sales_data_set_amount);

		final AlertDialog.Builder builderPercantage = new AlertDialog.Builder(parentContext);
		builderPercantage.setTitle("Enter The Percantage Amount");
		builderPercantage.setCancelable(false);

		final EditText percantageInput = new EditText(parentContext);
		percantageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
		//percantageInput.setBackgroundColor(R.drawable.edittextstyle);
		builderPercantage.setView(percantageInput);

		builderPercantage.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				textPercantageAmount.setText(percantageInput.getText().toString() + "%");
			}
		});

		builderPercantage.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				percentageAlert.dismiss();
			}
		});

		final AlertDialog.Builder builderSetAmount = new AlertDialog.Builder(parentContext);
		builderSetAmount.setTitle("Enter The Set Amount");

		final EditText setAmountInput = new EditText(parentContext);
		setAmountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
		builderSetAmount.setView(setAmountInput);
		builderSetAmount.setCancelable(false);

		builderSetAmount.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				textSetAmount.setText(setAmountInput.getText().toString() + "$");
			}
		});

		builderSetAmount.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				setAmountAlert.dismiss();
			}
		});
		
		percentageAlert = builderPercantage.create();
		setAmountAlert = builderSetAmount.create();

		if (isPercantage) {
			indicatorPercantage.setBackgroundResource(R.color.green);
		} else {
			indicatorPercantage.setBackgroundResource(R.color.dark);
		}

		if (isSetAmount) {
			indicatorSetAmount.setBackgroundResource(R.color.green);
		} else {
			indicatorSetAmount.setBackgroundResource(R.color.dark);
		}

		percantageSelection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isPercantage) {
					isPercantage = true;
					isSetAmount = false;
					indicatorPercantage.setBackgroundResource(R.color.green);
					indicatorSetAmount.setBackgroundResource(R.color.dark);
				}
			}
		});

		setAmountSelection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isSetAmount) {
					isSetAmount = true;
					isPercantage = false;
					indicatorSetAmount.setBackgroundResource(R.color.green);
					indicatorPercantage.setBackgroundResource(R.color.dark);
				}
			}
		});

		textPercantageAmount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				percentageAlert.show();
			}
		});

		textSetAmount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setAmountAlert.show();
			}
		});

		((Button) view.findViewById(R.id.button_sales_data_submit)).setOnClickListener(new OnClickListener() {

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
