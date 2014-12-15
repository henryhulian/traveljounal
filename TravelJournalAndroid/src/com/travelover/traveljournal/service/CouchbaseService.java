package com.travelover.traveljournal.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.couchbase.lite.Context;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.BasicAuthenticator;
import com.couchbase.lite.replicator.Replication;

import android.app.Activity;
import android.util.Log;

public class CouchbaseService {

	public static final String JOURNAL_DB_NAME = "journals";

	private Manager manager;
	private Database journalsDB;
	private Replication journalsDBPush;
	private Replication journalsDBPull;
	private final String TAG = "CouchbaseService";
	
	private String journalName;

	public CouchbaseService(Activity activity) {
		try {

			/* 创建数据库管理对象 */
			manager = new Manager((Context) new AndroidContext(activity),
					Manager.DEFAULT_OPTIONS);
			Log.d(TAG, "Manager created!");

			/* 检查创建数据库对象 */
			journalsDB = manager.getDatabase(JOURNAL_DB_NAME);

			URL url = new URL("http://120.24.225.199:4984/journals/");
			Replication push = journalsDB.createPushReplication(url);
			Replication pull = journalsDB.createPullReplication(url);
			pull.setContinuous(true);
			push.setContinuous(true);
			Authenticator auth = new BasicAuthenticator("test001", "111111");
			push.setAuthenticator(auth);
			pull.setAuthenticator(auth);

			push.addChangeListener(new Replication.ChangeListener() {
				@Override
				public void changed(Replication.ChangeEvent event) {
					// will be called back when the push replication status
					// changes
					Log.d(TAG, "change count:" + event.getChangeCount());
				}
			});
			pull.addChangeListener(new Replication.ChangeListener() {
				@Override
				public void changed(Replication.ChangeEvent event) {
					// will be called back when the pull replication status
					// changes
					Log.d(TAG, "change count:" + event.getChangeCount());
				}
			});
			push.start();
			pull.start();
			journalsDBPush = push;
			journalsDBPull = pull;

		} catch (IOException e) {
			Log.e(TAG, "Cannot create manager object");
			throw new RuntimeException();
		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "Cannot create manager object");
			throw new RuntimeException();
		}
	}

	public String createJournal() {
		Document document = this.journalsDB.getDocument(this.journalName);
		Log.d(TAG, "create journal:" + document.getId());
		return document.getId();
	}

	public void recordNode() {

		Document journal = this.journalsDB.getDocument(this.journalName);

		if (journal == null) {
			Log.e(TAG, "can not find journal:" + this.journalName);
			return;
		}

		// update the document
		Map<String, Object> updatedProperties = new HashMap<String, Object>();
		updatedProperties.putAll(journal.getProperties());

		@SuppressWarnings("unchecked")
		List<String> locationList = (List<String>) updatedProperties
				.get("locationlist");
		if (locationList == null) {
			locationList = new ArrayList<String>();
		}
		locationList.add(String.valueOf(System.currentTimeMillis()));
		updatedProperties.put("locationlist", locationList);

		try {
			journal.putProperties(updatedProperties);
			Log.d(TAG,
					"updated retrievedDocument="
							+ String.valueOf(journal.getProperties()));
		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "Cannot update document", e);
		}
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public Database getJournalsDB() {
		return journalsDB;
	}

	public void setJournalsDB(Database journalsDB) {
		this.journalsDB = journalsDB;
	}

	public Replication getJournalsDBPush() {
		return journalsDBPush;
	}

	public void setJournalsDBPush(Replication journalsDBPush) {
		this.journalsDBPush = journalsDBPush;
	}

	public Replication getJournalsDBPull() {
		return journalsDBPull;
	}

	public void setJournalsDBPull(Replication journalsDBPull) {
		this.journalsDBPull = journalsDBPull;
	}

	public String getJournalName() {
		return journalName;
	}

	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}

}
