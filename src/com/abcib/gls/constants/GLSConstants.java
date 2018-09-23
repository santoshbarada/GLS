/**
 * 
 */
package com.abcib.gls.constants;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.exception.GLSException;
import com.abcib.gls.util.GLSUtility;

/**
 * @author santosh.barada
 *
 */
public class GLSConstants {
	
	public static Properties glsProps = null;
	private final static Log mLog = LogFactory.getLog(GLSConstants.class);

    static {
		try {
			glsProps = GLSUtility.getProperty("glsconfig");
		} catch (GLSException ex) {
			mLog.fatal(" Error While Loading the Property File" + "glsconfig.properties");
		}
    }
    
	public static final String FEED_IN_REQUEST_PATH="/feed/in/request";
	public static final String FEED_IN_ACCEPT_PATH="/feed/in/accept";
	public static final String FEED_IN_REJECT_PATH="/feed/in/reject";
	public static final String FEED_OUT_RESPONSE_PATH="/feed/out/response";
	public static final String FEED_OUT_ARCHIVE_PATH="/feed/out/archive";
	public static final String XLS_DB_EOD_FILE_PATH="/exceldb/gls_eod_excel_db.xlsx";
	public static final String XLS_DB_SOD_FILE_PATH="/exceldb/gls_sod_excel_db.xlsx";
	public static final String XLS_DB_TXN_FILE_PATH="/exceldb/gls_txn_excel_db.xlsx";
	public static final String FEED_TYPE_POS="POS";
	public static final String FEED_TYPE_TXN="TXN";
	public static final String EMPTY_STRING="";
	public static final String ACCOUNT_TYPE_E="E";
	public static final String ACCOUNT_TYPE_I="I";
	public static final String TRANSACTION_TYPE_B="B";
	public static final String TRANSACTION_TYPE_S="S";
	
	public static final String GLS_ERR_CODE_10001="GLS10001";
	public static final String GLS_ERR_CODE_10002="GLS10002";
	public static final String GLS_ERR_CODE_10003="GLS10003";
	public static final String GLS_ERR_CODE_10004="GLS10004";
	public static final String GLS_ERR_CODE_10005="GLS10005";
	
	public static final String APPS_HOME_PATH=glsProps.getProperty("APPS_HOME_PATH")!=null?(String)glsProps.getProperty("APPS_HOME_PATH"):".";
	
}
