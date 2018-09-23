/**
 * 
 */
package com.abcib.gls.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.dao.GLSExceldbDao;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;

/**
 * @author santosh.barada
 *
 */
public class SODTxnProcessorImpl implements TransactionProcessor {
	
	private final static Log mLog = LogFactory.getLog(SODTxnProcessorImpl.class);

	@Override
	public Object preProcessTransaction(Object txnObject)  throws GLSException {
		OpeningPositionDO openingPosDO=(OpeningPositionDO) txnObject;
		
		return openingPosDO;
	}

	@Override
	public Object processTransaction(Object txnObject)  throws GLSException {
		OpeningPositionDO openingPosDO=(OpeningPositionDO) txnObject;
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering processTransaction() for "+openingPosDO);
		}
		
		//Persist SOD Transaction to Excel DB
		GLSExceldbDao.insertSODTxn(openingPosDO);
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving processTransaction()");
		}
		return openingPosDO;
	}

	@Override
	public Object postProcessTransaction(Object txnObject) throws GLSException {
		OpeningPositionDO openingPosDO=(OpeningPositionDO) txnObject;

		return openingPosDO;
	}
}
