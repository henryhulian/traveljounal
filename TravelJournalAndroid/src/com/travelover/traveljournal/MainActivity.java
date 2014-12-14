package com.travelover.traveljournal;

import com.travelover.traveljournal.service.CouchbaseService;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CouchbaseService.init(this);
		
	}

}
