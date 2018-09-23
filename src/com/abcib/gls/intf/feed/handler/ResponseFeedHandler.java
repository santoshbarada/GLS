/**
 * 
 */
package com.abcib.gls.intf.feed.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.abcib.gls.alert.AlertMessageHandler;
import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.dao.GLSExceldbDao;
import com.abcib.gls.intf.feed.dataobjects.EndOfDayPositionDO;
import com.abcib.gls.processor.EODProcessorImpl;
import com.abcib.gls.processor.TransactionProcessor;
import com.abcib.gls.util.GLSFeedUtility;

/**
 * @author santosh.barada
 *
 */
public class ResponseFeedHandler {
	
	private final static Log mLog = LogFactory.getLog(ResponseFeedHandler.class);
	private final static CopyOption[] options = new CopyOption[] {StandardCopyOption.REPLACE_EXISTING};
	
	//Call EOD Process to generate EOD out feed
	public static void handleEODTransactions() {
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering handleEODTransactions()");
		}
		
		//Instrument List (Can be fetched from Instrument List Excel DB)
		List<String> instrumentsArr=new ArrayList<String>();
		instrumentsArr.add("IBM");
		instrumentsArr.add("MSFT");
		instrumentsArr.add("APPL");
		instrumentsArr.add("AMZN");
		instrumentsArr.add("NFLX");
		
		try{
			
			EndOfDayPositionDO eodPosDO=null;
			TransactionProcessor txnProc=new EODProcessorImpl();
			String nextEODSeq=GLSExceldbDao.fetchNextOnlineTxnSeq("EOD");
			ResponseFeedHandler.moveEODfilesToArchivePath();
			
			for(String instrument : instrumentsArr){
				eodPosDO= new EndOfDayPositionDO();
				eodPosDO.setInstrumentName(instrument);
				eodPosDO.setTransactionSeq(nextEODSeq);
				eodPosDO=(EndOfDayPositionDO) txnProc.preProcessTransaction(eodPosDO);
				eodPosDO=(EndOfDayPositionDO) txnProc.processTransaction(eodPosDO);
				eodPosDO=(EndOfDayPositionDO) txnProc.postProcessTransaction(eodPosDO);
			}
			
			if(generateEODResponseFeed(instrumentsArr)){
				AlertMessageHandler.logAlertMessage("End Of Day Response Feed has been generated successfully.");
				mLog.fatal("End Of Day Feed has been generated successfully.");
			}else{
				AlertMessageHandler.logAlertMessage("End Of Day Response Feed generation has been failed.");
				mLog.fatal("End Of Day Feed generation has been failed.");
			}
		}catch(Exception ex){
			AlertMessageHandler.logAlertMessage("End Of Day Response Feed generation has been failed.");
			mLog.fatal("End Of Day Feed generation has been failed.");
		}
		
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaving handleEODTransactions()");
		}
		
	}

	private static boolean generateEODResponseFeed(List<String> instrumentsArr) {
		if(mLog.isDebugEnabled()){
			mLog.debug("Entering generateEODResponseFeed()");
		}
		
		boolean feedGen=true;
		FileWriter fileWriter = null;
		String fileName="Expected_EndOfDay_Positions.txt";
		
		try{
			
			List<EndOfDayPositionDO> eodTxnList = new ArrayList<EndOfDayPositionDO>();
			for(String instrument : instrumentsArr){
				List<EndOfDayPositionDO> eodDos=GLSExceldbDao.fetchEODTxn(instrument, GLSConstants.EMPTY_STRING);
				eodTxnList.addAll(eodDos);
			}
			
			//Create EOD response file
			File eodOutFile = new File(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_OUT_RESPONSE_PATH+File.separator+fileName);
            fileWriter = new FileWriter(eodOutFile);
            
            
            //Write header to file
            String header=GLSFeedUtility.getEODOHeaderString();
            fileWriter.write(header);
            for(EndOfDayPositionDO eodTxn : eodTxnList){
            	fileWriter.write("\n");
				String dataString=GLSFeedUtility.getEODObjectToCommaSeparatedString(eodTxn);
				fileWriter.write(dataString);
			}
			
		}catch(Exception ex){
			
			feedGen=false;
		}finally{
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(mLog.isDebugEnabled()){
			mLog.debug("Leaaving generateEODResponseFeed()");
		}
		
		return feedGen;
	}
	
	public static void moveEODfilesToArchivePath(){
		try {
			
			File folder = new File(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_OUT_RESPONSE_PATH);
			File[] listOfFiles = folder.listFiles();
			
			for (int i = 0; i < listOfFiles.length; i++) {
				  if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")){
					  String feedFileName=listOfFiles[i].getName();
					  if(feedFileName!=null){
							Files.move(Paths.get(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_OUT_RESPONSE_PATH+File.separator+feedFileName), 
									Paths.get(GLSConstants.APPS_HOME_PATH+GLSConstants.FEED_OUT_ARCHIVE_PATH+File.separator+feedFileName),options);
						}
				  }
				  AlertMessageHandler.logAlertMessage("EOD feed files moved from response to archive directory successfully.");
				  mLog.fatal("EOD feed files moved from response to archive directory successfully.");
			}
		}catch (IOException e) {
			AlertMessageHandler.logAlertMessage("Got IOException while moving the file from response to archive directory");
			mLog.fatal("Got IOException while moving the file from response to archive directory", e);
		}catch (Exception e) {
			AlertMessageHandler.logAlertMessage("Got a fatal Exception while moving the file from response to archive directory");
			mLog.fatal("Got a fatal Exception while moving the file from response to archive directory", e);
		}
	}
}
