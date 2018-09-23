/**
 * 
 */
package com.abcib.gls.intf.feed.dataobjects;

/**
 * @author santosh.barada
 *
 */
public class EndOfDayPositionDO {
	
	private String transactionSeq;
	private String instrumentName;
	private String accountId;
	private String accountType;
	private String eodQuantity;
	private String deltaQuantity;
	
	/**
	 * @return the transactionSeq
	 */
	public String getTransactionSeq() {
		return transactionSeq;
	}
	/**
	 * @param transactionSeq the transactionSeq to set
	 */
	public void setTransactionSeq(String transactionSeq) {
		this.transactionSeq = transactionSeq;
	}
	/**
	 * @return the instrumentName
	 */
	public String getInstrumentName() {
		return instrumentName;
	}
	/**
	 * @param instrumentName the instrumentName to set
	 */
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the eodQuantity
	 */
	public String getEodQuantity() {
		return eodQuantity;
	}
	/**
	 * @param eodQuantity the eodQuantity to set
	 */
	public void setEodQuantity(String eodQuantity) {
		this.eodQuantity = eodQuantity;
	}
	/**
	 * @return the deltaQuantity
	 */
	public String getDeltaQuantity() {
		return deltaQuantity;
	}
	/**
	 * @param deltaQuantity the deltaQuantity to set
	 */
	public void setDeltaQuantity(String deltaQuantity) {
		this.deltaQuantity = deltaQuantity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EndOfDayPositionDO [transactionSeq=" + transactionSeq + ", instrumentName=" + instrumentName + ", accountId=" + accountId + ", accountType="
				+ accountType + ", eodQuantity=" + eodQuantity + ", deltaQuantity=" + deltaQuantity + "]";
	}
}
