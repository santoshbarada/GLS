/**
 * 
 */
package com.abcib.gls.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;

/**
 * @author santosh.barada
 *
 */
public class TransactionValidator {
	
	private final static Log mLog = LogFactory.getLog(TransactionValidator.class);
	
	public static boolean validateOnlineTransactions(TransactionDO transactionDO, String feedType) {
		// TODO Auto-generated method stub
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Online Transaction Validation completed successfully.");
		}
		return true;
	}
	
	public static boolean validateSODTransactions(OpeningPositionDO sodPosDO, String feedType) {
		// TODO Auto-generated method stub
		
		if(mLog.isDebugEnabled()){
			mLog.debug("SOD Transaction Validation completed successfully.");
		}
		return true;
	}

}
