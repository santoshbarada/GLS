/**
 * 
 */
package com.abcib.gls.intf.feed.dataobjects;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author santosh.barada
 *
 */
public class TransactionDO {
	
	private String transactionSeq;
	@JsonProperty("TransactionId")
	private String transactionId;
	@JsonProperty("Instrument")
	private String instrumentName;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("TransactionQuantity")
	private String transactionQuantity;
	
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
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return Integer.parseInt(transactionId);
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @return the transactionQuantity
	 */
	public String getTransactionQuantity() {
		return transactionQuantity;
	}
	/**
	 * @param transactionQuantity the transactionQuantity to set
	 */
	public void setTransactionQuantity(String transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactionDO [transactionSeq=" + transactionSeq + ", transactionId=" + transactionId +
				", instrumentName=" + instrumentName + ", transactionType=" + transactionType + ", transactionQuantity=" + transactionQuantity+ "]";
	}
}
