package openthinks.libs.sql.exception;
/**
 * 实体类反射异常
 * @author dmj
 * @since   2010/11/16
 */
public class EntityReflectException extends RuntimeException {
	private static final long serialVersionUID = -302784764171463472L;

	public EntityReflectException() {}

	public EntityReflectException(String message) {
		super("实体类反射异常:"+message);
	}

	public EntityReflectException(Throwable cause) {
		super(cause);
	}

	public EntityReflectException(String message, Throwable cause) {
		super("实体类反射异常:"+message, cause);
	}

}
