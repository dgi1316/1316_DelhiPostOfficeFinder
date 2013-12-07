package com.hard.targets.delhi.post.office.finder;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

	Activity context;
	ArrayList<String> office = new ArrayList<String>();
	ArrayList<String> district = new ArrayList<String>();
	ArrayList<String> pin = new ArrayList<String>();

	public CustomListAdapter(Activity context, ArrayList<String> office, ArrayList<String> district, ArrayList<String> pin) {
		super(context, R.layout.list_item, pin);
		this.context = context;
		this.office = office;
		this.district = district;
		this.pin = pin;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = context.getLayoutInflater().inflate(R.layout.list_item, null, true);
		TextView txtOffice = (TextView) rowView.findViewById(R.id.tvArea);
		TextView txtDistrict = (TextView) rowView.findViewById(R.id.tvDistrict);
		TextView txtPin = (TextView) rowView.findViewById(R.id.tvPin);
		txtOffice.setText(office.get(position).toString());
		txtDistrict.setText(district.get(position).toString());
		txtPin.setText(pin.get(position).toString());
		return rowView;
	}

}
