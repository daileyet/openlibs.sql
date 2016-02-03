/**
 * 数据库实体访问常用包
 */
package com.openthinks.libs.sql.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.openthinks.libs.sql.exception.ConfigureFileNotFoundException;

/**
 * 配置器工厂类
 * 
 * @author dmj
 * 
 */
public class ConfiguratorFactory {

	private static Configurator configurator = null;

	/**
	 * @param configurator
	 *            the configurator to set
	 */
	public static void setConfigurator(Configurator configurator) {
		ConfiguratorFactory.configurator = configurator;
	}

	/**
	 * 取得配置文件名为dbconfig.properties的数据库默认配置实例
	 * 
	 * @throws ConfigureFileNotFoundException
	 *             配置文件没有找到异常
	 * @return Configurator 数据库连接属性配置实例
	 */
	public static Configurator getDefaultInstance() {
		return getDefaultInstance(ConfiguratorFactory.class,
				"dbconfig.properties");
	}

	/**
	 * 根据加载配置参数所在的类clz取得配置文件名为dbconfig.properties的数据库默认配置实例
	 * 
	 * @param clz
	 *            加载配置参数所在的类
	 * @throws ConfigureFileNotFoundException
	 *             配置文件没有找到异常
	 * @return Configurator 数据库连接属性配置实例
	 */
	public static Configurator getDefaultInstance(Class<?> clz) {
		return getDefaultInstance(clz, "dbconfig.properties");
	}

	/**
	 * 根据配置文件名称取得数据库默认配置实例
	 * 
	 * @param clz
	 *            加载配置参数所在的类
	 * @param configureName
	 *            配置文件名
	 * @throws ConfigureFileNotFoundException
	 *             配置文件没有找到异常
	 * @return Configurator 数据库连接属性配置实例
	 */
	public static Configurator getDefaultInstance(Class<?> clz,
			String configureName) {
		if (configurator == null) {
			configurator = new Configurator();
			InputStream in = clz.getResourceAsStream("/" + configureName);
			try {
				configurator.load(in);
			} catch (IOException e) {
				configurator = null;
				throw new ConfigureFileNotFoundException("数据库默认配置文件未能找到",
						e.fillInStackTrace());
			}
		}
		return configurator;
	}

	/**
	 * 根据指定配置文件示例File类型对象取得数据库默认配置实例
	 * 
	 * @param file
	 *            配置文件示例File类型对象
	 * @throws ConfigureFileNotFoundException
	 *             配置文件没有找到异常
	 * @return Configurator 数据库连接属性配置实例
	 */
	public static Configurator getInstance(File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ConfigureFileNotFoundException("数据库配置文件未能找到",
					e.fillInStackTrace());
		}
		return getInstance(in);
	}

	/**
	 * 根据外部输入流取得数据库默认配置实例
	 * 
	 * @param in
	 *            输入流
	 * @throws ConfigureFileNotFoundException
	 *             配置文件没有找到异常
	 * @return Configurator 数据库连接属性配置实例
	 */
	public static Configurator getInstance(InputStream in) {
		if (configurator == null) {
			configurator = new Configurator();
		}
		try {
			configurator.load(in);
		} catch (IOException e) {
			configurator = null;
			throw new ConfigureFileNotFoundException("数据库配置文件未能找到",
					e.fillInStackTrace());
		}
		return configurator;
	}

	/**
	 * 根据已有的配置示例取得数据库默认配置实例
	 * 
	 * @param config
	 *            已有的配置示例
	 * @return Configurator 数据库连接属性配置实例
	 */
	public static Configurator getInstance(Configurator config) {
		if (configurator == null) {
			configurator = new Configurator(config);
		}
		return configurator;
	}

}
