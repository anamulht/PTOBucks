package org.teacherbucks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class EmployeeLoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_login);
		getActionBar().hide();
	}
	
	public void goToEmployeeLoginAction(View view) {
		Intent intent = new Intent(EmployeeLoginActivity.this, MainActivity.class);
		intent.putExtra("userG", 1);
		startActivity(intent);
		// Toast.makeText(this, "Employee", Toast.LENGTH_LONG).show();
	}

}
