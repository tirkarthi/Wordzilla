package com.Yennaachi.Wordzilla;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class About extends ActionBarActivity {

	
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
		setContentView(R.layout.activity_about);
		TextView about = (TextView) findViewById(R.id.aboutus);
		about.setText(Html.fromHtml(
						"<br><b>Version		:</b> 2.0<br>" +
						"<b>Developer	:</b> Karthikeyan S<br>"+
						"<b>Mail </b>@ tir.karthi@gmail.com<br>" +
						"<b>Fork </b>@ Github<br>" +
						"https://github.com/tirkarthi/Wordzilla<br>"+
						"<b>Tweet </b> @tirkarthi <br>"+
						"<b>Facebook </b>: www.facebook.com/Yennaachi <br><br>" +
						""+
						"<b>Credits:</b><br>"+
						"Nandheeswaran<br>"+
						"Thilak<br>"+
						"Dhanabalan<br>"+
						"Dinesh Kumar<br>"+
						"Ram Kumar<br><br>" +
						""+ 
						"<b>Thank you :</b><br>" +
						"<b>Wikitionary</b> - Wordzilla uses content provided by wikitionary.<br>" +
						"<b>Perl</b> - For its excellent string parsing and manipulation.<br>" +
						"<b>SQLite Manager for Firefox</b> - Used for database operations.<br><br>" +
						"" +
						"<b>Libraries used :</b><br> " +
						"android-support-v7-appcompat for Action Bar<br>" +
						"SwarmConnect for LeaderBoards<br>" +
						"GoogleAnalytics for crash reports and exception logs.<br>" +
						"We don't use any of your personal information or store them.<br><br>" +
						"" +
						"<b>Permissions :</b><br>" +
						"Internet and Network state :<br> They are required to send scores and error reports.<br>" +
						"Read External Storage : <br>To check for the words and to provide definitions.<br>" +
						"Write External Storage : <br>To save your scores and to reset database flags.<br><br>" +
						"" +
						"<b>Background Image License : </b><br>" +
						"The image was obtained from https://commons.wikimedia.org/wiki/File:Antennae_galaxies_xl.jpg<br>" +
						"This file is in the public domain because it was created by NASA and ESA. " +
						"NASA Hubble material (and ESA Hubble material prior to 2009) is copyright-free " +
						"and may be freely used as in the public domain without fee, on the condition that " +
						"only NASA, STScI, and/or ESA is credited as the source of the material. " +
						"This license does not apply if ESA material created after 2008 or source material " +
						"from other organizations is in use."+
						"The material was created for NASA by Space Telescope Science Institute under Contract NAS5-26555, " +
						"or for ESA by the Hubble European Space Agency Information Centre. " +
						"Copyright statement at hubblesite.org or 2008 copyright statement at spacetelescope.org. " +
						"For material created by the European Space Agency on the spacetelescope.org site since 2009, " +
						"use the {{ESA-Hubble}} tag.<br><br>"+
						"" +
						"<b>Copying:</b><br>" +
						"	 The copies of the software should have attribution to the original developer and me from whom the software was modified and the content used from the app" +
						"should have attribution and the content still holds the respective license from which they were copied." +
						"or substantial copy is made.<br><br>" +
						"    The wikitionary content is available under the Creative Commons Attribution-ShareAlike License;<br>" +
						"	 For License see http://creativecommons.org/licenses/by-sa/3.0/ <br><br>"+
						"" +
						"<br>Wordzilla is free software: you can redistribute it and/or modify"+
						"    it under the terms of the GNU General Public License as published by"+
						"    the Free Software Foundation, either version 3 of the License, or"+
						"    (at your option) any later version.<br>"+
						"<br>"+
						"    Wordzilla is distributed in the hope that it will be useful,"+
						"    but WITHOUT ANY WARRANTY; without even the implied warranty of"+
						"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the"+
						"    GNU General Public License for more details.<br>"+
						"<br>"+
						"    You should have received a copy of the GNU General Public License"+
						"    along with this program.  If not, see http://www.gnu.org/licenses/"+
						"<br><br>"+
						"" +
						"<b>Disclaimer :</b><br>" +
						"Wordzilla <b>should not be confused</b> with Mozilla, FileZilla or any other products and services." +
						"Wordzilla is just a hobby project inspired by the open source " +
						"nature and mission of Mozilla and Wikipedia. Any such resemblance is purely coincidental."));
	}
	public void GPL(View view)
	{
		Intent con = new Intent(this, License.class);
		startActivity(con);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		getSupportActionBar().setTitle("About");
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
		} else if (itemId == R.id.action_fork) {
			Intent Git = new Intent(Intent.ACTION_VIEW);
			Git.setData(Uri.parse("https://www.github.com/tirkarthi/Wordzilla"));
			startActivity(Git);
		} else if (itemId == R.id.action_rating) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.Yennaachi.Wordzilla"));
			startActivity(intent);
		} else {
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
