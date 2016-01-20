package org.teacherbucks.adapter;

import org.teacherbucks.R;
import org.teacherbucks.holder.EmployeeHolder;
import org.teacherbucks.model.Employee;
import org.teacherbucks.utils.ImageLoader;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EmployeeListAdapter extends ArrayAdapter<Employee> {

	private Context context;

	public EmployeeListAdapter(Context context) {
		super(context, R.layout.row_employee_list, EmployeeHolder.getAllEmployees());
		this.context = context;
	}

	static class ViewHolder {
		TextView name;
		TextView phone;
		TextView email;
		ImageView photo;
		View indicator;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View v = convertView;

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row_employee_list, null);

			holder = new ViewHolder();
			holder.name = (TextView) v.findViewById(R.id.text_view_employee_name);
			holder.phone = (TextView) v.findViewById(R.id.text_view_employee_contact_no);
			holder.email = (TextView) v.findViewById(R.id.text_view_employee_email);
			holder.photo = (ImageView) v.findViewById(R.id.employee_profile_photo);
			holder.indicator = v.findViewById(R.id.action_bar_activity_content);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (position < EmployeeHolder.getAllEmployees().size()) {
			final Employee emp = EmployeeHolder.getAllEmployees().elementAt(position);
			holder.name.setText(emp.getName().toString());
			holder.phone.setText(emp.getPhone().toString());
			holder.email.setText(emp.getEmail().toString());
			UrlImageViewHelper.setUrlDrawable(holder.photo, emp.getImage().toString(), R.drawable.employee_thumbnail);
			if (emp.getStatus() == 1) {
				holder.indicator.setBackgroundResource(R.color.green);
			} else {
				holder.indicator.setBackgroundResource(R.color.red);
			}
		}

		return v;
	}

}
