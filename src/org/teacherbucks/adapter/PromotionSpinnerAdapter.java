package org.teacherbucks.adapter;

import java.util.Vector;

import org.teacherbucks.model.Promotion;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class PromotionSpinnerAdapter extends ArrayAdapter<Promotion> {

	private Context context;
	private Vector<Promotion> itemList;

	public PromotionSpinnerAdapter(Context context, int textViewResourceId, Vector<Promotion> itemList) {

		super(context, textViewResourceId);
		this.context = context;
		this.itemList = itemList;
	}

	public int getCount() {
		return itemList.size();
	}

	public Promotion getItem(int position) {
		return itemList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(itemList.get(position).getTitle());
		//label.setPadding(10, 10, 10, 10);
		
		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(itemList.get(position).getTitle());
		label.setPadding(20, 20, 20, 20);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		return label;
	}

}
