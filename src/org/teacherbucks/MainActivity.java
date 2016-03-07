package org.teacherbucks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.teacherbucks.fragments.BizChangePassword;
import org.teacherbucks.fragments.DataPreferencesFragment;
import org.teacherbucks.fragments.EmailVoucherFragment;
import org.teacherbucks.fragments.EmpChangePassword;
import org.teacherbucks.fragments.HomeFragment;
import org.teacherbucks.fragments.ManageEmployeeFragment;
import org.teacherbucks.fragments.QRVoucherFragment;
import org.teacherbucks.fragments.SMSVoucherFragment;
import org.teacherbucks.fragments.SalesDataFragment;
import org.teacherbucks.fragments.UsernameVoucherFragment;
import org.teacherbucks.holder.LogInDataHolder;
import org.teacherbucks.utils.Constant;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends Activity {

	public View mCustomView;
	private TextView actionBarTitle;
	private ImageView copilotmenuback;

	public View sliderMenuContent;
	private DrawerLayout mDrawerLayout;
	private LinearLayout layout_slider;
	private ActionBarDrawerToggle mDrawerToggle;

	ProgressDialog dialog;

	private boolean backKeyFlag = false;
	private int userGroup = 0;

	private Uri imageUri;
	private Bitmap voucherBitmap;
	boolean isVoucherPicTaken;

	public int getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(int userGroup) {
		this.userGroup = userGroup;
	}

	public boolean isVoucherPicTaken() {
		return isVoucherPicTaken;
	}

	public void setVoucherPicTaken(boolean isVoucherPicTaken) {
		this.isVoucherPicTaken = isVoucherPicTaken;
	}

	public Bitmap getVoucherBitmap() {
		return voucherBitmap;
	}

	public void setVoucherBitmap(Bitmap voucherBitmap) {
		this.voucherBitmap = voucherBitmap;
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}

	public boolean isBackKeyFlag() {
		return backKeyFlag;
	}

	public void setBackKeyFlag(boolean backKeyFlag) {
		this.backKeyFlag = backKeyFlag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setUserGroup(getIntent().getExtras().getInt("userG"));

		setCustomActionBarAndSetUpSlideMenu();
		goToHomeFragment();

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait...");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);

	}

	private void goToHomeFragment() {
		backKeyFlag = false;
		final FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
		setActionBarTitle(LogInDataHolder.getLogInData().getCompany().getTitle());
	}

	private void setCustomActionBarAndSetUpSlideMenu() {

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
		mActionBar.setCustomView(mCustomView);
		copilotmenuback = (ImageView) mCustomView.findViewById(R.id.copilotmenuback);

		((ImageView) mCustomView.findViewById(R.id.title_home_icon)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				goToHomeFragment();
			}
		});

		actionBarTitle = (TextView) mCustomView.findViewById(R.id.title);
		setActionBarTitle(LogInDataHolder.getLogInData().getCompany().getTitle());

		mActionBar.setDisplayShowCustomEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		layout_slider = (LinearLayout) findViewById(R.id.layout_slider);

		if (userGroup == 0) {
			sliderMenuContent = mInflater.inflate(R.layout.slider_menu_layout_business, null);

			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_data_preferences))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDrawerLayout.closeDrawer(layout_slider);
							backKeyFlag = true;
							final FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction()
									.replace(R.id.frame_container, new DataPreferencesFragment()).commit();
							setActionBarTitle("Settings");
						}
					});

			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_sales_data))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDrawerLayout.closeDrawer(layout_slider);
							backKeyFlag = true;
							final FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, new SalesDataFragment())
									.commit();
							setActionBarTitle("Settings");
						}
					});

			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_manage_employees))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mDrawerLayout.closeDrawer(layout_slider);
							backKeyFlag = true;
							final FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction()
									.replace(R.id.frame_container, new ManageEmployeeFragment()).commit();
							setActionBarTitle("Settings");
						}
					});

			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_log_out))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							new LogOutAsyncTask().execute("params");
						}
					});
			
			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_change_pass_biz))
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mDrawerLayout.closeDrawer(layout_slider);
					backKeyFlag = true;
					final FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, new BizChangePassword()).commit();
					setActionBarTitle("Change Password");
				}
			});

			((TextView) sliderMenuContent.findViewById(R.id.textview_menu_banner_title_biz))
					.setText(LogInDataHolder.getLogInData().getCompany().getTitle());

		} else if (userGroup == 1) {
			sliderMenuContent = mInflater.inflate(R.layout.slider_menu_layout_employee, null);

			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_log_out_emp))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							new LogOutAsyncTask().execute("param");
						}
					});
			
			((TextView) sliderMenuContent.findViewById(R.id.text_view_menu_change_pass_emp))
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mDrawerLayout.closeDrawer(layout_slider);
					backKeyFlag = true;
					final FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, new EmpChangePassword()).commit();
					setActionBarTitle("Change Password");
				}
			});

			((TextView) sliderMenuContent.findViewById(R.id.textview_menu_banner_title_emp))
					.setText(LogInDataHolder.getLogInData().getUser().getFirst_name() + " "
							+ LogInDataHolder.getLogInData().getUser().getLast_name());
		}

		layout_slider.addView(sliderMenuContent);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, R.drawable.ic_launcher);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setFocusableInTouchMode(false);

		copilotmenuback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "Coming Soon !",
				// Toast.LENGTH_LONG).show();

				if (mDrawerLayout.isDrawerOpen(layout_slider)) {
					mDrawerLayout.closeDrawer(layout_slider);
				} else {
					mDrawerLayout.openDrawer(layout_slider);
				}
			}
		});

	}

	public void setActionBarTitle(String title) {
		actionBarTitle.setText(title);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isBackKeyFlag()) {
				goToHomeFragment();
			} else {
				// android.os.Process.killProcess(android.os.Process.myPid());

				Intent i = new Intent();
				i.setAction(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_HOME);
				this.startActivity(i);

			}

		default:
			return false;
		}
	}

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { super.onActivityResult(requestCode, resultCode, data);
	 * switch (requestCode) { case 100: if (resultCode == Activity.RESULT_OK) {
	 * Uri selectedImage = getImageUri();
	 * this.getContentResolver().notifyChange(selectedImage, null);
	 * ContentResolver cr = this.getContentResolver(); Bitmap bitmap; try {
	 * bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
	 * selectedImage);
	 * 
	 * setVoucherBitmap(bitmap); setVoucherPicTaken(true);
	 * 
	 * } catch (Exception e) { Toast.makeText(this, "Failed to load",
	 * Toast.LENGTH_SHORT).show(); Log.e("Camera", e.toString()); } } } }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("Req Code: " + requestCode);
		//switch (requestCode) {
		//case 100:
			if (RESULT_OK == resultCode) {
				// Get Extra from the intent
				Bundle extras = data.getExtras();
				// Get the returned image from extra
				Bitmap bmp = (Bitmap) extras.get("data");
				
				DisplayMetrics displaymetrics = new DisplayMetrics();
				this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

				setVoucherBitmap(Bitmap.createScaledBitmap(bmp, displaymetrics.widthPixels-80, displaymetrics.widthPixels, true));
				setVoucherPicTaken(true);

				setVoucherImage(requestCode);
				// Toast.makeText(this, bmp.toString(),
				// Toast.LENGTH_LONG).show();
			}
		//}
	}
	
	private void setVoucherImage(int fragmentCode) {

		if (fragmentCode == Constant.emailFragmentCode) {
			EmailVoucherFragment email = (EmailVoucherFragment) getFragmentManager().findFragmentById(R.id.frame_container);
			System.out.println("Email Fragment");
			email.setImage();
		}

		if (fragmentCode == Constant.smsFragmentCode) {

			SMSVoucherFragment sms = (SMSVoucherFragment) getFragmentManager().findFragmentById(R.id.frame_container);
			System.out.println("SMS Fragment");
			sms.setImage();
		}

		if (fragmentCode == Constant.usernamelFragmentCode) {

			UsernameVoucherFragment username = (UsernameVoucherFragment) getFragmentManager().findFragmentById(R.id.frame_container);
			System.out.println("Username Fragment");
			username.setImage();
		}

		if (fragmentCode == Constant.qrFragmentCode) {

			QRVoucherFragment qr = (QRVoucherFragment) getFragmentManager().findFragmentById(R.id.frame_container);
			System.out.println("Qr Fragment");
			qr.setImage();
		}
	}

	class LogOutAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(
						Constant.baseURL + "api/v1/auth/logout?token=" + LogInDataHolder.getLogInData().getToken()));

			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			dialog.dismiss();
			mDrawerLayout.closeDrawer(layout_slider);
			Intent intent = new Intent(MainActivity.this, LoginChooserActivity.class);
			startActivity(intent);
		}

	}
}
