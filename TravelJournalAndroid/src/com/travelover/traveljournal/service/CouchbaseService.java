package com.travelover.traveljournal.service;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
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
import com.travelover.traveljournal.exceptions.CreateJournalFailedException;
import com.travelover.traveljournal.exceptions.DeleteJournalFailedException;
import com.travelover.traveljournal.exceptions.JournalNotExistedException;

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
			journalsDBPush = journalsDB.createPushReplication(url);
			journalsDBPull = journalsDB.createPullReplication(url);
			
			journalsDBPull.setContinuous(true);
			journalsDBPush.setContinuous(true);
			
			Authenticator auth = new BasicAuthenticator("test001", "111111");
			journalsDBPush.setAuthenticator(auth);
			journalsDBPull.setAuthenticator(auth);

			journalsDBPush.addChangeListener(new Replication.ChangeListener() {
				@Override
				public void changed(Replication.ChangeEvent event) {
					// will be called back when the journalsDBPush replication status
					// changes
					Log.d(TAG, "change count:" + event.getChangeCount());
				}
			});
			journalsDBPull.addChangeListener(new Replication.ChangeListener() {
				@Override
				public void changed(Replication.ChangeEvent event) {
					// will be called back when the pull replication status
					// changes
					Log.d(TAG, "change count:" + event.getChangeCount());
				}
			});
			
			journalsDBPush.start();
			journalsDBPull.start();

		} catch (IOException e) {
			Log.e(TAG, "Cannot create manager object");
			throw new RuntimeException();
		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "Cannot create manager object");
			throw new RuntimeException();
		}
	}

	public void createJournal() throws CreateJournalFailedException {
		Document journal;
		try {
			journal = this.journalsDB.getDocument(this.journalName);
			Map<String, Object> updatedProperties = new HashMap<String, Object>();
			updatedProperties.put("username", "test001");
			updatedProperties.put("createTime", new Timestamp(System.currentTimeMillis()));
			journal.putProperties(updatedProperties);
			Log.d(TAG, "create journal:" + journal.getId());
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
			throw new CreateJournalFailedException();
		}
	}

	public void recordNode() throws JournalNotExistedException {

		Document journal = this.journalsDB.getDocument(this.journalName);

		if (journal == null || journal.isDeleted()) {
			Log.e(TAG, "can not find journal:" + this.journalName);
			throw new JournalNotExistedException();
		}

		// update the document
		Map<String, Object> updatedProperties = new HashMap<String, Object>();
		Map<String, Object> orignalProperties = journal.getProperties();
		if( orignalProperties!=null ){
			updatedProperties.putAll(orignalProperties);
		}

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
	
	public void deleteJournal() throws DeleteJournalFailedException, JournalNotExistedException {
		try {
			Document journal = this.journalsDB.getDocument(this.journalName);
			
			if(journal==null){
				throw new JournalNotExistedException();
			}
			
			
			if(journal.isDeleted()){
				throw new JournalNotExistedException();
			}
			
			Log.d(TAG, "delete journal:" + journal.getId());
			journal.delete();
		
		} catch (CouchbaseLiteException e) {
			throw new DeleteJournalFailedException();
		}
		return;
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
