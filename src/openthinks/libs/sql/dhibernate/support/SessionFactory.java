package openthinks.libs.sql.dhibernate.support;

import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.lang.Configurator;
import openthinks.libs.sql.lang.ConfiguratorFactory;

/**
 * Session的工厂类
 * 
 * @author dmj
 * @version 17:47 2010/11/19
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
			return new DefaultSession();
		else
			return new DefaultSession(defaultConfigurator);
	}

	/**
	 * 
	 * @param configurator
	 *            Configurator
	 * @return Session
	 */
	public static Session getSession(Configurator configurator) {
		return new DefaultSession(configurator);
	}
}
