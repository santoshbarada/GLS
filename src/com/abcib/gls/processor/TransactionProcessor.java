/**
 * 
 */
package com.abcib.gls.processor;

import com.abcib.gls.exception.GLSException;

/**
 * @author santosh.barada
 *
 */
public interface TransactionProcessor {
	
	public Object preProcessTransaction(Object txnObject) throws GLSException;
	public Object processTransaction(Object txnObject) throws GLSException;
	public Object postProcessTransaction(Object txnObject) throws GLSException;

}
