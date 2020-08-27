package com.openthinks.libs.sql.lang;

import java.util.Properties;

/**
 * 数据库参数配置类
 * 
 * @author dmj
 * 
 */
public final class Configurator extends Properties {
	private static final long serialVersionUID = 7744802081226565359L;
	/**
	 * 配置文件数据库驱动键名常量
	 */
	public final static String DRIVER = "DRIVER";
	/**
	 * 配置文件数据库连接地址键名常量
	 */
	public final static String URL = "URL";
	/**
	 * 配置文件数据库登录名键名常量
	 */
	public final static String USERNAME = "USERNAME";
	/**
	 * 配置文件数据库登录密码键名常量
	 */
	public final static String USERPWD = "USERPWD";

	public final static String USEDPOOL = "USEPOOL";

	public final static String DIALECT = "DIALECT";

	public Configurator() {
	}

	public Configurator(Configurator defaults) {
		super(defaults);
	}

	/**
	 * 取得配置文件中的数据库驱动键名对应的值
	 * 
	 * @return String 数据库驱动键名对应的值
	 */
	public String getDriver() {
		return getProperty(DRIVER);
	}

	/**
	 * 设置配置文件中的数据库驱动键名对应的值
	 * 
	 * @param driver
	 *            数据库驱动键名对应的值
	 */
	public void setDriver(String driver) {
		setProperty(DRIVER, driver);
	}

	/**
	 * 取得配置文件中的数据库连接地址键名对应的值
	 * 
	 * @return String 数据库连接地址键名对应的值
	 */
	public String getUrl() {
		return getProperty(URL);
	}

	/**
	 * 设置配置文件中的数据库连接地址键名对应的值
	 * 
	 * @param url
	 *            连接地址键名对应的值
	 */
	public void setUrl(String url) {
		setProperty(URL, url);
	}

	/**
	 * 取得配置文件中的数据库登录名键名对应的值
	 * 
	 * @return String 数据库登录名键名对应的值
	 */
	public String getUserName() {
		return getProperty(USERNAME);
	}

	/**
	 * 设置配置文件中的数据库登录名键名对应的值
	 * 
	 * @param username
	 *            数据库登录名键名对应的值
	 */
	public void setUserName(String username) {
		setProperty(USERNAME, username);
	}

	/**
	 * 取得配置文件中的数据库登录密码键名对应的值
	 * 
	 * @return String 数据库登录密码键名对应的值
	 */
	public String getUserPwd() {
		return getProperty(USERPWD);
	}

	/**
	 * 设置配置文件中的数据库登录密码键名对应的值
	 * 
	 * @param userpwd
	 *            数据库登录密码键名对应的值
	 */
	public void setUserPwd(String userpwd) {
		setProperty(USERPWD, userpwd);
	}

	public void setUsePool(boolean isUse) {
		setProperty(USEDPOOL, String.valueOf(isUse));
	}

	public boolean isUsePool() {
		boolean isUse = Boolean.valueOf(getProperty(USEDPOOL));
		return isUse;
	}

	/**
	 * 获取数据库方言类别, current support 3 dialect: mysql, oracle, sqlserver<BR>
	 * The default value is Dialect.MYSQL
	 * @return Dialect
	 */
	public Dialect getDialect() {
		try {
			return Dialect.toDialect(getProperty(DIALECT));
		} catch (Exception e) {
			return Dialect.MYSQL;
		}
	}

	public void setDialect(Dialect dialect) {
		setProperty(DIALECT, dialect.toString());
	}

	public enum Dialect {
		MYSQL, ORACLE, SQLSERVER, DERBY;

		static Dialect toDialect(String dialect) {
			for (Dialect d : Dialect.values()) {
				if (d.toString().equalsIgnoreCase(dialect)) {
					return d;
				}
			}
			return null;
		}
	}
}
