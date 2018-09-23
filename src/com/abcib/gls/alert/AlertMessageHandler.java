/**
 * 
 */
package com.abcib.gls.alert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author santosh.barada
 *
 */
public class AlertMessageHandler {
	
	private final static Log mLog = LogFactory.getLog(AlertMessageHandler.class);

	public static void logAlertMessage(String alertMessage) {
		mLog.info(alertMessage);
	}

}
