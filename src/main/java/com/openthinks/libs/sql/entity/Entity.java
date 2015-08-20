package com.openthinks.libs.sql.entity;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.openthinks.libs.sql.data.AbstractRow;
import com.openthinks.libs.sql.dhibernate.Session;
import com.openthinks.libs.sql.entity.key.IDType;
import com.openthinks.libs.sql.exception.EntityReflectException;
import com.openthinks.libs.utilities.Converter;

/**
 * 对应数据库表的实体类
 * The simple entity which reference the table in database;<BR>
 * required:<BR>
 * <ul>
 * 	<li>the field or property name in this entity need same as the column name in database table</li>
 * 	<li>the first attribute in this entity map to the primary key column in database table</li>
 *  <li>the entity class name need same as table name while call {@link Session} high level API</li>
 * </ul>
 * @author dmj
 */
public abstract class Entity extends AbstractRow {
	/**
	 * 实体类对象的属性名称Map
	 */
	private static Map<Class<? extends Entity>, ColumnAttributeMapping> entityMetaDataCache = new ConcurrentHashMap<>();

	private final Map<String, PropertyDescriptor> propertyDescMap = new HashMap<>();

	/**
	 * 返回所有属性的名称 子类无需重写该方法
	 * 
	 * @return ColumnAttribute[] 列名称字符串数组
	 */
	@Override
	public ColumnAttribute[] getColumnAttributes() {
		ColumnAttributeMapping columnAttributeMapping = entityMetaDataCache.get(this.getClass());
		if (columnAttributeMapping == null) {
			columnAttributeMapping = new ColumnAttributeMapping();
			try {
				Field[] fields = this.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					String propertyName = fields[i].getName();
					ColumnAttribute columnAttribute = new ColumnAttribute(propertyName, propertyName);
					if (i == 0) {
						columnAttribute.setIdType(IDType.MANUAL);
						Class<?> type = fields[i].getType();
						if (type == int.class || type == long.class || type == Integer.class || type == Long.class) {
							columnAttribute.setIdType(IDType.AUTO);
						}
					}
					columnAttributeMapping.map(columnAttribute);
				}
				entityMetaDataCache.put(this.getClass(), columnAttributeMapping);
			} catch (SecurityException e) {
				throw new EntityReflectException("属性字段安全", e.getCause());
			} catch (Exception e) {
				throw new EntityReflectException("未能反射到属性字段", e.getCause());
			}
		}
		return columnAttributeMapping.toArray();
	}

	protected PropertyDescriptor getPropertyDescriptor(String propertyName) throws IntrospectionException {
		PropertyDescriptor propertyDescriptor = propertyDescMap.get(propertyName);
		if (propertyDescriptor == null) {
			propertyDescriptor = new PropertyDescriptor(propertyName, getClass());
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
		try {//get property value directly by access the filed
			Field filed = this.getClass().getDeclaredField(propertyName);
			filed.setAccessible(true);
			ret = filed.get(this);
		} catch (Exception e) {
			isThrow = true;
		}
		if (isThrow) {
			try {//get property value by method
				PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName);
				Method method = propertyDescriptor.getReadMethod();
				ret = method.invoke(this);
			} catch (Exception e) {
				throw new EntityReflectException(propertyName + "属性", e.getCause());
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
		try {//set property value directly by access the filed
			Field filed = getClass().getDeclaredField(propertyName);
			filed.setAccessible(true);
			filed.set(this, Converter.source(e).convertToSingle(filed.getType()));
		} catch (Exception ex) {
			// throw new
			// EntityReflectException(ReflectEntity.getSetMethodName(propertyName)
			// + "方法", ex.getCause());
			isThrow = true;
		}
		if (isThrow) {
			try {
				PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName);
				Method method = propertyDescriptor.getWriteMethod();
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes != null && parameterTypes.length == 1) {
					method.invoke(this, Converter.source(e).convertToSingle(parameterTypes[0]));
				} else {
					method.invoke(this, e);
				}
			} catch (Exception ex) {
				throw new EntityReflectException(propertyName + "属性", ex.getCause());
			}
		}
	}

}
