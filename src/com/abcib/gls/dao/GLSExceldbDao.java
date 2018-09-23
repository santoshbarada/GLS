/**
 * 
 */
package com.abcib.gls.dao;

import java.util.ArrayList;
import java.util.List;

import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.dataobjects.EndOfDayPositionDO;
import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

/**
 * @author santosh.barada
 *
 */
public class GLSExceldbDao {
	
	public static String fetchNextOnlineTxnSeq(String txnType) throws GLSException {
		String nextTxnSeq="1";
		Fillo fillo=new Fillo();
		Connection connection=null;
		Recordset recordset=null;
		String sheetName="";
		try{
			if(txnType.equals("TXN")){
				sheetName="OnlineTransactions";
				connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_TXN_FILE_PATH);
			}else if(txnType.equals("SOD")){
				sheetName="SODTransactions";
				connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_SOD_FILE_PATH);
			}else if(txnType.equals("EOD")){
				sheetName="EODTransactions";
				connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_EOD_FILE_PATH);
			}else{
				return "1";
			}
			
			String strQuery="Select * from "+sheetName;
			recordset=connection.executeQuery(strQuery);
			
			while(recordset.next()){
				nextTxnSeq=recordset.getField("SequenceNo");
			}
			
			nextTxnSeq=(Integer.parseInt(nextTxnSeq)+1)+"";
		}catch(Exception ex){
			nextTxnSeq="1";
		}finally{
			try{
				recordset.close();
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return nextTxnSeq;	
	}
	
	public static List<TransactionDO> fetchOnlineTxn(String instrumentName, String seqNo) throws GLSException {
		Fillo fillo=new Fillo();
		Connection connection=null;
		Recordset recordset=null;
		TransactionDO txnDo=null;
		List<TransactionDO> txnDoList=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_TXN_FILE_PATH);
			String maxSeq="1";
			if(seqNo!=null && !seqNo.equals(GLSConstants.EMPTY_STRING)){
				maxSeq=seqNo;
			}else{
				maxSeq=fetchNextOnlineTxnSeq("TXN");
				maxSeq=(Integer.parseInt(maxSeq)-1)+"";
			}
			String strQuery="Select * from OnlineTransactions where Instrument='"+instrumentName+"' and SequenceNo='"+maxSeq+"'";
			recordset=connection.executeQuery(strQuery);
			txnDoList=new ArrayList<TransactionDO>();
			while(recordset.next()){
				txnDo=new TransactionDO();
				txnDo.setTransactionId(recordset.getField("TransactionId"));
				txnDo.setInstrumentName(recordset.getField("Instrument"));
				txnDo.setTransactionType(recordset.getField("TransactionType"));
				txnDo.setTransactionQuantity(recordset.getField("TransactionQuantity"));
				txnDo.setTransactionSeq(recordset.getField("SequenceNo"));
				txnDoList.add(txnDo);
			}
		}catch(Exception ex){
			
			if(ex.getMessage().equals("No records found")){
				txnDoList=new ArrayList<TransactionDO>();
			}else{
				throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to fetch Online Transactions", ex);
			}
			
		}finally{
			try{
				recordset.close();
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return txnDoList;	
	}
	
	public static List<TransactionDO> fetchOnlineTxnsAfterSOD(String instrumentName) throws GLSException {
		Fillo fillo=new Fillo();
		Connection connection=null;
		Recordset recordset=null;
		TransactionDO txnDo=null;
		List<TransactionDO> txnDoList=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_TXN_FILE_PATH);
			String maxSODSeq=(Long.parseLong(fetchNextOnlineTxnSeq("SOD"))-1)+"";
			String strQuery="Select * from OnlineTransactions where Instrument='"+instrumentName+"' and SequenceNo like '"+maxSODSeq+"%'";
			recordset=connection.executeQuery(strQuery);
			txnDoList=new ArrayList<TransactionDO>();
			while(recordset.next()){
				txnDo=new TransactionDO();
				txnDo.setTransactionId(recordset.getField("TransactionId"));
				txnDo.setInstrumentName(recordset.getField("Instrument"));
				txnDo.setTransactionType(recordset.getField("TransactionType"));
				txnDo.setTransactionQuantity(recordset.getField("TransactionQuantity"));
				txnDo.setTransactionSeq(recordset.getField("SequenceNo"));
				txnDoList.add(txnDo);
			}
		}catch(Exception ex){
			if(ex.getMessage().equals("No records found")){
				txnDoList=new ArrayList<TransactionDO>();
			}else{
				throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to fetch Online Transactions", ex);
			}
		}finally{
			try{
				recordset.close();
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return txnDoList;	
	}
	
	public static boolean insertOnlineTxn(TransactionDO txnDO) throws GLSException{
		boolean flgSuccess=true;
		Fillo fillo=new Fillo();
		Connection connection=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_TXN_FILE_PATH);
			String txnValues="'"+txnDO.getTransactionId()+"','"+txnDO.getInstrumentName()+"','"+txnDO.getTransactionType()+"','"+txnDO.getTransactionQuantity()+"','"+txnDO.getTransactionSeq()+"'";
			String strQuery="INSERT INTO OnlineTransactions(TransactionId,Instrument,TransactionType,TransactionQuantity,SequenceNo) VALUES("+txnValues+")";
			 
			connection.executeUpdate(strQuery);
			 
			connection.close();
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to insert Online Transaction", ex);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return flgSuccess;	
	}
	
	public static boolean updateOnlineTxn(TransactionDO txnDO) throws GLSException{
		boolean flgSuccess=true;
		
		try{
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to update Online Transaction", ex);
		}
		
		return flgSuccess;	
	}
	
	public static List<OpeningPositionDO> fetchSODTxn(String instrumentName, String seqNo) throws GLSException{
		Fillo fillo=new Fillo();
		Connection connection=null;
		Recordset recordset=null;
		OpeningPositionDO txnDo=null;
		List<OpeningPositionDO> txnDoList=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_SOD_FILE_PATH);
			String maxSeq="1";
			if(seqNo!=null && !seqNo.equals(GLSConstants.EMPTY_STRING)){
				maxSeq=seqNo;
			}else{
				maxSeq=fetchNextOnlineTxnSeq("SOD");
				maxSeq=(Integer.parseInt(maxSeq)-1)+"";
			}
			String strQuery="Select * from SODTransactions where Instrument='"+instrumentName+"' and SequenceNo='"+maxSeq+"'";
			recordset=connection.executeQuery(strQuery);
			 
			txnDoList=new ArrayList<OpeningPositionDO>();
			while(recordset.next()){
				txnDo=new OpeningPositionDO();
				txnDo.setInstrumentName(recordset.getField("Instrument"));
				txnDo.setAccountId(recordset.getField("Account"));
				txnDo.setAccountType(recordset.getField("AccountType"));
				txnDo.setOpeningQuantity(recordset.getField("Quantity"));
				txnDo.setTransactionSeq(recordset.getField("SequenceNo"));
				txnDoList.add(txnDo);
			}
		}catch(Exception ex){
			
			if(ex.getMessage().equals("No records found")){
				txnDoList=new ArrayList<OpeningPositionDO>();
			}else{
				throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to fetch SOD Transaction", ex);
			}
		}finally{
			try{
				recordset.close();
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return txnDoList;	
	}
	
	public static boolean insertSODTxn(OpeningPositionDO txnDO) throws GLSException{
		boolean flgSuccess=true;
		Fillo fillo=new Fillo();
		Connection connection=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_SOD_FILE_PATH);
			String txnValues="'"+txnDO.getInstrumentName()+"','"+txnDO.getAccountId()+"','"+txnDO.getAccountType()+"','"+txnDO.getOpeningQuantity()+"','"+txnDO.getTransactionSeq()+"'";
			String strQuery="INSERT INTO SODTransactions(Instrument,Account,AccountType,Quantity,SequenceNo) VALUES("+txnValues+")";
			 
			connection.executeUpdate(strQuery);
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to insert SOD Transaction", ex);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return flgSuccess;	
	}
	
	public static boolean updateSODTxn(OpeningPositionDO txnDO) throws GLSException{
		boolean flgSuccess=true;
		
		try{
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to update SOD Transaction", ex);
		}
		
		return flgSuccess;	
	}
	
	public static List<EndOfDayPositionDO> fetchEODTxn(String instrumentName, String seqNo) throws GLSException{
		Fillo fillo=new Fillo();
		Connection connection=null;
		Recordset recordset=null;
		EndOfDayPositionDO txnDo=null;
		List<EndOfDayPositionDO> txnDoList=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_EOD_FILE_PATH);
			String maxSeq="1";
			if(seqNo!=null && !seqNo.equals(GLSConstants.EMPTY_STRING)){
				maxSeq=seqNo;
			}else{
				maxSeq=fetchNextOnlineTxnSeq("EOD");
				maxSeq=(Integer.parseInt(maxSeq)-1)+"";
			}
			
			String strQuery="Select * from EODTransactions where Instrument='"+instrumentName+"' and SequenceNo='"+maxSeq+"'";
			recordset=connection.executeQuery(strQuery);
			 
			txnDoList=new ArrayList<EndOfDayPositionDO>();
			while(recordset.next()){
				txnDo=new EndOfDayPositionDO();
				txnDo.setInstrumentName(recordset.getField("Instrument"));
				txnDo.setAccountId(recordset.getField("Account"));
				txnDo.setAccountType(recordset.getField("AccountType"));
				txnDo.setEodQuantity(recordset.getField("Quantity"));
				txnDo.setDeltaQuantity(recordset.getField("Delta"));
				txnDo.setTransactionSeq(recordset.getField("SequenceNo"));
				txnDoList.add(txnDo);
			}

		}catch(Exception ex){
			
			if(ex.getMessage().equals("No records found")){
				txnDoList=new ArrayList<EndOfDayPositionDO>();
			}else{
				throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to fetch EOD Transaction", ex);
			}
		}finally{
			try{
				recordset.close();
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return txnDoList;	
	}
	
	public static boolean insertEODTxn(EndOfDayPositionDO txnDO) throws GLSException{
		boolean flgSuccess=true;
		Fillo fillo=new Fillo();
		Connection connection=null;
		try{
			connection=fillo.getConnection(GLSConstants.APPS_HOME_PATH+GLSConstants.XLS_DB_EOD_FILE_PATH);
			String txnValues="'"+txnDO.getInstrumentName()+"','"+txnDO.getAccountId()+"','"+txnDO.getAccountType()+"','"+txnDO.getEodQuantity()+"','"+txnDO.getDeltaQuantity()+"','"+txnDO.getTransactionSeq()+"'";
			String strQuery="INSERT INTO EODTransactions(Instrument,Account,AccountType,Quantity,Delta,SequenceNo) VALUES("+txnValues+")";
			
			connection.executeUpdate(strQuery);
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to insert EOD Transaction", ex);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){
				
			}
		}
		
		return flgSuccess;	
	}
	
	public static boolean updateEODTxn(OpeningPositionDO txnDO) throws GLSException{
		boolean flgSuccess=true;
		
		try{
			
		}catch(Exception ex){
			throw new GLSException(GLSConstants.GLS_ERR_CODE_10001, "Unable to update EOD Transaction", ex);
		}
		
		return flgSuccess;	
	}
}
