package com.travelover.traveljournal.service;

import java.io.IOException;
import java.net.URL;

import com.couchbase.lite.Context;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.BasicAuthenticator;
import com.couchbase.lite.replicator.Replication;

import android.app.Activity;
import android.util.Log;


public class CouchbaseService {
	
	public static final String JOURNAL_DB_NAME="journals";

	private static Manager manager;
	private static Database journalsDB;
	private static Replication journalsDBPush;
	private static Replication journalsDBPull;
	private static final String TAG="CouchbaseService";
	
	
	private CouchbaseService() {
	}
	
	public static void init( Activity activity ){
		try {
			
			/*创建数据库管理对象*/
			manager = new Manager((Context) new AndroidContext(activity), Manager.DEFAULT_OPTIONS);
			Log.d (TAG, "Manager created!");
			
			/*检查创建数据库对象*/
			journalsDB=manager.getDatabase(JOURNAL_DB_NAME);
			
			URL url = new URL("http://www.en2012.com:4984/journals/");
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
			        // will be called back when the push replication status changes
			    }
			});
			pull.addChangeListener(new Replication.ChangeListener() {
			    @Override
			    public void changed(Replication.ChangeEvent event) {
			        // will be called back when the pull replication status changes
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

	public static Manager getManager() {
		return manager;
	}

	public static void setManager(Manager manager) {
		CouchbaseService.manager = manager;
	}

	public static Database getJournalsDB() {
		return journalsDB;
	}

	public static void setJournalsDB(Database journalsDB) {
		CouchbaseService.journalsDB = journalsDB;
	}

	public static Replication getJournalsDBPush() {
		return journalsDBPush;
	}

	public static void setJournalsDBPush(Replication journalsDBPush) {
		CouchbaseService.journalsDBPush = journalsDBPush;
	}

	public static Replication getJournalsDBPull() {
		return journalsDBPull;
	}

	public static void setJournalsDBPull(Replication journalsDBPull) {
		CouchbaseService.journalsDBPull = journalsDBPull;
	}

}
