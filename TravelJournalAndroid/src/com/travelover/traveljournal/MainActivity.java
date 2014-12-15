package com.travelover.traveljournal;

import com.travelover.traveljournal.exceptions.CreateJournalFailedException;
import com.travelover.traveljournal.exceptions.DeleteJournalFailedException;
import com.travelover.traveljournal.exceptions.JournalNotExistedException;
import com.travelover.traveljournal.service.CouchbaseService;
import com.travelover.traveljournal.util.AlertUtils;
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
		
		/*创建旅行日志*/
		Button createJournal = (Button) findViewById(R.id.createJournal);
		createJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				couchbaseService.setJournalName("journal1");
				try {
					couchbaseService.createJournal();
				} catch (CreateJournalFailedException e) {
					AlertUtils.alert("create journal failed!", v.getContext());
				}
			}
		});
		
		/*记录旅行日志节点*/
		Button recordJournalNode = (Button) findViewById(R.id.recordJournalNode);
		recordJournalNode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					couchbaseService.recordNode();
				} catch (JournalNotExistedException e) {
					AlertUtils.alert("journal not found!", v.getContext());
				}
			}
		});
		
		Button deleteJournal = (Button) findViewById(R.id.deleteJournal);
		deleteJournal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					couchbaseService.deleteJournal();
				} catch ( DeleteJournalFailedException e) {
		            AlertUtils.alert("delete journal failed!", v.getContext());
				} catch (JournalNotExistedException e) {
					 AlertUtils.alert("journal not found!", v.getContext());
				}
			}
		});
	}

	
}
