package com.Yennaachi.Wordzilla;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Help extends ActionBarActivity {

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
		setContentView(R.layout.activity_help);
		TextView help = (TextView) findViewById(R.id.help_txt);
		help.setText(Html.fromHtml("<br><b>How to Play :</b> <br><br>"+
				"1. Type in your favourite word and press Wordzilla icon.<br>" +
				"2. If the word is found in the dictionary then the score increases by one. <br>" +
				"3. Wordzilla gives an equivalent word that has the first letter as the last letter of your word.<br>" +
				"<b>Example : </b><br>"+
				"You : gotch<b>a</b><br>" +
				"Wordzilla : <b>a</b>bsolut<b>e</b><br>" +
				"You : <b>e</b>nric<b>h</b><br><br>" +
				"<b>Leaderboards :</b><br><br>" +
				"1. You don't need Swarm account to play or use Wordzilla. Just select Play as guest from the log in window.<br>" +
				"2. You can select submit score from the overflow to submit score. We accept 5 scores per user and " +
				"scores of the same value from the same user will be discarded.<br>" +
				"3. You need to log into or create a Swarm account if you don't have, to submit scores and to store your level and scores.<br>"+
				"<br><b>Levels :</b><br><br>"+
				"Each time you cross a certain score your level also increases by one and the difficulty increases.<br><br>"+
				"<b>Goal :</b><br><br>"+
				"Learn more words and gain exposure to new words. You can gain more word exposure in one session of play." +
				"<br><b>Don't skip the words without reading their meaning</b><br>" +
				"If you are not a serious gamer you can use it as a dictionary and contribute whenever and wherever necessary" +
				". We hoe the app helps.<br><br>"+
				"<b>Rules : </b><br><br>" +
				"1. You must enter the word within 25 seconds.<br>" + 
				"2. The word must be in dictionary and also the word should not have been entered before.<br>"+
				"3. If you enter a word that was already entered by you or wordzilla your life decreases by one. You have 5 lives remaining.<br><br>"+
				"<b>Pause/Resume : </b><br><br>"+
				"1. You can use the pause button to pause the timer and <b>only 5 times</b> <br>"+
				"2. The game is passed and saved as you leave the game.<br><br>" +
				"<b>Clear : </b><br><br>" +
				"Clear button at the top with x mark is used to clear the text field<br><br>" +
				"<b>Score : </b><br><br>" +
				"Your score is adaptive and it increases as you play more and more.<br><br>" +
				"<b>Bonus : </b><br><br>" +
				"Words greater than 10 characters gives <br> Bonus = (number of characters) * 10 <br>" +
				"Words greater than 13 characters gives <br> Mega bonus = (number of characters) * 100 <br><br>"+
				"<b>Time : </b><br><br>" +
				"25 seconds time is given for you enter the word. Typing an already entered word doesn't give you extra time it only gives you a notification.<br><br>"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		getSupportActionBar().setTitle("Help");
		getSupportActionBar().setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		if(itemId == android.R.id.home){
			onBackPressed();
		}
		else if(itemId == R.id.action_share){
			share();
		}
		else if(itemId == R.id.action_fork){
			Intent Git = new Intent(Intent.ACTION_VIEW);
			Git.setData(Uri.parse("https://www.github.com/tirkarthi/Wordzilla"));
			startActivity(Git);
		}
			
		else if(itemId == R.id.action_rating){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.Yennaachi.Wordzilla"));
			startActivity(intent);
		}
		else
		{
			
		}
		return super.onOptionsItemSelected(item);
	}

	public void share()
	{
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		String sharebody = "Hey I am playing Wordzilla a cool word game that improves vocabulary. Check it out @ market://details?id=com.Yennaachi.Wordzilla";
		share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Playing Wordzilla");
		share.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
		startActivity(Intent.createChooser(share, "Share word via"));
	}
}
