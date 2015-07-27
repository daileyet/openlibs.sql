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
 * @Package sql.dhibernate.support 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-8
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support;

import static openthinks.libs.sql.dhibernate.support.SQLDialectUtils.wrapColumnName;
import static openthinks.libs.sql.dhibernate.support.SQLDialectUtils.wrapColumnValue;

import java.beans.PropertyDescriptor;

import openthinks.libs.sql.entity.key.IdGenerator;
import openthinks.libs.sql.lang.reflect.ReflectEngine;
import openthinks.libs.utilities.Checker;

/**
 * @author dailey
 * 
 */
public class StandardSQLTemplate implements Template {

	private SQLType type;
	private Object data;
	protected final ColumnAttributeMapping mapping;
	protected final Class<?> entityType;

	/**
	 * @param columnAttributeMapping
	 */
	public StandardSQLTemplate(
			final ColumnAttributeMapping columnAttributeMapping,
			final Class<?> entityType) {
		this.mapping = columnAttributeMapping;
		this.entityType = entityType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sql.dhibernate.support.Template#setType(sql.dhibernate.support.SQLType)
	 */
	@Override
	public void setType(SQLType type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sql.dhibernate.support.Template#setData(java.lang.Object)
	 */
	@Override
	public <T> void setData(T entityData) {
		this.data = entityData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sql.dhibernate.support.Template#generateSQL()
	 */
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
	 * @param entityTableName
	 * @param idName_
	 * @return
	 */
	private String getDelete(String entityTableName, String idName_) {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM " + entityTableName);
		ColumnAttribute attribute = mapping.findByColumn(idName_);
		Checker.require(attribute).notNull();
		sb.append(" WHERE "
				+ wrapColumnName(attribute.getColumnName())
				+ "="
				+ wrapColumnValue(getPropertyValue(attribute.getAttributeName())));
		return sb.toString();
	}

	/**
	 * @param entityTableName
	 * @return
	 */
	private String getUpdate(String entityTableName, String idName) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE " + entityTableName);
		sb.append(" SET ");
		for (ColumnAttribute ca : mapping) {
			if (ca.getIdType() == null) {
				sb.append(wrapColumnName(ca.getColumnName()));
				sb.append("=");
				sb.append(wrapColumnValue(getPropertyValue(ca
						.getAttributeName())));
				sb.append(",");
			}
		}
		sb = sb.delete(sb.length() - 1, sb.length());
		ColumnAttribute attribute = mapping.findByColumn(idName);
		Checker.require(attribute).notNull();
		sb.append(" WHERE " + idName + "='"
				+ getPropertyValue(attribute.getAttributeName()) + "'");
		return sb.toString();
	}

	/**
	 * @param entityTableName
	 * @return
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
			if (ca.getIdType() == IDType.AUTO)// pass when the column is primary key and its generator strategy
				continue;
			Object columnValue = null;
			//get column/attribute value from entity data
			columnValue = getPropertyValue(ca.getAttributeName());
			// set id column when id type is IDType.MANUAL and current value is empty or not setting.
			if (ca.getIdType() == IDType.MANUAL && columnValue ==null ) {
				columnValue = IdGenerator.getGenerator(this.entityType)
						.generator();
				// set entity id
				ReflectEngine.propertyReflect(data, ca.getColumnName(),
						columnValue);
			} 
			sb.append(wrapColumnValue(columnValue));
			sb.append(",");

		}
		sb = sb.delete(sb.length() - 1, sb.length());
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @param entityTableName
	 * @return
	 */
	private String getSelect(String entityTableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM " + wrapColumnName(entityTableName));
		return sb.toString();
	}

	protected Object getPropertyValue(String propertyName) {
		try {
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
					propertyName, data.getClass());
			return propertyDescriptor.getReadMethod().invoke(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
