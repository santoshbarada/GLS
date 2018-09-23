/**
 * 
 */
package com.abcib.gls.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.exception.GLSException;

/**
 * @author santosh.barada
 *
 */
public class GLSUtility {
	
	private final static Log mLog = LogFactory.getLog(GLSUtility.class);

	private static HashMap<String, Properties> propertiesCache = new HashMap<String, Properties>();
	
	private static Properties loadProperties(String propFileName) throws GLSException {

	  if (mLog.isDebugEnabled()) {
		  mLog.debug("Entering  loadProperties() for property file: "+propFileName);	
	  }
	
	  Properties props = new Properties();
	  try {
	
		  ResourceBundle bundle=PropertyResourceBundle.getBundle(propFileName);
		  if(bundle!=null){
			  Enumeration<String> keys = bundle.getKeys();
			  while (keys.hasMoreElements()) {
				  String key = keys.nextElement();
				  props.put(key, bundle.getString(key));
			  } 
		  }else{
			  System.out.println("Got a fatal Exception while Loading the Properties File"+ propFileName);
		  }
		  
	
	  }
	  catch (Exception e) {
		  mLog.fatal("Got a fatal Exception while Loading the Properties File " + propFileName, e);
		  throw new GLSException(GLSConstants.GLS_ERR_CODE_10001," Error While Loading the Property File " + propFileName,e);
	  }
	  
	  if(mLog.isDebugEnabled()){
			mLog.debug("Leaving  loadProperties()");	
	  }
	  return props;
	}
	
	public static Properties getProperty(String propFileName) throws GLSException{
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering  getProperty() for property file: "+propFileName);
		}
		Properties props = (Properties) propertiesCache.get(propFileName);
	    try
	    {
	    if (props == null) {
	      synchronized (propertiesCache) {
	        props = loadProperties(propFileName);
	        propertiesCache.put(propFileName, props);
	      }
	    }else{
	    	if(mLog.isDebugEnabled()){
				mLog.debug("Property file loaded from Cache");	
			}
	    }
	    }catch (Exception e){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001,"Property file can not be loded:"+propFileName,e);
		}
	    
	    if(mLog.isDebugEnabled()){
			mLog.debug("Leaving  getProperty()");	
		}
	    
	    return props;
	}

	public static InputStream loadInputStream(String fileName) throws GLSException {
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering  loadInputStream() for file: "+fileName);	
		}
		
		InputStream inputStream = null;
		File file=null;
		
		try{
			file = new File(fileName);
			inputStream = new FileInputStream(file);
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001,"File '" + fileName + "' not found in the classpath",ex);
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving  loadInputStream()");
		}
		return inputStream;
	}

	public static void close(InputStream ins){
		try {
	      if (ins != null) {
	    	  ins.close();
	      }
	    }catch (IOException e) {
			mLog.fatal("Exception Occured while closing InputStream: ",e);
		}
	}

}
