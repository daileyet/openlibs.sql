/**
 * 数据库访问自定义异常包
 */
package com.openthinks.libs.sql.exception;
/**
 * 配置文件没有找到异常
 * @author dmj
 *
 */
public class ConfigureFileNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -1585458801518490846L;

	public ConfigureFileNotFoundException() {}

	public ConfigureFileNotFoundException(String message) {
		super(message);
	}

	public ConfigureFileNotFoundException(Throwable cause) {
		super(cause);
	}

	public ConfigureFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
