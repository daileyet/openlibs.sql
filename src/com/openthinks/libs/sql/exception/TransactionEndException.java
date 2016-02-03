package com.openthinks.libs.sql.exception;
/**
 * 事务结束提交时异常
 * @author dmj
 *
 */
public class TransactionEndException extends TransactionException {
	private static final long serialVersionUID = 7545900712402801181L;

	public TransactionEndException() {
		
	}

	public TransactionEndException(String message) {
		super(message);
	}

	public TransactionEndException(Throwable cause) {
		super(cause);
	}

	public TransactionEndException(String message, Throwable cause) {
		super(message, cause);
	}
}
