package com.Yennaachi.Wordzilla;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Dictionary extends ActionBarActivity {

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		TextView Wordzilla_meaning_dict = (TextView) findViewById(R.id.wordzilla_meaning_dict);
		Wordzilla_meaning_dict.setHint(Html.fromHtml("<br>If you find the app <b>useful</b> please take a minute of" +
				" your busy time to <b>share me your experience</b>** that helps me to grow. If you find the word useful" +
				" for someone else click the share button to share the word with them. Hope this app helps.<br>" +
				"** You can write me your experience or rate the app at Play Store by the time you start and stop" +
				" reading this hint :-). <br>"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		getSupportActionBar().setTitle("Dictionary");
		getSupportActionBar().setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
		} else if (itemId == R.id.action_fork) {
			Intent Git = new Intent(Intent.ACTION_VIEW);
			Git.setData(Uri.parse("https://www.github.com/tirkarthi/Wordzilla"));
			startActivity(Git);
		} else if (itemId == R.id.action_rating) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.Yennaachi.Wordzilla"));
			startActivity(intent);
		} else if (itemId == R.id.action_share) {
			share();
		} else {
		}
		return super.onOptionsItemSelected(item);
	}

	public void enter(View view)
	{
		InputMethodManager inputManager = 
				(InputMethodManager) this.
				getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(
				this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		String meaning = "";
		String database = Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/testwords";
		EditText word = (EditText) findViewById(R.id.input_dict);
		TextView wordzilla = (TextView) findViewById(R.id.wordzilla_meaning_dict);
		String word_str = word.getText().toString();
		word_str = word_str.trim();
		if((word_str.trim().length()!=0) && (word_str.charAt(0)!=' ') && (!Character.isDigit(word_str.charAt(0))))
		{
			new LongOperation().execute(word_str);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Word not found.", Toast.LENGTH_LONG).show();
		}
	}
	public void share()
	{
		TextView meaning = (TextView) findViewById(R.id.wordzilla_meaning_dict);
		EditText word = (EditText) findViewById(R.id.input_dict);
		if(word.getText().toString().trim().length()!=0 && meaning.getText().toString().trim().length()!=0)
		{
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("text/plain");
			String sharebody = word.getText().toString()+"\n"+meaning.getText().toString()+" shared via #Wordzilla ";
			share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wordzilla");
			share.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
			startActivity(Intent.createChooser(share, "Share word via"));
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Fields missing. Have an input and meaning.", Toast.LENGTH_SHORT).show();
		}
	}
	private class LongOperation extends AsyncTask<String, Void, String> {
		ProgressDialog pro = new ProgressDialog(Dictionary.this);
		@Override
		protected void onPreExecute() {
			TextView txt = (TextView) findViewById(R.id.wordzilla_meaning_dict);
			pro.show();
			pro.setMessage("Loading definitions");
			pro.setCancelable(false);
		}
		@Override
		protected String doInBackground(String... params) {
			String meaning = "";
			String database = Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/testwords";
			try {
				EditText word = (EditText) findViewById(R.id.input_dict);
				TextView wordzilla = (TextView) findViewById(R.id.wordzilla_meaning_dict);
				String word_str = word.getText().toString();
				SQLiteDatabase words = openOrCreateDatabase(database, MODE_PRIVATE, null);
				Cursor c = words.rawQuery("select * from "+word_str.charAt(0)+" where word = '"+word_str+"';",null);
				if(c!=null){
					while(c.moveToNext()){
						meaning += "<br><i>"+c.getString(c.getColumnIndex("part"))+"</i><br>"+"<b>"+c.getString(c.getColumnIndex("meaning"))+"</b><br>";
					}	
					c.close();
					words.close();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Word not found", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e("LongOperation", "Interrupted", e);
				return "<br><br><b>No word found</b>. If you are sure that the word<b>(Not Name or Place)</b> exists you can contribute the word to us. Take just a minute to mail me tir.karthi@gmail.com or you are free to" +
						"edit the database at Wordzilla folder.";
			}
			return meaning;
		}
		@Override
		protected void onPostExecute(String meaning) {
			pro.dismiss();
			TextView txt = (TextView) findViewById(R.id.wordzilla_meaning_dict);
			if(meaning.trim().length()!=0){
				txt.setText(Html.fromHtml(meaning));
			}
			else
			{
				txt.setText(Html.fromHtml("<br><br><b>No word found</b>. If you are sure that the word<b>(Not Name or Place)</b> exists you can contribute the word to us. Take just a minute to mail me tir.karthi@gmail.com or you are free to" +
						"edit the database at Wordzilla folder."));	
			}
		}
	}
}
