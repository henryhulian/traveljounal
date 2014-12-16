package com.travelover.traveljournal;

import com.couchbase.lite.Document;
import com.travelover.traveljournal.service.ServiceDelegate;
import com.travelover.traveljournal.util.ErrorCodeUtils;
import com.travelover.traveljournalandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mainview);
		
		ServiceDelegate.init(this);
		
		Button createJournal = (Button) findViewById(R.id.createJournal);
		createJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ServiceDelegate.getCouchbaseService().setJournalName("journal1");
					int code=ServiceDelegate.getCouchbaseService().createJournal();
					ErrorCodeUtils.alertErrorMessage(code, v.getContext());
			}
		});
		
		Button recordJournalNode = (Button) findViewById(R.id.recordJournalNode);
		recordJournalNode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					//int code=ServiceDelegate.getCouchbaseService().recordNode();
					//ErrorCodeUtils.alertErrorMessage(code, v.getContext());
			}
		});
		
		Button deleteJournal = (Button) findViewById(R.id.deleteJournal);
		deleteJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					
					int code = ServiceDelegate.getCouchbaseService().deleteJournal();
					ErrorCodeUtils.alertErrorMessage(code, v.getContext());
			}
		});
		
		Button findJournal = (Button) findViewById(R.id.findJournal);
		findJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				    
					Document journalFound = ServiceDelegate.getCouchbaseService().findJournal("jounal1");
					
					TextView journalView = (TextView) findViewById(R.id.journalview);
					journalView.setMovementMethod(new ScrollingMovementMethod());
					journalView.setText(journalFound.getProperties().toString());
			}
		});
	}

	
}
