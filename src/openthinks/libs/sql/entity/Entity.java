package openthinks.libs.sql.entity;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import openthinks.libs.sql.data.AbstractRow;
import openthinks.libs.sql.dhibernate.support.ColumnAttribute;
import openthinks.libs.sql.dhibernate.support.ColumnAttributeMapping;
import openthinks.libs.sql.dhibernate.support.IDType;
import openthinks.libs.sql.exception.EntityReflectException;
import openthinks.libs.utilities.Converter;

/**
 * 对应数据库表的实体类
 * 
 * @author dmj
 */
public abstract class Entity extends AbstractRow {
	/**
	 * 实体类对象的属性名称数组
	 */
	private static String[] propertyNames = null;

	private final Map<String, PropertyDescriptor> propertyDescMap = new HashMap<String, PropertyDescriptor>();

	/**
	 * 返回所有属性的名称 子类无需重写该方法
	 * 
	 * @return String[] 列名称字符串数组
	 */
	@Override
	public ColumnAttribute[] getColumnAttributes() {
		ColumnAttributeMapping columnAttributeMapping = new ColumnAttributeMapping();
		if (propertyNames == null) {
			try {
				Field[] fields = this.getClass().getDeclaredFields();
				propertyNames = new String[fields.length];
				for (int i = 0; i < propertyNames.length; i++) {
					propertyNames[i] = fields[i].getName();
					ColumnAttribute columnAttribute = new ColumnAttribute(
							propertyNames[i], propertyNames[i]);
					if (i == 0) {
						columnAttribute.setIdType(IDType.MANUAL);
						Class<?> type = fields[i].getType();
						if (type == int.class || type == long.class
								|| type == Integer.class || type == Long.class) {
							columnAttribute.setIdType(IDType.AUTO);
						}
					}
					columnAttributeMapping.map(columnAttribute);
				}
			} catch (SecurityException e) {
				throw new EntityReflectException("属性字段安全", e.getCause());
			} catch (Exception e) {
				throw new EntityReflectException("未能反射到属性字段", e.getCause());
			}
		}
		return columnAttributeMapping.toArray();
	}

	protected PropertyDescriptor getPropertyDescriptor(String propertyName)
			throws IntrospectionException {
		PropertyDescriptor propertyDescriptor = propertyDescMap
				.get(propertyName);
		if (propertyDescriptor == null) {
			propertyDescriptor = new PropertyDescriptor(propertyName,
					getClass());
			propertyDescMap.put(propertyName, propertyDescriptor);
		}
		return propertyDescriptor;
	}

	/**
	 * 根据属性名称返回该属性的值<br>
	 * 子类可以重写该方法
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return Object 属性的值
	 */
	@Override
	public Object get(String propertyName) {
		boolean isThrow = false;
		Object ret = null;
		try {
			PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName);
			Method method = propertyDescriptor.getReadMethod();
			ret = method.invoke(this);
		} catch (Exception e) {
			isThrow = true;
		}
		if (isThrow) {
			try {
				Field filed = this.getClass().getDeclaredField(propertyName);
				filed.setAccessible(true);
				ret = filed.get(this);
			} catch (Exception e) {
				throw new EntityReflectException(propertyName + "属性",
						e.getCause());
			}
		}
		return ret;
	}

	/**
	 * 根据属性名称设置该属性的值<br>
	 * 子类可以重写该方法
	 * 
	 * @param propertyName
	 *            设置属性值的属性名
	 * @param e
	 *            该属性的新值
	 */
	@Override
	public void set(String propertyName, Object e) {
		boolean isThrow = false;
		try {
			PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName);
			Method method = propertyDescriptor.getWriteMethod();
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes != null && parameterTypes.length == 1) {
				method.invoke(this,
						Converter.source(e).convertToSingle(parameterTypes[0]));
			} else {
				method.invoke(this, e);
			}
		} catch (Exception ex) {
			// throw new
			// EntityReflectException(ReflectEntity.getSetMethodName(propertyName)
			// + "方法", ex.getCause());
			isThrow = true;
		}
		if (isThrow) {
			try {
				Field filed = getClass().getDeclaredField(propertyName);
				filed.setAccessible(true);
				filed.set(this,
						Converter.source(e).convertToSingle(filed.getType()));
			} catch (Exception ex) {
				throw new EntityReflectException(propertyName + "属性",
						ex.getCause());
			}
		}
	}

}
