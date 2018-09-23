package com.abcib.gls.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.abcib.gls.constants.GLSConstants;
import com.abcib.gls.exception.GLSException;
import com.abcib.gls.intf.feed.dataobjects.EndOfDayPositionDO;
import com.abcib.gls.intf.feed.dataobjects.OpeningPositionDO;
import com.abcib.gls.intf.feed.dataobjects.TransactionDO;

public class GLSFeedUtility
{
	
	public static List<TransactionDO> convertJSONStringToTxnObject(String jsonInString) throws GLSException {
		//JSON from String to Object
		List<TransactionDO> txnObjs = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			txnObjs = mapper.readValue(jsonInString, mapper.getTypeFactory().constructCollectionType(List.class, TransactionDO.class));
		} catch (Exception e) {
			new GLSException(GLSConstants.GLS_ERR_CODE_10005, "Unable to parse json from input feed file", e);
		}
		
		return txnObjs;
	}
	
	public static List<TransactionDO> convertJSONFileToTxnObject(File jsonInFile) throws GLSException {
		//JSON from File to Object
		List<TransactionDO> txnObjs = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			txnObjs = mapper.readValue(jsonInFile, mapper.getTypeFactory().constructCollectionType(List.class, TransactionDO.class));
		} catch (Exception e) {
			new GLSException(GLSConstants.GLS_ERR_CODE_10005, "Unable to parse json from input feed file", e);
		}
		
		return txnObjs;
	}

	public static List<OpeningPositionDO> convertFileToPositionObject(File feedfile) throws GLSException {
		
		List<OpeningPositionDO> positionList= new ArrayList<OpeningPositionDO>();
		OpeningPositionDO posDO=null;
		try {
            BufferedReader in = new BufferedReader(new FileReader(feedfile.getAbsolutePath()));
            String str;
            int lnNo=1;
            while ((str = in.readLine()) != null) {
            	if(lnNo!=1){
            		String[] itemArr=str.split(",");
                	posDO=new OpeningPositionDO();
                	posDO.setInstrumentName(itemArr[0]);
                	posDO.setAccountId(itemArr[1]);
                	posDO.setAccountType(itemArr[2]);
                	posDO.setOpeningQuantity(itemArr[3]);
                	positionList.add(posDO);
            	}
            	lnNo++;
            }
            in.close();
        } catch (IOException e) {
        	new GLSException(GLSConstants.GLS_ERR_CODE_10005, "Unable to parse Start Of Day Position feed file", e);
        }
		return positionList;
	}
	
	public static String getEODObjectToCommaSeparatedString(EndOfDayPositionDO eodPosDO) {
		
		String eodTxnString=eodPosDO.getInstrumentName()+","+eodPosDO.getAccountId()+","+eodPosDO.getAccountType()+","+eodPosDO.getEodQuantity()+","+eodPosDO.getDeltaQuantity();
		
		return eodTxnString;		
	}
	
	public static String getEODOHeaderString() {
		
		String eodTxnString="Instrument,Account,AccountType,Quantity,Delta";
		
		return eodTxnString;		
	}
	
}