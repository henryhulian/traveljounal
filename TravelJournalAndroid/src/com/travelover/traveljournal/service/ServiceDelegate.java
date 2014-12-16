package com.travelover.traveljournal.service;

public class ServiceDelegate {

	private static CouchbaseService couchbaseService;
	
	public static void init(){
		couchbaseService = new CouchbaseService();
	}

	public static CouchbaseService getCouchbaseService() {
		return couchbaseService;
	}

	public static void setCouchbaseService(CouchbaseService couchbaseService) {
		ServiceDelegate.couchbaseService = couchbaseService;
	}
	
	
}
