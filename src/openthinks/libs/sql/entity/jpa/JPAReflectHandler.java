/**
 * Licensed to the Apache Software Foundation (ASF) under one 
 * or more contributor license agreements. See the NOTICE file 
 * distributed with this work for additional information 
 * regarding copyright ownership. The ASF licenses this file 
 * to you under the Apache License, Version 2.0 (the 
 * "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
 * KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations 
 * under the License. 
 * 
 * @Title: JPAReflectHandler.java 
 * @Package sql.entity.jpa 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-6
 * @version V1.0 
 */
package openthinks.libs.sql.entity.jpa;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import openthinks.libs.sql.dhibernate.support.ColumnAttribute;
import openthinks.libs.sql.dhibernate.support.ColumnAttributeMapping;
import openthinks.libs.sql.dhibernate.support.IDType;
import openthinks.libs.sql.lang.reflect.ReflectEntity;
import openthinks.libs.utilities.Converter;

/**
 * Simple implement for JPA Annotation
 * 
 * @author dailey
 * 
 */
public class JPAReflectHandler implements IReflectHandler {

	/**
	 * column name		
	 * 1. from the name of annotation {@link javax.persistence.Column}
	 * 2. if the name of annotation {@link javax.persistence.Column} is empty, use the attribute name
	 * attribute name	
	 * 
	 * @see openthinks.libs.sql.entity.jpa.IReflectHandler#parseEntityClass(java.lang.Class)
	 */
	@Override
	public <T> ColumnAttributeMapping parseEntityClass(Class<T> entityClass) {
		ColumnAttributeMapping columnAttributeMapping = new ColumnAttributeMapping();

		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			Column cloumn = field.getAnnotation(Column.class);
			if (cloumn == null) {
				continue;
			}
			String attributeName = field.getName();
			String _cloumnName = cloumn.name();
			if ("".equals(_cloumnName)) {
				_cloumnName = field.getName();
			}
			ColumnAttribute columnAttribute = new ColumnAttribute(_cloumnName,
					attributeName);
			handIfisKeyColumn(field, columnAttribute);
			columnAttributeMapping.map(columnAttribute);
		}

		Method[] methods = entityClass.getDeclaredMethods();

		for (Method method : methods) {
			Column cloumn = method.getAnnotation(Column.class);
			if (cloumn == null) {
				continue;
			}
			String _cloumnName = cloumn.name();
			String propertyName = ReflectEntity.getPropertyName(method);
			if ("".equals(_cloumnName)) {
				_cloumnName = propertyName;
			}
			ColumnAttribute columnAttribute = new ColumnAttribute(_cloumnName,
					propertyName);
			handIfisKeyColumn(method, columnAttribute);
			columnAttributeMapping.map(columnAttribute);
		}

		return columnAttributeMapping;
	}

	private void handIfisKeyColumn(AccessibleObject field,
			ColumnAttribute columnAttribute) {
		Id id = field.getAnnotation(Id.class);
		if (id != null) {
			columnAttribute.setIdType(IDType.MANUAL);
			GeneratedValue generatedValue = field
					.getAnnotation(GeneratedValue.class);
			if (generatedValue != null) {
				if (GenerationType.AUTO == generatedValue.strategy()) {
					columnAttribute.setIdType(IDType.AUTO);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sql.lang.reflect.IReflectHandler#handField(java.lang.Object,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> boolean handColumnField(T entity, String columnName,
			Object columnValue) {
		if (isMarkEntity(entity.getClass()) == false)
			return false;
		ColumnAttributeMapping columnAttributeMapping = parseEntityClass(entity
				.getClass());
		String attributeName = columnAttributeMapping.findByColumn(columnName)
				.getAttributeName();
		if (attributeName == null)
			return false;
		boolean isSuccess = trySetByField(entity, attributeName, columnValue);
		if (isSuccess == true) {
			return true;
		}
		isSuccess = trySetByMethod(entity, attributeName, columnValue);
		return isSuccess;
	}

	private <T> boolean isMarkEntity(Class<T> clzz) {
		Entity entityAnnotation = clzz.getAnnotation(Entity.class);
		if (entityAnnotation == null) {
			return false;
		}
		return true;
	}

	private <T> boolean trySetByMethod(T entity, String attributeName,
			Object columnValue) {
		try {
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
					attributeName, entity.getClass());
			propertyDescriptor.getWriteMethod().invoke(entity, columnValue);
			return true;
		} catch (Exception e) {
			// ignore
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean trySetByField(T entity, String attributeName,
			Object value) {
		Class<T> clzz = (Class<T>) entity.getClass();
		try {
			Field field = clzz.getDeclaredField(attributeName);
			if (field != null) {
				field.setAccessible(true);
				if (value == null || field.getType() == value.getClass())
					field.set(entity, value);
				else {// if field type is not accepted by value type
					field.set(
							entity,
							Converter.source(value).convertToSingle(
									field.getType()));
				}
				return true;
			}
		} catch (Exception e) {
			// ignore
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sql.entity.jpa.IReflectHandler#getEntityTableName(java.lang.Object)
	 */
	@Override
	public <T> String getEntityTableName(Class<T> entityClazz) {
		String tableName = null;
		if (isMarkEntity(entityClazz) == false)
			return tableName;
		Table tableAnnotation = entityClazz.getAnnotation(Table.class);
		if (tableAnnotation == null) {
			tableName = entityClazz.getSimpleName();
			return tableName;
		}
		tableName = tableAnnotation.name();
		if ("".equals(tableName)) {
			tableName = entityClazz.getSimpleName();
		}
		return tableName;
	}

	@Override
	public <T> String getEntityIDName(Class<T> entityClazz) {
		String idName = null;
		if (isMarkEntity(entityClazz) == false)
			return idName;
		ColumnAttributeMapping columnAttributeMapping = parseEntityClass(entityClazz);
		for (ColumnAttribute columnAttribute : columnAttributeMapping) {
			if (columnAttribute.isKey()) {
				return columnAttribute.getColumnName();
			}
		}
		return idName;
	}

}