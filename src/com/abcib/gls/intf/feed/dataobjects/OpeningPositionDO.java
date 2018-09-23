/**
 * 
 */
package com.abcib.gls.intf.feed.dataobjects;

/**
 * @author santosh.barada
 *
 */
public class OpeningPositionDO {
	
	private String transactionSeq;
	private String instrumentName;
	private String accountId;
	private String accountType;
	private String openingQuantity;
	
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
	 * @return the openingQuantity
	 */
	public String getOpeningQuantity() {
		return openingQuantity;
	}
	/**
	 * @param openingQuantity the openingQuantity to set
	 */
	public void setOpeningQuantity(String openingQuantity) {
		this.openingQuantity = openingQuantity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OpeningPositionDO [transactionSeq=" + transactionSeq + ", instrumentName=" + instrumentName + ", accountId=" + accountId + ", accountType="
				+ accountType + ", openingQuantity=" + openingQuantity + "]";
	}

}
