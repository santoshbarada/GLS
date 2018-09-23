/**
 * 
 */
package com.abcib.gls.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.dao.GLSExceldbDao;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;

/**
 * @author santosh.barada
 *
 */
public class OnlineTxnProcessorImpl  implements TransactionProcessor {
	
	private final static Log mLog = LogFactory.getLog(OnlineTxnProcessorImpl.class);

	@Override
	public Object preProcessTransaction(Object txnObject) throws GLSException {
		TransactionDO transactionDO=(TransactionDO) txnObject;
		
		return transactionDO;
	}

	@Override
	public Object processTransaction(Object txnObject) throws GLSException {
		TransactionDO transactionDO=(TransactionDO) txnObject;
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering processTransaction() for "+transactionDO);
		}
		
		//Persist Online Transaction to Excel DB
		GLSExceldbDao.insertOnlineTxn(transactionDO);
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving processTransaction()");
		}
		return transactionDO;
	}

	@Override
	public Object postProcessTransaction(Object txnObject) throws GLSException {
		TransactionDO transactionDO=(TransactionDO) txnObject;

		return transactionDO;
	}

}
