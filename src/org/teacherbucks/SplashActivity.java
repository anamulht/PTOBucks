package org.teacherbucks;

import org.teacherbucks.LoginChooserActivity;
import org.teacherbucks.R;
import org.teacherbucks.SplashActivity;
import org.teacherbucks.utils.AlertMessage;
import org.teacherbucks.utils.SharedPreferencesHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class SplashActivity extends Activity {

	private Context context;
	protected static final int TIMER_RUNTIME = 500; // in ms --> 10s
	protected boolean mbActive;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		context = this;

		/*if (!SharedPreferencesHelper.isOnline(this)) {
			AlertMessage.showMessage(this, "Attention!",
					"To Use this Application Please Connect to internet");

		} else {*/
			initUi();


		//}

	}

	private void initUi() {

		final Thread timerThread = new Thread() {
			@Override
			public void run() {
				mbActive = true;
				try {
					int waited = 0;
					while (mbActive && (waited < TIMER_RUNTIME)) {
						sleep(5);
						if (mbActive) {
							waited += 5;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					onContinue();
				}
			}
		};
		timerThread.start();
	}

	protected void onResume() {
		
		super.onResume();
		overridePendingTransition(0, 0);
	}

	public void onContinue() {
		// perform any final actions here

		runOnUiThread(new Runnable() {

			public void run() {

				Intent intent = new Intent(SplashActivity.this,
						LoginChooserActivity.class);
				startActivity(intent);
				finish();

			}
		});

	}

	String results = "";
	String keword_response = "";




	public void onDestroy() {

	

		super.onDestroy();

	}

}
