/**
 * 
 */
package com.abcib.gls.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.dao.GLSExceldbDao;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.dataobjects.EndOfDayPositionDO;
import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;

/**
 * @author santosh.barada
 *
 */
public class EODProcessorImpl implements TransactionProcessor {
	
	private final static Log mLog = LogFactory.getLog(EODProcessorImpl.class);

	@Override
	public Object preProcessTransaction(Object txnObject) throws GLSException {
		EndOfDayPositionDO eodPositionDO=(EndOfDayPositionDO) txnObject;
		
		return eodPositionDO;
	}

	@Override
	public Object processTransaction(Object txnObject) throws GLSException {
		EndOfDayPositionDO eodPositionDO=(EndOfDayPositionDO) txnObject;
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering processTransaction() for "+eodPositionDO);
		}
		try{
			List<OpeningPositionDO> sodTxnDOs=GLSExceldbDao.fetchSODTxn(eodPositionDO.getInstrumentName(), GLSConstants.EMPTY_STRING);
			List<TransactionDO> transactionDOs=null;
			try{
				transactionDOs=GLSExceldbDao.fetchOnlineTxnsAfterSOD(eodPositionDO.getInstrumentName());
			}catch(Exception ex){
				transactionDOs=new ArrayList<TransactionDO>();
			}
			
			Map<String,EndOfDayPositionDO> instrEODPosMap=new HashMap<String,EndOfDayPositionDO>();
			Map<String,OpeningPositionDO> tmpSODPosMap=new HashMap<String,OpeningPositionDO>();
			
			if(mLog.isDebugEnabled()){
				mLog.debug("OpeningPositionDO List for EOD : "+sodTxnDOs);
			}
			//Create EndOfDayPositionDO objects using OpeningPositionDO for the Instrument 
			for(OpeningPositionDO sodTxnDO : sodTxnDOs){
				if(sodTxnDO!=null && sodTxnDO.getAccountId()!=null && sodTxnDO.getAccountType()!=null){
					EndOfDayPositionDO eodPosDO=new EndOfDayPositionDO();
					eodPosDO.setAccountId(sodTxnDO.getAccountId());
					eodPosDO.setAccountType(sodTxnDO.getAccountType());
					eodPosDO.setInstrumentName(eodPositionDO.getInstrumentName());
					eodPosDO.setTransactionSeq(eodPositionDO.getTransactionSeq());
					eodPosDO.setEodQuantity(sodTxnDO.getOpeningQuantity());
					instrEODPosMap.put((sodTxnDO.getAccountId()+sodTxnDO.getAccountType()), eodPosDO);
					tmpSODPosMap.put((sodTxnDO.getAccountId()+sodTxnDO.getAccountType()), sodTxnDO);
				}
			}
			
			if(mLog.isDebugEnabled()){
				mLog.debug("EOD Position DO Map for EOD : "+instrEODPosMap);
			}
			
			Set<String> keySet = instrEODPosMap.keySet();
			
			if(mLog.isDebugEnabled()){
				mLog.debug("EOD key set to be processed : "+keySet);
			}
			
			//Iterate Transactions for the Instrument 
			for(TransactionDO txnDO : transactionDOs){
				long txnQuantity=0;
				if(txnDO!=null && txnDO.getTransactionQuantity()!=null && txnDO.getTransactionType()!=null && txnDO.getTransactionType()!=null){
					txnQuantity=Long.parseLong(txnDO.getTransactionQuantity());
					
					//Iterate EndOfDayPositionDO and update the Eod Quantity
					for(String posKey : keySet){
						EndOfDayPositionDO tempEodPosDO=instrEODPosMap.get(posKey);
						long tmpEodQuantity=Long.parseLong(tempEodPosDO.getEodQuantity());
						
						//Updated EOD Quantity
						if(txnDO.getTransactionType().equals(GLSConstants.TRANSACTION_TYPE_B) && tempEodPosDO.getAccountType().equals(GLSConstants.ACCOUNT_TYPE_E)){
							tmpEodQuantity=tmpEodQuantity+txnQuantity;
						}else if(txnDO.getTransactionType().equals(GLSConstants.TRANSACTION_TYPE_B) && tempEodPosDO.getAccountType().equals(GLSConstants.ACCOUNT_TYPE_E)){
							tmpEodQuantity=tmpEodQuantity-txnQuantity;
						}else if(txnDO.getTransactionType().equals(GLSConstants.TRANSACTION_TYPE_S) && tempEodPosDO.getAccountType().equals(GLSConstants.ACCOUNT_TYPE_E)){
							tmpEodQuantity=tmpEodQuantity-txnQuantity;
						}else if(txnDO.getTransactionType().equals(GLSConstants.TRANSACTION_TYPE_S) && tempEodPosDO.getAccountType().equals(GLSConstants.ACCOUNT_TYPE_I)){
							tmpEodQuantity=tmpEodQuantity+txnQuantity;
						}
						tempEodPosDO.setEodQuantity(tmpEodQuantity+"");
						instrEODPosMap.put(posKey, tempEodPosDO);
					}
				}
			}
			
			if(mLog.isDebugEnabled()){
				mLog.debug("EOD Position DO Map for EOD : "+instrEODPosMap);
			}
			
			if(mLog.isDebugEnabled()){
				mLog.debug("SOD Position DO Map for EOD : "+tmpSODPosMap);
			}
			//Iterate EndOfDayPositionDO and update the Delta Quantity
			for(String eodPosKey : keySet){
				EndOfDayPositionDO tempEodPosDO=instrEODPosMap.get(eodPosKey);
				OpeningPositionDO tempSODPosDO=tmpSODPosMap.get(eodPosKey);
				
				long eodQuantity=Long.parseLong(tempEodPosDO.getEodQuantity());
				long sodQuantity=Long.parseLong(tempSODPosDO.getOpeningQuantity());
				long deltaQuantity=eodQuantity-sodQuantity; //Delta
				tempEodPosDO.setDeltaQuantity(deltaQuantity+"");
				instrEODPosMap.put(eodPosKey, tempEodPosDO);
			}
			
			//Persist EOD Transaction to Excel DB
			for(String eodPosKey : keySet){
				EndOfDayPositionDO eodPosDO=instrEODPosMap.get(eodPosKey);
				GLSExceldbDao.insertEODTxn(eodPosDO);
			}
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10005, "Unable to process EOD", ex);
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving processTransaction()");
		}
		return eodPositionDO;
	}

	@Override
	public Object postProcessTransaction(Object txnObject) throws GLSException {
		EndOfDayPositionDO eodPositionDO=(EndOfDayPositionDO) txnObject;
		
		return eodPositionDO;
	}

}
