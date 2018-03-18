package it.furtmeier.supportboxandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


public class JavaScriptHook {
	private Context mContext = null;
	private ActivityMain supportBoxAndroid;

	@JavascriptInterface
	public void showSettings(){
		mContext.startActivity(new Intent(mContext, ActivitySettings.class));
	}

	@JavascriptInterface
	public void openBrowser(String url){
		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}

	public JavaScriptHook(ActivityMain supportBoxAndroid) {
		this.supportBoxAndroid = supportBoxAndroid;
		mContext = supportBoxAndroid;
	}


	private static boolean downloadFile(String url, File outputFile) throws IOException {
		Log.d("supportBox", url);

		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		int contentLength = conn.getContentLength();

		DataInputStream stream = new DataInputStream(u.openStream());

		byte[] buffer = new byte[contentLength];
		stream.readFully(buffer);
		stream.close();

		DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
		fos.write(buffer);
		fos.flush();
		fos.close();

		return true;
	}
}
