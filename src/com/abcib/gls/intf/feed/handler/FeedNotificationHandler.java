/**
 * 
 */
package com.abcib.gls.intf.feed.handler;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;
import com.abcib.gls.util.GLSFeedUtility;

/**
 * @author santosh.barada
 *
 */
public class FeedNotificationHandler implements Runnable{

	private final static Log mLog = LogFactory.getLog(FeedNotificationHandler.class);
	
	private File feedfile =null;
	private String feedFileName=null;
	private String feedType =null;
	
	public FeedNotificationHandler(File file) {
		this.feedfile=file;
		this.feedFileName=file.getName();
		
		if(feedfile!=null && feedFileName.contains("Positions")){
			this.feedType=GLSConstants.FEED_TYPE_POS;
		}else if(feedfile!=null && feedFileName.contains("Transactions")){
			this.feedType=GLSConstants.FEED_TYPE_TXN;
		}else{
			this.feedType="";
		}
	}

	public void run() {
		mLog.debug("Processing started for feed file: "+feedFileName);
		processFeed();
		mLog.debug("Processing finished for feed file: "+feedFileName);
	}
	
	private void processFeed() {
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering processOnlineTransactions() for feed processing for file: "+feedFileName);
		}
		
		List<TransactionDO> onlineTxnDOs=null;
		List<OpeningPositionDO> sodPosDOList=null;
		RequestFeedHandler feedHandler=new RequestFeedHandler();
		
		try{
			if(feedType.equals(GLSConstants.FEED_TYPE_POS)){
				sodPosDOList=GLSFeedUtility.convertFileToPositionObject(feedfile);
				feedHandler.handleSODTransactions(sodPosDOList,feedType);
				feedHandler.movefileToAcceptPath(feedfile);
			}else if(feedType.equals(GLSConstants.FEED_TYPE_TXN)){
				onlineTxnDOs=GLSFeedUtility.convertJSONFileToTxnObject(feedfile);
				feedHandler.handleTransactions(onlineTxnDOs,feedType);
				feedHandler.movefileToAcceptPath(feedfile);
			}else{
				feedHandler.movefileToRejectPath(feedfile);
			}
		}catch(Exception ex){
			mLog.fatal("Exception: Unable to process requested feed "+feedFileName,ex);
			feedHandler.movefileToRejectPath(feedfile);
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving processOnlineTransactions() after feed processing for file: "+feedFileName);
		}
	}
}
