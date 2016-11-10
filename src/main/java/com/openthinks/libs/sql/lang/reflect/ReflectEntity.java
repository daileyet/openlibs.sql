package com.openthinks.libs.sql.lang.reflect;

import java.lang.reflect.Method;

/**
 * 数据库实体类反射工具类
 * @author dmj
 * @version  2010/11/16
 */
public class ReflectEntity {

	/**
	 * 根据属性名获取设置该属性的方法名称<br>
	 * 将属性名第一字母大写<br>
	 * 例如:属性名为username 则该方法返回为setUsername<br>
	 * 		  属性名为userPwd 则该方法返回为setUserPwd
	 * @param propertyName 属性名
	 * @return String set方法名
	 */
	public static String getSetMethodName(String propertyName) {
		return "set" + propertyName.substring(0, 1).toUpperCase() + //columnName.toLowerCase().substring(1);
				propertyName.substring(1);
	}

	/**
	 * get property name from the given parameter {@link Method}
	 * @param method {@link Method}
	 * @return property name String
	 */
	public static String getPropertyName(Method method) {
		String methodName = method.getName();
		String propertyName = "";
		if (methodName.length() > 3 && (methodName.startsWith("set") || methodName.startsWith("get"))) {
			propertyName = methodName.substring(3);
		} else if (methodName.length() > 2 && methodName.startsWith("is")) {
			propertyName = methodName.substring(2);
		}
		if (propertyName.length() >= 1) {
			String firstLetter = String.valueOf(propertyName.charAt(0));
			propertyName = propertyName.replaceFirst(firstLetter, firstLetter.toLowerCase());
		}
		return propertyName;
	}

	/**
	 * 根据属性名获取取得该属性值的方法名称<br>
	 * 将属性名第一字母大写<br>
	 * 例如:属性名为username 则该方法返回为getUsername<br>
	 * 		  属性名为userPwd 则该方法返回为getUserPwd
	 * @param propertyName 属性名
	 * @return String get方法名
	 */
	public static String getGetMethodName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase() + //columnName.toLowerCase().substring(1);
				propertyName.substring(1);
	}

	/**
	 * 根据属性名获取取得该属性值的方法名称<br>
	 * 将属性名第一字母大写,<span style="color:red">属性的类型为boolean</span><br>
	 * @param propertyName 属性名
	 * @return String get方法名
	 */
	public static String getIsMethodName(String propertyName) {
		return "is" + propertyName.substring(0, 1).toUpperCase() + //columnName.toLowerCase().substring(1);
				propertyName.substring(1);
	}

}
