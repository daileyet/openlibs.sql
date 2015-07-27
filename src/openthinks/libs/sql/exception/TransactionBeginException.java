package openthinks.libs.sql.exception;
/**
 * 事务开启时异常类
 * @author dmj
 *
 */
public class TransactionBeginException extends TransactionException {
	private static final long serialVersionUID = 268682038638991995L;

	public TransactionBeginException() {
		
	}

	public TransactionBeginException(String message) {
		super(message);
	}

	public TransactionBeginException(Throwable cause) {
		super(cause);
	}

	public TransactionBeginException(String message, Throwable cause) {
		super(message, cause);
	}
}
