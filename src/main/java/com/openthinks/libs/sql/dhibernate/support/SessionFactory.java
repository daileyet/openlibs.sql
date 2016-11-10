package com.openthinks.libs.sql.dhibernate.support;

import com.openthinks.libs.sql.dhibernate.Session;
import com.openthinks.libs.sql.lang.Configurator;
import com.openthinks.libs.sql.lang.ConfiguratorFactory;

/**
 * Session的工厂类
 * <BR>
 * Date:	17:47 2010/11/19
 * @author dmj
 */
public class SessionFactory {

	private static Configurator defaultConfigurator;

	/**
	 * @param defaultConfigurator
	 *            the defaultConfigurator to set
	 */
	public static void setDefaultConfigurator(Configurator defaultConfigurator) {
		ConfiguratorFactory.setConfigurator(defaultConfigurator);
		SessionFactory.defaultConfigurator = defaultConfigurator;
	}

	/**
	 * 获取Session对象
	 * 
	 * @return Session对象
	 */
	public static Session getSession() {
		if (defaultConfigurator == null)
			return new DefaultSessionImpl();
		else
			return new DefaultSessionImpl(defaultConfigurator);
	}

	/**
	 * 
	 * @param configurator
	 *            Configurator
	 * @return Session
	 */
	public static Session getSession(Configurator configurator) {
		return new DefaultSessionImpl(configurator);
	}
}
