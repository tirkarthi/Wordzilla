package com.Yennaachi.Wordzilla;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class Intro extends Activity {
	
	Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	String Error = "<br><b>Error!</b><br>" +
			"Wordzilla works under the presence of Database which is stored in your memory card.<br>" +
			"Please check the following :<br>" +
			"1. External storage is present.<br>" +
			"2. External storage has a database file of name teztwords in Wordzilla folder of around 40 MB(Size is approx.)<br>" +
			"3. External storage has adequate memory and readable<br>" +
			"4. Try reinstalling the app." +
			"Sorry for the inconvenience caused!. If problem persists do criticise the hell out of me at tir.karthi@gmail.com";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isSDPresent){
		setContentView(R.layout.activity_intro);
		SharedPreferences score = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		int counter = score.getInt("counter",0);
		if(counter==0)
		{
			try{
				Log.v("Copy", "Copy started");
				new CopyOperation().execute("");
			}
			catch(Exception e)
			{
				Log.e("Setup error", "Database error",e);
			}
		}
		}
		else
		{
			setContentView(R.layout.activity_error);
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(Html.fromHtml(Error));
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	public void newgame(View view)
	{
		if(isSDPresent)
		{
			Intent New_Game = new Intent(this, MainActivity.class);
			startActivity(New_Game);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Sorry. No external storage found", Toast.LENGTH_LONG).show();
		}

	}
	public void aboutus(View view)
	{
		Intent about = new Intent(this, About.class);
		startActivity(about);
	}
	public void help(View view)
	{
		Intent help = new Intent(this, Help.class);
		startActivity(help);	
	}
	public void words(View view)
	{
		Intent report = new Intent(this, Report.class);
		startActivity(report);	
	}

	public void dictionary(View view)
	{		
		if(isSDPresent)
		{
				Intent dict = new Intent(this, Dictionary.class);
				startActivity(dict);
		}
		else{
			Toast.makeText(getApplicationContext(), "Sorry. No external storage found", Toast.LENGTH_LONG).show();
		}

	}
	public void rateus(View view)
	{
		if(isApplicationIstalledByPackageName ("com.slideme.sam.manager")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("sam://details?id=com.Yennaachi.Wordzilla"));
			startActivity(intent);
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://slideme.org/application/wordzilla"));
			startActivity(intent);
		}
	}

	public void flag()
	{
		SharedPreferences exit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		int flag = exit.getInt("flag",0);
		Editor exit_edit = exit.edit();
		exit_edit.putInt("flag",++flag);
		exit_edit.commit();
	}
	public boolean isApplicationIstalledByPackageName(String packageName) {
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		if (packages != null && packageName != null) {
			for (PackageInfo packageInfo : packages) {
				if (packageName.equals(packageInfo.packageName)) {
					return true;
				}
			}
		}
		return false;
	}


	private class CopyOperation extends AsyncTask<String, Void, String> {
		ProgressDialog pro = new ProgressDialog(Intro.this);
		int check = 0;
		@Override
		protected void onPreExecute() {
			pro.show();
			pro.setTitle("One time activity");
			pro.setMessage("Copying files");
			pro.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... params) {
			try{
				copyFilesToSdCard();
			}
			catch(Exception e){
				check = 1;
				Log.e("Copy error", "Copy error log", e);
			}
			return "Copy End";
		}

		protected void onPostExecute(String meaning) {
			if(check==0){
				pro.dismiss();
				new ZipOperation().execute("");
			}
			else{
			setContentView(R.layout.activity_error);
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(Html.fromHtml(Error));
			}
		}
	}
	private class ZipOperation extends AsyncTask<String, Void, String> {
		int check = 0;
		ProgressDialog pro = new ProgressDialog(Intro.this);
		@Override
		protected void onPreExecute() {
			pro.show();
			pro.setMessage("One time task. Just a minute. please wait..");
			pro.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... params) {
			try{
				String zipFilename = "/"+Environment.getExternalStorageDirectory() + "/Wordzilla/testwords.zip"; 
				String unzipLocation = "/"+Environment.getExternalStorageDirectory() + "/Wordzilla/"; 
				Log.v("Zip", "zip started");
				unpackZip(unzipLocation, "testwords.zip");
				SharedPreferences score = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Editor score_inc = score.edit();
				score_inc.putInt("counter", 1);
				score_inc.commit();
				Log.v("Zip", "zip ended");
				Log.v("Zip", "Zip delete");
				File file = new File("/"+Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/testwords.zip");
				if(file.exists()){
					file.delete();
				}
				file = new File("/"+Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/swarm_app_config.json");
				if(file.exists()){
					file.delete();
				}
			}
			catch(Exception e)
			{
				check = 1;
				Log.e("Zip error", "zip is unsuccessful", e);
			}
			return "Reset End";
		}

		protected void onPostExecute(String meaning) {
			if(check==0){
				Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
				pro.dismiss();
			}
			else{
			setContentView(R.layout.activity_error);
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(Html.fromHtml(Error));
			}
		}
	}

	private boolean unpackZip(String path, String zipname)
	{       
		InputStream is;
		ZipInputStream zis;
		try 
		{
			String filename;
			is = new FileInputStream(path + zipname);
			zis = new ZipInputStream(new BufferedInputStream(is));          
			ZipEntry ze;
			byte[] buffer = new byte[8192];	//8192
			int count;

			while ((ze = zis.getNextEntry()) != null) 
			{
				filename = ze.getName();
				if (ze.isDirectory()) {
					File fmd = new File(path + filename);
					fmd.mkdirs();
					continue;
				}

				FileOutputStream fout = new FileOutputStream(path + filename);
				while ((count = zis.read(buffer)) != -1) 
				{
					fout.write(buffer, 0, count);             
				}

				fout.close();               
				zis.closeEntry();
			}

			zis.close();
		} 
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void copyFilesToSdCard() {
		copyFileOrDir(""); // copy all files in assets folder in my project
	}

	private void copyFileOrDir(String path) {
		String TARGET_BASE_PATH = Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/";
		AssetManager assetManager = this.getAssets();
		String assets[] = null;
		try {
			Log.i("tag", "copyFileOrDir() "+path);
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(path);
			} else {
				String fullPath =  TARGET_BASE_PATH + path;
				Log.i("tag", "path="+fullPath);
				File dir = new File(fullPath);
				if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
					if (!dir.mkdirs());
				Log.i("tag", "could not create dir "+fullPath);
				for (int i = 0; i < assets.length; ++i) {
					String p;
					if (path.equals(""))
						p = "";
					else 
						p = path + "/";

					if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
						copyFileOrDir( p + assets[i]);
				}
			}
		} catch (IOException ex) {
			Log.e("tag", "I/O Exception", ex);
		}
	}

	private void copyFile(String filename) {
		AssetManager assetManager = this.getAssets();
		String TARGET_BASE_PATH = Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/";
		InputStream in = null;
		OutputStream out = null;
		String newFileName = null;
		try {
			Log.i("CopyFunction", "copyFile() "+filename);
			in = assetManager.open(filename);
			if (filename.endsWith(".mp3")) // extension was added to avoid compression on APK file
				newFileName = TARGET_BASE_PATH + filename.substring(0, filename.length()-4);
			else
				newFileName = TARGET_BASE_PATH + filename;
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[4096];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			Log.e("CopyFunction", "Exception in copyFile() of "+newFileName);
			Log.e("CopyFunction", "Exception in copyFile() "+e.toString());
		}

	}
}