package it.furtmeier.supportboxandroid;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class SupportBoxWebView extends WebView {
	
	public SupportBoxWebView(ActivityMain multiPOSAndroid) {
		super(multiPOSAndroid);
		
		Log.d("multiPOSWebView", "Started");
		
		final SupportBoxWebView view = this;
		final ActivityMain activity = multiPOSAndroid;
		
		setKeepScreenOn(true);

		WebSettings webSettings = getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDatabasePath("/data/data/"+getContext().getPackageName()+"/databases/");
		
		webSettings.setUserAgentString(webSettings.getUserAgentString() + " supportBox Android/" + multiPOSAndroid.getVersionName());
		
		addJavascriptInterface(new JavaScriptHook(multiPOSAndroid), "android");
	    setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
			public void onSystemUiVisibilityChange(int visibility){
				if(visibility == 0)
					activity.setMenuButtonVisibility(true);
				
				if (visibility != 0)
					return;
			
				Runnable rehideRunnable = new Runnable() {
					public void run() {
						activity.setMenuButtonVisibility(false);
						view.setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE);
					}
				};
				Handler rehideHandler = new Handler();
				rehideHandler.postDelayed(rehideRunnable, 2000);
			}
		});
	    
		setWebChromeClient(new WebChromeClient() {
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("supportBoxWebView", cm.message() + " -- From line "+ cm.lineNumber() + " of "+ cm.sourceId() );
				return true;
			}
		});
	    
		setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE);
	}
}
