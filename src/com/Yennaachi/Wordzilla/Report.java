package com.Yennaachi.Wordzilla;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Report extends ActionBarActivity {

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
		setContentView(R.layout.activity_report);
	}

	public void send(View view)
	{
		int flag=2;
		EditText word = (EditText) findViewById(R.id.word_rep);
		String word_str = word.getText().toString();
		EditText name = (EditText) findViewById(R.id.name);
		String name_str = name.getText().toString();
		EditText meaning = (EditText) findViewById(R.id.meaning);
		String meaning_str = meaning.getText().toString();
		EditText feedback = (EditText) findViewById(R.id.feedback);
		String feedback_str = feedback.getText().toString();
		String mail_content = "Meaning : "+ meaning_str +
				"\n Name : " + name_str +
				"\n Feedback : "+ feedback_str;
		if(feedback_str.trim().length()==0)
		{flag=1;
		Toast.makeText(getApplicationContext(), "Plz enter Feedback", Toast.LENGTH_SHORT).show();
		}
		if(word_str.trim().length()==0)
		{flag=1;
		Toast.makeText(getApplicationContext(), "Plz enter Word", Toast.LENGTH_SHORT).show();
		}
		if(flag==2)
		{
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"tir.karthi@gmail.com"});
			i.putExtra(Intent.EXTRA_SUBJECT, word_str);
			i.putExtra(Intent.EXTRA_TEXT   , mail_content);
			try {
				startActivity(Intent.createChooser(i, "Send mail..."));
			} 
			catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
		getSupportActionBar().setTitle("Contribute");
		getSupportActionBar().setHomeButtonEnabled(true);
		return true;
	}

	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
	    
	   if(itemId == android.R.id.home){
	    	onBackPressed();
	   } else if(itemId == R.id.action_share){
	    	share();
	   }else if(itemId == R.id.action_fork){
			Intent Git = new Intent(Intent.ACTION_VIEW);
			Git.setData(Uri.parse("https://www.github.com/tirkarthi/Wordzilla"));
			startActivity(Git);
		}
			
		else if(itemId == R.id.action_rating){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.Yennaachi.Wordzilla"));
			startActivity(intent);
		}
	 	
	   else{
	    }
	    return super.onOptionsItemSelected(item);
	  }

	public void share()
	{
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		String sharebody = "Hey I am playing Wordzilla a cool word game that improves vocabulary. Check it out @ http://goo.gl/EHqGA";
		share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Playing Wordzilla");
		share.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
		startActivity(Intent.createChooser(share, "Share word via"));
	}

}
