/**
 * 
 */
package com.abcib.gls.intf.feed.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.alert.AlertMessageHandler;
import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.dao.GLSExceldbDao;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;
import com.abcib.gls.processor.OnlineTxnProcessorImpl;
import com.abcib.gls.processor.SODTxnProcessorImpl;
import com.abcib.gls.processor.TransactionProcessor;
import com.abcib.gls.validator.TransactionValidator;

/**
 * @author santosh.barada
 *
 */
public class RequestFeedHandler {
	
	private final static Log mLog = LogFactory.getLog(RequestFeedHandler.class);
	private final CopyOption[] options = new CopyOption[] {StandardCopyOption.REPLACE_EXISTING};

	public void handleTransactions(List<TransactionDO> onlineTxnDOs, String feedType) throws GLSException {
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering handleTransactions() for "+onlineTxnDOs.toString());
		}
		TransactionProcessor txnProcessor=new OnlineTxnProcessorImpl();
		try{
			String txnSeq=GLSExceldbDao.fetchNextOnlineTxnSeq("TXN");
			String maxSODSeq=(Long.parseLong(GLSExceldbDao.fetchNextOnlineTxnSeq("TXN"))-1)+"";
			txnSeq=maxSODSeq+txnSeq;
			for(TransactionDO transactionDO:onlineTxnDOs){
				if(TransactionValidator.validateOnlineTransactions(transactionDO,feedType)){
					transactionDO.setTransactionSeq(txnSeq);
					transactionDO=(TransactionDO) txnProcessor.preProcessTransaction(transactionDO);
					transactionDO=(TransactionDO) txnProcessor.processTransaction(transactionDO);
					transactionDO=(TransactionDO) txnProcessor.postProcessTransaction(transactionDO);
				}
			}
			
			try{
				ResponseFeedHandler.handleEODTransactions();
			}catch(Exception ex){
				mLog.fatal("Unable to process EOD", ex);
			}
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10005, "Unable to process Online Transaction", ex);
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving handleTransactions()");
		}
	}
	
	public void handleSODTransactions(List<OpeningPositionDO> sodPosDOList, String feedType) throws GLSException {
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering handleSODTransactions() for "+sodPosDOList.toString());
		}
		TransactionProcessor sodTxnProc=new SODTxnProcessorImpl();
		try{
			String seq=GLSExceldbDao.fetchNextOnlineTxnSeq("SOD");
			for(OpeningPositionDO sodPosDO : sodPosDOList){
				if(TransactionValidator.validateSODTransactions(sodPosDO, feedType)){
					sodPosDO.setTransactionSeq(seq);
					sodPosDO=(OpeningPositionDO) sodTxnProc.preProcessTransaction(sodPosDO);
					sodPosDO=(OpeningPositionDO) sodTxnProc.processTransaction(sodPosDO);
					sodPosDO=(OpeningPositionDO) sodTxnProc.postProcessTransaction(sodPosDO);
				}
			}
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10005, "Unable to process Start of Day Transaction", ex);
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving handleSODTransactions()");
		}
	}
	
	public void movefileToAcceptPath(File feedfile){
		String feedFileName="";
		try {
			feedFileName=feedfile.getName();
			if(feedfile!=null){
				Files.move(Paths.get(feedfile.getAbsolutePath()), 
						Paths.get(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_IN_ACCEPT_PATH+File.separator+feedFileName),options);
				AlertMessageHandler.logAlertMessage(feedFileName+" feed file has been processed successfully.");
				mLog.fatal(feedFileName+" feed file has been processed successfully.");
			}
		}catch (IOException e) {
			mLog.fatal("Got a fatal Exception while moving the file from " + feedfile.getAbsolutePath()
				+" to "+GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_IN_ACCEPT_PATH+File.separator+feedFileName, e);
		}
	}
	
	public void movefileToRejectPath(File feedfile){
		String feedFileName="";
		try {
			feedFileName=feedfile.getName();
			if(feedfile!=null){
				Files.move(Paths.get(feedfile.getAbsolutePath()),
						Paths.get(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_IN_REJECT_PATH+File.separator+feedFileName),options);
				AlertMessageHandler.logAlertMessage(feedFileName+" feed file has been rejected during processing. Please check application logs for further details.");
				mLog.fatal(feedFileName+" feed file has been rejected during processing. Please check application logs for further details.");
			}
		}catch (IOException e) {
			mLog.fatal("Got a fatal Exception while moving the file from " + feedfile.getAbsolutePath()
				+" to "+GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_IN_REJECT_PATH+File.separator+feedFileName, e);
		}
	}
}
