package com.travelover.traveljournal;

import com.travelover.traveljournal.service.CouchbaseService;
import com.travelover.traveljournalandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	
	private CouchbaseService couchbaseService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		couchbaseService=new CouchbaseService(this);
		
		setContentView(R.layout.mainview);
		
		Button createJournal = (Button) findViewById(R.id.createJournal);
		createJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				couchbaseService.setJournalName("journal1");
				couchbaseService.createJournal();
			}
		});
		
		Button recordJournalNode = (Button) findViewById(R.id.recordJournalNode);
		recordJournalNode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				couchbaseService.recordNode();
			}
		});
	}

}
