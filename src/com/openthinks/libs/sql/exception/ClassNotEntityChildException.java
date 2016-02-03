package com.openthinks.libs.sql.exception;
/**
 * 传入的类型没有继承Entity异常
 * @author dmj
 * @version 2010/11/19
 */
public class ClassNotEntityChildException extends RuntimeException{
	private static final long serialVersionUID = 6577650533252139638L;

	public ClassNotEntityChildException() {}

	public ClassNotEntityChildException(String message) {
		super(message+"不是Entity的子类型");
	}

	public ClassNotEntityChildException(Throwable cause) {
		super(cause);
	}

	public ClassNotEntityChildException(String message, Throwable cause) {
		super(message+"不是Entity的子类型", cause);
	}
}
