/**
 * 
 */
package com.abcib.gls.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author santosh.barada
 * 
 * GLS10001 - Exception for IO Operations
 * GLS10002 - Application Specific Exception
 * GLS10003 - Application Specific Txn Validation Exception
 * GLS10004 - Application Specific Feed Validation Exception
 * GLS10005 - Application Specific Processing Exception
 *
 */
public class GLSException extends Exception{
	
	private final static Log mLog = LogFactory.getLog(GLSException.class);

	public GLSException(String errorMsg){
	    super("GLS10002"+" : "+errorMsg);
	    mLog.fatal("GLSException Occurred: "+errorMsg);
	}
	
	public GLSException(Exception exception, String errorMsg){
		super("GLS10002"+" : "+errorMsg);
		mLog.fatal("GLSException Occurred: "+errorMsg,exception);
	}

	public GLSException(String errorCode, String miscMsg, Exception exception) {
		super(errorCode + " : " + miscMsg);
		mLog.fatal("GLSException: "+errorCode+" : "+miscMsg,exception);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

}
