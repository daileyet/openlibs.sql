package openthinks.libs.sql.exception;
/**
 * 事务异常
 * @author dmj
 *
 */
public class TransactionException extends Exception{
	private static final long serialVersionUID = -4445784918368066030L;

	public TransactionException() {
		
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

}
