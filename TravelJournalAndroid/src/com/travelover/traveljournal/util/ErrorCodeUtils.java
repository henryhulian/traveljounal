package com.travelover.traveljournal.util;

import android.content.Context;

public class ErrorCodeUtils {
	
	public static final int SUCCESS=0;
	
	/**
	 * ����������־ʧ��
	 * */
	public static final int CREATE_JOURNAL_FAILED=-1000;
	
	/**
	 * ����������־�ڵ�ʧ��
	 * */
	public static final int CREATE_JOURNAL_NODE_FAILED=-1001;
	
	/**
	 * ɾ��������־ʧ��
	 * */
	public static final int DELETE_JOURNAL_FAILED=-1002;
	
	/**
	 * ��־������
	 */
	public static final int JOURNAL_NOT_EXISTED = -1003;

	/**
	 * �������ݿ�������ʧ��
	 * */
	public static final int CREATE_COUCHBASE_MANAGER_FAILED = -2000;

	/**
	 * �������ݿ�ʧ��
	 */
	public static final int CREATE_JOURNAL_DATABASE_FAILED = -2001;

	/**
	 * URL��ʽ����
	 */
	public static final int URL_FORMATE_ERROR = -2002;

	
	public static void alertErrorMessage( int code , Context context){
		switch (code) {
		
		case SUCCESS:
			AlertUtils.alert("SUCCESS!", context);
			break;
		
		case CREATE_JOURNAL_FAILED:
			AlertUtils.alert("CREATE_JOURNAL_FAILED!", context);
			break;
		
		case CREATE_JOURNAL_NODE_FAILED:
			AlertUtils.alert("CREATE_JOURNAL_NODE_FAILED!", context);
			break;
		
		case DELETE_JOURNAL_FAILED:
			AlertUtils.alert("DELETE_JOURNAL_FAILED!", context);
			break;
			
		case JOURNAL_NOT_EXISTED:
			AlertUtils.alert("JOURNAL_NOT_EXISTED!", context);
			break;
			
		case CREATE_COUCHBASE_MANAGER_FAILED:
			AlertUtils.alert("CREATE_COUCHBASE_MANAGER_FAILED!", context);
			break;
			
		case CREATE_JOURNAL_DATABASE_FAILED:
			AlertUtils.alert("CREATE_JOURNAL_DATABASE_FAILED!", context);
			break;
			
		case URL_FORMATE_ERROR:
			AlertUtils.alert("URL_FORMATE_ERROR!", context);
			break;
			
		default:
			AlertUtils.alert("UNKNOWN_ERROR! CODE:"+code, context);
			break;
		}
	}
	
}
