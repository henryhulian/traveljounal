package com.travelover.traveljournal.service;

import java.io.IOException;
import java.net.MalformedURLException;
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
import com.travelover.traveljournal.model.Node;
import com.travelover.traveljournal.util.ErrorCodeUtils;

import android.location.Location;
import android.util.Log;

public class CouchbaseService {

	public static final String JOURNAL_DB_NAME = "journals";

	private Manager manager;
	private Database journalsDB;
	private Replication journalsDBPush;
	private Replication journalsDBPull;
	private final String TAG = "CouchbaseService";

	private String journalName;

	public int init(android.content.Context context) {

		int code = ErrorCodeUtils.SUCCESS;

		try {
			manager = new Manager((Context) new AndroidContext(context),
					Manager.DEFAULT_OPTIONS);
		} catch (IOException e) {
			Log.e(TAG, "error:"
					+ ErrorCodeUtils.CREATE_COUCHBASE_MANAGER_FAILED);
			code = ErrorCodeUtils.CREATE_COUCHBASE_MANAGER_FAILED;
		}
		Log.d(TAG, "Manager created!");

		try {
			journalsDB = manager.getDatabase(JOURNAL_DB_NAME);
		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "error:" + ErrorCodeUtils.CREATE_JOURNAL_DATABASE_FAILED);
			code = ErrorCodeUtils.CREATE_JOURNAL_DATABASE_FAILED;
		}

		URL url = null;
		try {
			url = new URL("http://120.24.225.199:4984/journals/");
		} catch (MalformedURLException e) {
			Log.e(TAG, "error:" + ErrorCodeUtils.URL_FORMATE_ERROR);
			code = ErrorCodeUtils.URL_FORMATE_ERROR;
		}

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
				// will be called back when the journalsDBPush replication
				// status
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

		return code;

	}

	public int createJournal() {

		int code = ErrorCodeUtils.SUCCESS;

		try {
			Document journal = this.journalsDB.getDocument(this.journalName);

			Map<String, Object> updatedProperties = new HashMap<String, Object>();
			updatedProperties.put("username", "test001");
			updatedProperties.put("createTime",
					new Timestamp(System.currentTimeMillis()));
			journal.putProperties(updatedProperties);

			Log.d(TAG, "create journal:" + journal.getId());
		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "error:" + ErrorCodeUtils.CREATE_JOURNAL_FAILED);
			code = ErrorCodeUtils.CREATE_JOURNAL_FAILED;
		}

		return code;
	}

	public int recordNode(Location location) {

		int code = ErrorCodeUtils.SUCCESS;

		Document journal = this.journalsDB
				.getExistingDocument(this.journalName);

		if (journal == null || journal.isDeleted()) {
			Log.e(TAG, "error:" + ErrorCodeUtils.JOURNAL_NOT_EXISTED);
			code = ErrorCodeUtils.JOURNAL_NOT_EXISTED;
			return code;
		}

		// update the document
		Map<String, Object> updatedProperties = new HashMap<String, Object>();
		Map<String, Object> orignalProperties = journal.getProperties();
		if (orignalProperties != null) {
			updatedProperties.putAll(orignalProperties);
		}

		@SuppressWarnings("unchecked")
		List<Node> nodeList = (List<Node>) updatedProperties.get("nodes");
		if (nodeList == null) {
			nodeList = new ArrayList<Node>();
		}

		Node node = new Node();
		node.setLangtitude(location.getLongitude());
		node.setLatitude(location.getLatitude());
		node.setAltitude(location.getAltitude());
		node.setTime(location.getTime());

		nodeList.add(node);
		updatedProperties.put("nodes", nodeList);

		try {
			journal.putProperties(updatedProperties);
			Log.d(TAG,
					"updated retrievedDocument="
							+ String.valueOf(journal.getCurrentRevision()));
		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "error:" + ErrorCodeUtils.CREATE_JOURNAL_NODE_FAILED);
			code = ErrorCodeUtils.CREATE_JOURNAL_NODE_FAILED;
		}

		return code;
	}

	public Document  findJournal(String journalName) {

		Document journal = this.journalsDB
				.getExistingDocument(this.journalName);

		if (journal == null || journal.isDeleted()) {
			Log.e(TAG, "error:" + ErrorCodeUtils.JOURNAL_NOT_EXISTED);
			return null;
		}

		return journal;
	}

	public int deleteJournal() {

		int code = ErrorCodeUtils.SUCCESS;

		try {
			Document journal = this.journalsDB
					.getExistingDocument(this.journalName);

			if (journal == null || journal.isDeleted()) {
				Log.e(TAG, "error:" + ErrorCodeUtils.JOURNAL_NOT_EXISTED);
				code = ErrorCodeUtils.JOURNAL_NOT_EXISTED;
				return code;
			}

			Log.d(TAG, "delete journal:" + journal.getId());
			journal.delete();

		} catch (CouchbaseLiteException e) {
			Log.e(TAG, "error:" + ErrorCodeUtils.DELETE_JOURNAL_FAILED);
			code = ErrorCodeUtils.DELETE_JOURNAL_FAILED;
		}

		return code;
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
