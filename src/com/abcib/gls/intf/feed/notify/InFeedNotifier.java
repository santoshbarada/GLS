/**
 * 
 */
package com.abcib.gls.intf.feed.notify;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.alert.AlertMessageHandler;
import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.handler.FeedNotificationHandler;

/**
 * @author santosh.barada
 *
 */
public class InFeedNotifier {
	
	private final static Log mLog = LogFactory.getLog(InFeedNotifier.class);
	private static List<String> fileNameArr=new ArrayList<String>();
	
	    public static void runTask() {
	    	try{
    			File folder = new File(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_IN_REQUEST_PATH);
    			File[] listOfFiles = folder.listFiles();
    			
    			for (int i = 0; i < listOfFiles.length; i++) {
    			  if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")){
    				  if(!fileNameArr.contains(listOfFiles[i].getName()+listOfFiles[i].lastModified())){
    					  AlertMessageHandler.logAlertMessage(listOfFiles[i].getName()+" file picked for processing.");
    					  mLog.fatal(listOfFiles[i].getName()+" file picked for processing.");
    					  GregorianCalendar cal = new GregorianCalendar();
    					  SimpleDateFormat dtf = new SimpleDateFormat("ddMMyyyy");
    					  String dt=dtf.format(cal.getTime().getTime());
    					  FeedNotificationHandler feedHandler=new FeedNotificationHandler(listOfFiles[i]);
        				  Thread thread = new Thread(feedHandler);
        				  thread.setName("GLS"+thread.getName()+dt);
        				  thread.start();
    				  }
    				  fileNameArr.add(listOfFiles[i].getName()+listOfFiles[i].lastModified());
    			  }
    			}
    		}catch(Exception ex){
    			try {
    				throw new GLSException(GLSConstants.GLS_ERR_CODE_10002,"Application Specific Error Occurred In InFeedNotifier",ex);
    			} catch (GLSException e) {
    				mLog.fatal("GLSException: "+"Application Specific Error Occurred In InFeedNotifier While running File Picker Thread");
    			}
    		}
	    }
}
