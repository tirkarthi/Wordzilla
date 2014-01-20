package com.Yennaachi.Wordzilla;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmAchievement;
import com.swarmconnect.SwarmLeaderboard;


public class MainActivity extends ActionBarActivity{

	Activity activity = (Activity) this;
	int score_in, i=1, pause_time=1,time=5, has_user_entered_a_word=0, has_user_entered_level = 0;
	private CountDownTimer count;
	long time_remain;
	char wordzilla_last = 'a';
	String database = Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/testwords";
	int present_score, level;
	long start = 25*1000, interval = 1*1000;
	int pause_flag,  reset = 1;
	String wordzilla_name="Kid";
	int life;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		try
		{
			new PreLoadOperation().execute("");
			setContentView(R.layout.activity_main);
			count = new MyCount (start, interval);
			final TextView score_view = (TextView) findViewById(R.id.score);
			TextView time_rem = (TextView) findViewById(R.id.timer);
			TextView life_txt = (TextView) findViewById(R.id.life);
			TextView Wordzilla_meaning = (TextView) findViewById(R.id.wordzilla_meaning);
			TextView wordzilla = (TextView) findViewById(R.id.wordzilla);
			Wordzilla_meaning.setMovementMethod(new ScrollingMovementMethod());
			final ImageButton go = (ImageButton) findViewById(R.id.Go);
			final EditText enter = (EditText) findViewById(R.id.input);
			SQLiteDatabase words = openOrCreateDatabase(database, MODE_PRIVATE, null);
			Cursor score_cur = words.rawQuery("select * from data ;", null);
			score_cur.moveToFirst();
			SharedPreferences score_level = PreferenceManager.getDefaultSharedPreferences(this);
			int level = score_level.getInt("level", 2);
			int score_begin = score_cur.getInt(score_cur.getColumnIndex("score"));
			present_score = score_begin;
			wordzilla_name = score_cur.getString(score_cur.getColumnIndex("player"));
			wordzilla.setText(wordzilla_name);
			life = score_cur.getInt(score_cur.getColumnIndex("life"));
			enter.setHint("Your word");
			life_txt.setText(""+life);
			Log.i("ENTRY DATA",""+life + " Wordzilla_name : " + wordzilla_name + " Score : " + score_begin);
			score_view.setText("" + score_begin);
			score_in = 10;
			time_rem.setText("25");
			time_rem.setTextColor(Color.rgb(7, 239, 7));
			score_cur.close();
			words.close();
			count = new MyCount (start, interval);
		}
		catch(Exception e)
		{
			String Error = "<br><b>Error!</b><br>" +
					"Wordzilla works under the presence of Database which is stored in your memory card.<br>" +
					"Please check the following :<br>" +
					"1. External storage is present.<br>" +
					"2. External storage has a database file of name teztwords in Wordzilla folder of around 40 MB(Size is approx.)<br>" +
					"3. External storage has adequate memory and readable<br>" +
					"4. Try reinstalling the app." +
					"Sorry for the inconvenience caused!. If problem persists do criticise the hell out of me at tir.karthi@gmail.com";
			Log.e("Entry", "Error in entry", e);
			setContentView(R.layout.activity_error);
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(Html.fromHtml(Error));
			Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
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
	public void enter(View view)
	{ 
		int score_bonus = 0;
		has_user_entered_a_word=1;
		final TextView score_view = (TextView) findViewById(R.id.score);
		TextView time_rem = (TextView) findViewById(R.id.timer);
		TextView life_txt = (TextView) findViewById(R.id.life);
		final TextView Wordzilla_meaning = (TextView) findViewById(R.id.wordzilla_meaning);
		Wordzilla_meaning.setMovementMethod(new ScrollingMovementMethod());
		SQLiteDatabase words = openOrCreateDatabase(database, MODE_PRIVATE, null);
		time_rem.setTextColor(Color.rgb(7, 239, 7));
		EditText enter = (EditText) findViewById(R.id.input);
		InputMethodManager inputManager = 
				(InputMethodManager) this.
				getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(
				this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		String enter_str = enter.getText().toString();
		int enter_length = enter_str.trim().length();
		if((enter_length!=0) && (enter_str.charAt(0)!=' ') && (!Character.isDigit(enter_str.charAt(0))))
		{	
			char you_test= enter_str.charAt(0);
			enter_str = enter_str.toLowerCase();
			if(i==1||you_test==wordzilla_last)
			{
				try
				{
					Cursor c = words.rawQuery("select * from "+you_test+" where word = '"+enter_str+"';",null);
					try
					{
						if(c.moveToNext())
						{
							c.moveToFirst();
							String enter_txt = c.getString(c.getColumnIndex("word"));
							int flag_int = c.getInt(c.getColumnIndex("id"));
							if(flag_int==0)
							{
								//you
								int word_zilla = enter_str.length();
								final char wordzilla_test = enter_txt.charAt(--word_zilla);
								words.execSQL("UPDATE "+you_test+" SET id='"+1+"'WHERE word='"+enter_str+"'");
								c.close();
								words.close();
								new LongOperation().execute("");
								if(enter_length>9)
								{
									if(enter_length>=14)
									{
										Toast.makeText(getApplicationContext(), "Congrats! Mega Bonus : " + enter_length*100 , Toast.LENGTH_LONG).show();
										score_bonus+=enter_length*100;
									}
									else
									{	
										Toast.makeText(getApplicationContext(), "Bonus : " + enter_length*10 , Toast.LENGTH_LONG).show();
										score_bonus+=enter_length*10;
									}
								}
								//score
								present_score += score_in + score_bonus;
								Runnable runnable = new Runnable() {
									@Override
									public void run() {
										for(int i=present_score-score_in;i<=present_score;i++)
										{
											final int value = i;
											try
											{
												Thread.sleep(time);			
											}
											catch(InterruptedException e)
											{
												Log.e("Score thread", "Score update error", e);
											}
											score_view.post(new Runnable(){
												@Override
												public void run()
												{
													score_view.setText("" + value);
												}
											});	
										}
									}
								};
								new Thread(runnable).start();
								score_in = score_in + 10;
								//hint
								count.cancel();
								pause_flag=1;
								i=2;
								SharedPreferences score_level = PreferenceManager.getDefaultSharedPreferences(this);
								Editor level_editor = score_level.edit();
								level = score_level.getInt("level", 1);
								Log.i("Score : ", "Score_in :" + score_in + " score_present : "+ present_score + " level - 1:"+ (level-1) +" level : " + level);
								if (present_score>0&&present_score<1500&&level==1) {
									wordzilla_name = "Jack larson";
									level_editor.putInt("level", 2);
									level_editor.commit();
								} else if (present_score>=1500&&present_score<3500&&level==2) {
									SwarmAchievement.unlock(17858);
									wordzilla_name = "Tyler Burden";
									level_editor.putInt("level", 3);
									level_editor.commit();
								} else if (present_score>=3500&&present_score<5000&&level==3) {
									SwarmAchievement.unlock(17898);
									wordzilla_name = "Cracker";
									level_editor.putInt("level", 4);
									level_editor.commit();
								} else if (present_score>=5000&&present_score<8000&&level==4) {
									wordzilla_name = "Sane";
									SwarmAchievement.unlock(17900);
									level_editor.putInt("level", 5);
									level_editor.commit();
								} else if (present_score>=8000&&present_score<15000&&level==5) {
									wordzilla_name="QueenPin";
									SwarmAchievement.unlock(17902);
									level_editor.putInt("level", 6);
									level_editor.commit();
								} else if (present_score>=15000&&present_score<20000&&level==6) {
									wordzilla_name="Waldemort";
									SwarmAchievement.unlock(17904);
									level_editor.putInt("level", 7);
									level_editor.commit();
								} else if (present_score>=20000&&present_score<30000&&level==7) {
									wordzilla_name="Vincent";
									SwarmAchievement.unlock(17906);
									level_editor.putInt("level", 8);
									level_editor.commit();
								} else if (present_score>=30000&&present_score<35000&&level==8) {
									wordzilla_name="Mundane";
									SwarmAchievement.unlock(17908);
									level_editor.putInt("level", 9);
									level_editor.commit();
								} else if (present_score>=35000&&present_score<50000&&level==9) {
									wordzilla_name="Warden";
									SwarmAchievement.unlock(17910);
									level_editor.putInt("level", 10);
									level_editor.commit();
								} else if (present_score>=50000&&present_score<80000&&level==10) {
									wordzilla_name="Magneta";
									SwarmAchievement.unlock(17912);
									level_editor.putInt("level", 11);
									level_editor.commit();
								} else if (present_score>=80000&&present_score<110000&&level==11) {
									wordzilla_name="Insane";
									SwarmAchievement.unlock(17914);
									level_editor.putInt("level", 12);
									level_editor.commit();
								} else if (present_score>=110000&&present_score<140000&&level==11) {
									wordzilla_name="Belissa";
									SwarmAchievement.unlock(17916);
									level_editor.putInt("level", 12);
									level_editor.commit();
								} else if (present_score>=140000&&present_score<180000&&level==12) {
									wordzilla_name="Boristein";
									SwarmAchievement.unlock(17918);
									level_editor.putInt("level", 13);
									level_editor.commit();
								}
								else if (present_score>=180000&&present_score<300000&&level==13) {
									wordzilla_name="Wedison";
									SwarmAchievement.unlock(17922);
									level_editor.putInt("level", 14);
									level_editor.commit();
								}
								else if (present_score>=300000&&present_score<600000&&level==14) {
									wordzilla_name="Wesla";
									SwarmAchievement.unlock(17924);
									level_editor.putInt("level", 15);
									level_editor.commit();
								}
								else if(present_score>600000&&level==15) {
									Toast.makeText(this, "You are a true warrior. Atlast you have entered the castle", Toast.LENGTH_SHORT).show();
									wordzilla_name="Wordzilla";
									SwarmAchievement.unlock(17926);
									level_editor.putInt("level", 14);
									level_editor.commit();
								}
								if (Swarm.isLoggedIn()) {
									Swarm.user.saveCloudData("levelProgress", "Level : "+level);
								}
							}
							else
							{
								if(flag_int==2||flag_int==1)
								{ 
									life--;
									life_txt.setText("" + life);
									if(flag_int==1)
									{
										Toast.makeText(this, "You already entered it.", Toast.LENGTH_SHORT).show();
									}
									else
									{
										Toast.makeText(this, "Wordzilla already entered it.", Toast.LENGTH_SHORT).show();
									}
									if(life==0)
									{
										reset();
									}
								}
							}
						}
						else
						{
							words.close();
							Toast.makeText(this, "No word found. Report if wrong", Toast.LENGTH_SHORT).show();
						}
					}
					catch(Exception e)
					{
						Log.e("Exception in checking word", "Word-checking exception", e);
						Toast.makeText(this, "No word found. Report if wrong", Toast.LENGTH_SHORT).show();	
					}
				}
				catch(Exception e)
				{
					Log.e("Exception in checking word", "Word-checking exception - apostrophe", e);
					Toast.makeText(this, "Dont use apostrophe or special character words ", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(this, "plz enter a word starting with " + wordzilla_last, Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(this, "plz enter an appropriate word ", Toast.LENGTH_SHORT).show();	
		}
	}

	public void reset()
	{
		SQLiteDatabase words = openOrCreateDatabase(database, MODE_PRIVATE, null);
		try
		{
			String wordzilla_txt;
			has_user_entered_a_word=0;
			TextView score_view = (TextView) findViewById(R.id.score);
			TextView time_rem = (TextView) findViewById(R.id.timer);
			TextView life_txt = (TextView) findViewById(R.id.life);
			EditText enter = (EditText) findViewById(R.id.input);
			TextView txt_wordzilla = (TextView) findViewById(R.id.wordzilla);
			TextView Wordzilla_meaning_txt = (TextView) findViewById(R.id.wordzilla_meaning);
			Cursor score_high = words.rawQuery("select * from high;", null);
			score_high.moveToFirst();
			int score_high_int = score_high.getInt(score_high.getColumnIndex("score"));
			score_high.close();
			words.execSQL("update data set score = '"+0+"';");
			words.execSQL("update data set player = 'Jack Larson';");
			words.execSQL("update data set life = '"+5+"';");
			int score_view_int = Integer.parseInt(score_view.getText().toString());
			count.cancel();
			Log.i(" Score_reset ","Score_high_int : " + score_high_int + " present_score : " + present_score + " score_view_int : " + score_view_int);
			if(reset==1)
			{
				if(present_score>score_high_int)
				{
					words.execSQL("update high set score = '"+score_view_int+"';");
					Swarm.user.saveCloudData("High Score", "High Score : " + present_score);
					SwarmLeaderboard.submitScore(13268, score_view_int);
					new ResetOperation().execute("Reset");
				}
				else
				{
					new ResetOperation().execute("Reset");
				}
			}
			present_score = 0;
			SharedPreferences score_level = PreferenceManager.getDefaultSharedPreferences(this);
			Editor level_editor = score_level.edit();
			level = score_level.getInt("level", 1);
			level_editor.putInt("level", 1);
			level_editor.commit();
			score_view.setText("" + 0);
			txt_wordzilla.setText("");
			enter.setText("");
			Wordzilla_meaning_txt.setText("");
			time_rem.setText(""+25);
			life_txt.setText(""+ 5);
			time_rem.setTextColor(Color.rgb(7, 239, 7));
			score_high.close();
			score_in=10;
			i=1;
			wordzilla_txt = null;
		}
		catch(Exception e)
		{
			Log.e("Reset function error","",e);
		}
		finally
		{
			words.close();
		}
	}
	public void pause(View view)
	{	
		pauseTime();
	}
	void pauseTime()
	{
		TextView time_rem = (TextView) findViewById(R.id.timer);		
		if(pause_time<=5||pause_flag!=1)
		{
			if(has_user_entered_a_word==1)
			{
				if(pause_flag==1)
				{
					pause_time++;
					time_rem.setText("" + time_remain);
					count.cancel();
					pause_flag=0;
				}
				else
				{
					count.cancel();
					long time_re = time_remain*1000;
					count = new MyCount (time_re, interval);
					count.start();
					pause_flag=1;
				}
			}
			else if(has_user_entered_a_word!=1)
			{
				Toast.makeText(this, "Start the game to pause", Toast.LENGTH_SHORT).show();	
			}
		}
		else
		{
			Toast.makeText(this, "You can't pause. You have used up 5 pauses.", Toast.LENGTH_SHORT).show();	
		}
	}
	public class MyCount extends CountDownTimer
	{			
		int rem;
		TextView timer = (TextView) findViewById(R.id.timer);
		public MyCount(long start_in, long interval)
		{
			super(start_in, interval);
		}
		@Override
		public void onFinish()
		{
			reset=1;
			timer.setText("" + 25);
			reset();
		}
		@Override
		public void onTick(long Finish)
		{
			timer.setText("" + Finish/1000);
			time_remain = Finish/1000;
			String re_str = timer.getText().toString();
			if(time_remain<=12)
			{
				if((Finish/1000)%2==0)
					timer.setTextColor(Color.rgb(251, 16, 5));
				else
					timer.setTextColor(Color.rgb(250, 250, 250));
			}
			if(time_remain>12&&time_remain<20)
			{
				timer.setTextColor(Color.rgb(251, 237, 20));
			}
		}

	}
	public void clear(View view)
	{	
		EditText enter = (EditText) findViewById(R.id.input);
		enter.setText("");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Swarm.setInactive(this);
		SaveGame();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Swarm.setActive(this);
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		Swarm.setInactive(this);
		SaveGame();
	}

	public void share(View view){
		share();
	}

	public void SaveGame()
	{
		try
		{
			SQLiteDatabase words = openOrCreateDatabase("/"+Environment.getExternalStorageDirectory().getPath()+"/Wordzilla/testwords", MODE_PRIVATE, null);			
			count.cancel();
			TextView score_str = (TextView) findViewById(R.id.score);
			TextView life = (TextView) findViewById(R.id.life);
			TextView wordzilla = (TextView) findViewById(R.id.wordzilla);
			int life_remaining = Integer.parseInt(life.getText().toString());
			Log.i("Life inside saved ",""+life_remaining);
			int score_to_be_saved = Integer.parseInt(score_str.getText().toString());
			words.execSQL("update data set score = '"+score_to_be_saved+"';");
			words.execSQL("update data set player = '"+wordzilla.getText().toString()+"';");
			words.execSQL("update data set life = '"+life_remaining+"';");
			Log.i("Life inside saved after write ","life :"+life_remaining+" score :"+score_to_be_saved + " player" + wordzilla.getText().toString());
			words.close();	
		}
		catch(Exception e)
		{
			Log.e("Save Game", "Save Game error",e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		getSupportActionBar().setTitle("Wordzilla");
		getSupportActionBar().setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
		} else if (itemId == R.id.action_share) {
			share();
		} else if (itemId == R.id.action_clear) {
			EditText input = (EditText) findViewById(R.id.input);
			input.setText("");
		} else if (itemId == R.id.action_more) {
		} else if (itemId == R.id.action_reset) {
			new AlertDialog.Builder(this)
			.setTitle("Reset")
			.setMessage("Delete all your data, levels and score. Are you sure?")
			.setCancelable(true)
			.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					reset();
					dialog.dismiss();
				}
			})
			.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).show();
		} else if (itemId == R.id.action_fork) {
			Intent Git = new Intent(Intent.ACTION_VIEW);
			Git.setData(Uri.parse("https://www.github.com/tirkarthi/Wordzilla"));
			startActivity(Git);
		} 
		else if (itemId == R.id.action_score_submit) {
			SQLiteDatabase words = openOrCreateDatabase("/"+Environment.getExternalStorageDirectory().getPath()+"/Wordzilla/testwords", MODE_PRIVATE, null);			count.cancel();
			Cursor score = words.rawQuery("select * from high;", null);
			while(score.moveToNext()){
				int score_to_be_submitted = score.getInt(score.getColumnIndex("score"));
				Log.i("Score_submit","score to be submitted : "+ score_to_be_submitted);
				if(Swarm.isOnline()&&Swarm.isLoggedIn()){
					SwarmLeaderboard.submitScore(13268, score_to_be_submitted);
					Toast.makeText(getApplicationContext(), "Score : "+ score_to_be_submitted + " submitted.", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "Please log in and be online to submit.", Toast.LENGTH_LONG).show();
				}
			}
			score.close();
			words.close();
		}else if (itemId == R.id.action_rating) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.Yennaachi.Wordzilla"));
			startActivity(intent);
		} 
		else if(itemId == R.id.action_score){
			Swarm.showLeaderboards();
		}
		else {
		}
		return super.onOptionsItemSelected(item);
	}

	public void share()
	{
		TextView meaning = (TextView) findViewById(R.id.wordzilla_meaning);
		if(meaning.getText().toString().trim().length()!=0)
		{
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("text/plain");
			String sharebody = meaning.getText().toString()+" shared via #Wordzilla ";
			share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wordzilla");
			share.putExtra(android.content.Intent.EXTRA_TEXT, meaning.getText().toString()+sharebody);
			startActivity(Intent.createChooser(share, "Share word via"));
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Nothing to share", Toast.LENGTH_SHORT).show();
		}
	}

	public void reset(View view)
	{
		reset();
	}


	private class LongOperation extends AsyncTask<String, Void, String> {

		ProgressDialog pro = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			pro.show();
			pro.setMessage("Thinking");
		}

		@Override
		protected String doInBackground(String... params) {
			String meaning = "";
			String database = Environment.getExternalStorageDirectory().getPath().toString()+"/Wordzilla/testwords";
			try {
				EditText word = (EditText) findViewById(R.id.input);
				TextView wordzilla = (TextView) findViewById(R.id.wordzilla_meaning);
				String word_str = word.getText().toString();
				SQLiteDatabase words = openOrCreateDatabase(database, MODE_PRIVATE, null);
				char wordzilla_char = word_str.charAt(word_str.length()-1);
				Cursor c_wordzilla = words.rawQuery("select * from "+wordzilla_char+";",null);
				c_wordzilla.moveToFirst();
				int ran = c_wordzilla.getCount();
				int randomNum = (int)(Math.random()*ran) ;
				c_wordzilla.moveToPosition(randomNum);
				String wordzilla_str = c_wordzilla.getString(c_wordzilla.getColumnIndex("word"));
				c_wordzilla.close();
				Cursor c = words.rawQuery("select * from "+wordzilla_char+" where word = '"+ wordzilla_str+"';",null);
				c.moveToFirst();
				wordzilla_last = wordzilla_str.charAt(wordzilla_str.length()-1);
				meaning += "<b>"+wordzilla_str+"</b>";
				words.execSQL("UPDATE "+wordzilla_char+" SET id='"+2+"' WHERE word='"+wordzilla_str+"';");
				words.execSQL("update data set score = "+present_score+" ;");
				words.execSQL("update data set player = '"+wordzilla_name+"' ;");
				if(c!=null){
					if(c.getCount()==1){
						meaning += "<br><i>"+c.getString(c.getColumnIndex("part"))+"</i><br>"+"<b>"+c.getString(c.getColumnIndex("meaning"))+"</b><br>";
					}
					else
					{
						while(c.moveToNext()){
							meaning += "<br><i>"+c.getString(c.getColumnIndex("part"))+"</i><br>"+"<b>"+c.getString(c.getColumnIndex("meaning"))+"</b><br>";
						}	
					}
					c.close();
					words.close();
					Log.e("Word Activity", "Ended");
				}
			} catch (Exception e) {
				Log.e("LongOperation", "Interrupted", e);
				return "Interrupted";
			}
			return meaning;
		}

		protected void onPostExecute(String meaning) {
			pro.dismiss();
			TextView wordzilla = (TextView) findViewById(R.id.wordzilla);
			wordzilla.setText(Html.fromHtml(wordzilla_name+"<br><br>"));
			EditText enter = (EditText) findViewById(R.id.input);
			enter.setText("");
			TextView wordzilla_meaning = (TextView) findViewById(R.id.wordzilla_meaning);
			wordzilla_meaning.setText(Html.fromHtml(meaning));
			count = new MyCount (start, interval);
			count.start();
		}
	}


	private class ResetOperation extends AsyncTask<String, Void, String> {

		ProgressDialog pro = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			pro.show();
			pro.setMessage("Resetting. Just a few seconds");
			pro.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... params) {
			SQLiteDatabase reset = openOrCreateDatabase(database, MODE_PRIVATE, null);
			char a= 'a';
			for(int i=0;i<26;i++,a++)
			{
				try
				{
					reset.execSQL("UPDATE "+ a +" SET id="+0+" WHERE id="+1+"");
					reset.execSQL("UPDATE "+ a +" SET id="+0+" WHERE id="+2+"");
				}
				catch(Exception e)
				{
					Log.e("ResetOperation", "Reset error", e);
					Toast.makeText(getApplicationContext(), "Reset error", Toast.LENGTH_SHORT).show();
				}
			}
			reset.close();
			return "Reset End";
		}

		protected void onPostExecute(String meaning) {
			pro.dismiss();
			Toast.makeText(getApplicationContext(), "Successful reset.", Toast.LENGTH_SHORT).show();
		}
	}

	private class PreLoadOperation extends AsyncTask<String, Void, String> {

		ProgressDialog pro = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			pro.show();
			pro.setMessage("Setting up values. Just a moment");
			pro.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... params) {

			try
			{
				Swarm.preload(activity, ID, "Secret_key");
				Swarm.setAllowGuests(true);
				Swarm.enableAlternativeMarketCompatability();
				Swarm.init(activity, ID, "Secret_key");
				Swarm.setActive(activity);
			}
			catch(Exception e)
			{
				Log.e("PreloadOperation", "Reset error", e);
				Toast.makeText(getApplicationContext(), "Preload error", Toast.LENGTH_SHORT).show();
			}
			return "Success";
		}

		protected void onPostExecute(String meaning) {
			pro.dismiss();
		}
	}

}
