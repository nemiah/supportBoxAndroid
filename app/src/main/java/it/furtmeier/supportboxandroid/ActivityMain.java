package it.furtmeier.supportboxandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

public class ActivityMain extends Activity {
	public static boolean running;
	public static ActivityMain instance;
	public static SupportBoxWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		running = true;
		instance = this;

		setFullScreen();

		webView = new SupportBoxWebView(this);
		setContentView(webView);
		webView.setWebViewClient(new SupportBoxViewClient());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		refreshWebView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}


	public void refreshWebView() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String url = sharedPrefs.getString("server_url", getResources().getString(R.string.pref_default_server_url))+"/ubiquitous/CustomerPage/?D=supportBox/SBElectron&cloud="+sharedPrefs.getString("server_cloud", getResources().getString(R.string.pref_default_server_cloud));

		webView.loadUrl(url);
	}

	public void setFullScreen() {
		if(!hasSoftKeys())
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	public boolean hasSoftKeys(){
		boolean hasSoftwareKeys;

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
			Display d = this.getWindowManager().getDefaultDisplay();

			DisplayMetrics realDisplayMetrics = new DisplayMetrics();
			d.getRealMetrics(realDisplayMetrics);

			int realHeight = realDisplayMetrics.heightPixels;
			int realWidth = realDisplayMetrics.widthPixels;

			DisplayMetrics displayMetrics = new DisplayMetrics();
			d.getMetrics(displayMetrics);

			int displayHeight = displayMetrics.heightPixels;
			int displayWidth = displayMetrics.widthPixels;

			hasSoftwareKeys =  (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
		}else{
			boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
			boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
			hasSoftwareKeys = !hasMenuKey && !hasBackKey;
		}
		return hasSoftwareKeys;
	}

	public void setMenuButtonVisibility(boolean visible) {
		try {
			int flag = WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null);
			getWindow().setFlags(visible ? flag : 0, flag);
		} catch (Exception e) {
		}
    }

	public String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}
}
