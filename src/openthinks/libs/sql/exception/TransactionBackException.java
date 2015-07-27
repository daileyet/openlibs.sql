package openthinks.libs.sql.exception;
/**
 * 事务回滚异常
 * @author dmj
 *
 */
public class TransactionBackException extends TransactionException {
	private static final long serialVersionUID = -74019271603206169L;

	public TransactionBackException() {
	}

	public TransactionBackException(String message) {
		super(message);
	}

	public TransactionBackException(Throwable cause) {
		super(cause);
	}

	public TransactionBackException(String message, Throwable cause) {
		super(message, cause);
	}

}
