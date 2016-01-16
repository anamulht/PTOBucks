package org.teacherbucks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class BusinessLoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_login);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActionBar().hide();
	}

	public void goToBusinessLoginAction(View view) {
		Intent intent = new Intent(BusinessLoginActivity.this, MainActivity.class);
		intent.putExtra("userG", 0);
		startActivity(intent);
	}
	
}
