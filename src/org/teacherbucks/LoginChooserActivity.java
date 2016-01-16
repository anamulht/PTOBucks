package org.teacherbucks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;;

public class LoginChooserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_chooser);
		getActionBar().hide();
	}

	public void goToBusinessLogin(View view) {
		Intent intent = new Intent(LoginChooserActivity.this, BusinessLoginActivity.class);
		startActivity(intent);
	}

	public void goToEmployeeLogin(View view) {
		Intent intent = new Intent(LoginChooserActivity.this, EmployeeLoginActivity.class);
		startActivity(intent);
		// Toast.makeText(this, "Employee", Toast.LENGTH_LONG).show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			Intent i = new Intent();
			i.setAction(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(i);

		default:
			return false;
		}
	}

}
