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
 * @Title: StandardSQLTemplate.java 
 * @Package openthinks.libs.sql.dhibernate.support.template
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-8
 * @version V1.0 
 */
package com.openthinks.libs.sql.dhibernate.support.template;

import static com.openthinks.libs.sql.dhibernate.support.template.SQLDialectUtils.wrapColumnName;
import static com.openthinks.libs.sql.dhibernate.support.template.SQLDialectUtils.wrapColumnValue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import openthinks.libs.utilities.Checker;

import org.apache.log4j.Logger;

import com.openthinks.libs.sql.entity.ColumnAttribute;
import com.openthinks.libs.sql.entity.ColumnAttributeMapping;
import com.openthinks.libs.sql.entity.key.IDType;
import com.openthinks.libs.sql.entity.key.IdGenerator;
import com.openthinks.libs.sql.lang.reflect.ReflectEngine;

/**
 * @author dailey
 * 
 */
public class StandardSQLTemplate implements Template {

	private SQLType type;
	private Object data;
	protected final ColumnAttributeMapping mapping;
	protected final Class<?> entityType;
	private final Logger logger = Logger.getLogger(StandardSQLTemplate.class);

	/**
	 * @param columnAttributeMapping {@link ColumnAttributeMapping}
	 * @param entityType Class entity class
	 */
	public StandardSQLTemplate(final ColumnAttributeMapping columnAttributeMapping, final Class<?> entityType) {
		this.mapping = columnAttributeMapping;
		this.entityType = entityType;
	}

	@Override
	public void setType(SQLType type) {
		this.type = type;
	}

	@Override
	public <T> void setData(T entityData) {
		this.data = entityData;
	}

	@Override
	public String generateSQL() {
		Checker.require(entityType).notNull();
		String entityTableName = ReflectEngine.getEntityTable(entityType);
		switch (type) {
		case QUERY:
			return getSelect(entityTableName);
		case INSERT:
			return getInsert(entityTableName);
		case UPDATE:
			String idName = ReflectEngine.getEntityID(entityType);
			Checker.require(idName).notNull();
			return getUpdate(entityTableName, idName);
		case DELETE:
			String idName_ = ReflectEngine.getEntityID(entityType);
			Checker.require(idName_).notNull();
			return getDelete(entityTableName, idName_);
		}
		return "";
	}

	/**
	 * generate delete action SQL
	 * @param entityTableName String table name
	 * @param idName_	String primary key name
	 * @return the delete action SQL
	 */
	private String getDelete(String entityTableName, String idName_) {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM " + entityTableName);
		ColumnAttribute attribute = mapping.findByColumn(idName_);
		Checker.require(attribute).notNull();
		sb.append(" WHERE " + wrapColumnName(attribute.getColumnName()) + "="
				+ wrapColumnValue(getPropertyValue(attribute.getAttributeName())));
		return sb.toString();
	}

	/**
	 * generate update action SQL
	 * @param entityTableName String table name
	 * @param idName	String primary key name
	 * @return the update action SQL
	 */
	private String getUpdate(String entityTableName, String idName) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE " + entityTableName);
		sb.append(" SET ");
		for (ColumnAttribute ca : mapping) {
			if (ca.getIdType() == null) {
				sb.append(wrapColumnName(ca.getColumnName()));
				sb.append("=");
				sb.append(wrapColumnValue(getPropertyValue(ca.getAttributeName())));
				sb.append(",");
			}
		}
		sb = sb.delete(sb.length() - 1, sb.length());
		ColumnAttribute attribute = mapping.findByColumn(idName);
		Checker.require(attribute).notNull();
		sb.append(" WHERE " + idName + "='" + getPropertyValue(attribute.getAttributeName()) + "'");
		return sb.toString();
	}

	/**
	 * generate insert action SQL
	 * @param entityTableName String table name
	 * @return the insert action SQL
	 */
	private String getInsert(String entityTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO " + entityTableName);
		sb.append("(");
		for (ColumnAttribute ca : mapping) {
			if (ca.getIdType() != IDType.AUTO) {
				String columnName = ca.getColumnName();
				sb.append(wrapColumnName(columnName));
				sb.append(",");
			}
		}
		sb = sb.delete(sb.length() - 1, sb.length());
		sb.append(")");

		sb.append(" VALUES(");
		for (ColumnAttribute ca : mapping) {
			if (ca.getIdType() == IDType.AUTO)// pass when the column is primary key and its generator strategy is auto
				continue;
			Object columnValue = null;
			//get column/attribute value from entity data
			columnValue = getPropertyValue(ca.getAttributeName());
			// set id column when id type is IDType.MANUAL and current value is empty or not setting.
			if (ca.getIdType() == IDType.MANUAL && columnValue == null) {
				columnValue = IdGenerator.getGenerator(this.entityType).generator();
				// set entity id
				ReflectEngine.propertyReflect(data, ca.getColumnName(), columnValue);
			}
			sb.append(wrapColumnValue(columnValue));
			sb.append(",");

		}
		sb = sb.delete(sb.length() - 1, sb.length());
		sb.append(")");
		return sb.toString();
	}

	/**
	 * generate select/query table SQL
	 * @param entityTableName String table name
	 * @return the query SQL
	 */
	private String getSelect(String entityTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM " + wrapColumnName(entityTableName));
		return sb.toString();
	}

	/**
	 * get the property value for the given property name
	 * @param propertyName String property name
	 * @return Object property value
	 */
	protected Object getPropertyValue(String propertyName) {
		Object value = null;
		try {//first try get value by its field directly
			Field field = entityType.getDeclaredField(propertyName);
			field.setAccessible(true);
			value = field.get(data);
		} catch (Exception e) {
			logger.info(e.getMessage());
			value = null;
		}
		if (value == null) {//secondly try get value by its getter method
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, data.getClass());
				return propertyDescriptor.getReadMethod().invoke(data);
			} catch (Exception e) {
				logger.error(e.getMessage());
				value = null;
			}
		}
		return value;
	}
}
