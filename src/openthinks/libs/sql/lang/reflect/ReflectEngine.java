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
 * @Title: ReflectEngine.java 
 * @Package sql.lang.reflect 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-5
 * @version V1.0 
 */
package openthinks.libs.sql.lang.reflect;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import openthinks.libs.sql.dhibernate.support.ColumnAttributeMapping;
import openthinks.libs.sql.dhibernate.support.FilterTemplate;
import openthinks.libs.sql.dhibernate.support.StandardSQLTemplate;
import openthinks.libs.sql.dhibernate.support.Template;
import openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import openthinks.libs.sql.dhibernate.support.query.impl.FilterSQLTemplate;
import openthinks.libs.sql.entity.Entity;
import openthinks.libs.sql.entity.jpa.EntityReflectHandler;
import openthinks.libs.sql.entity.jpa.IReflectHandler;
import openthinks.libs.sql.entity.jpa.JPAReflectHandler;

/**
 * The engine of reflect entity, its property, mapped table and sql template
 * @author dailey
 * 
 */
public abstract class ReflectEngine {

	private static Map<Class<?>, IReflectHandler> map = new ConcurrentHashMap<Class<?>, IReflectHandler>();
	static {
		register(Entity.class, new EntityReflectHandler());
		register(Object.class, new JPAReflectHandler());
	}

	/**
	 * register new {@link IReflectHandler} to bind given parameter entityClass
	 * @param entityClass Class<?>
	 * @param hander IReflectHandler
	 */
	public static void register(Class<?> entityClass, IReflectHandler hander) {
		map.put(entityClass, hander);
	}

	/**
	 * set column value to corresponding entity object
	 * @param entity entity instance
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @param columnName 	column name in database table
	 * @param columnValue	column name value in database table row
	 * @return boolean handle success or not
	 */
	public static <T> boolean propertyReflect(T entity, String columnName, Object columnValue) {
		boolean isSuccess = false;
		for (Entry<Class<?>, IReflectHandler> entry : map.entrySet()) {
			Class<?> clzz = entry.getKey();
			if (clzz.isInstance(entity)) {
				try {
					isSuccess = entry.getValue().handColumnField(entity, columnName, columnValue);
				} catch (Exception e) {
					continue;
				}
				if (isSuccess)
					break;
			}
		}
		return isSuccess;
	}

	/**
	 * get entity corresponding table name
	 * @param entityClazz Class<T>
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @return String table name
	 */
	public static <T> String getEntityTable(Class<T> entityClazz) {
		String tableName = null;
		for (Entry<Class<?>, IReflectHandler> entry : map.entrySet()) {
			Class<?> clzz = entry.getKey();
			if (clzz.isAssignableFrom(entityClazz)) {
				try {
					tableName = entry.getValue().getEntityTableName(entityClazz);
				} catch (Exception e) {
					continue;
				}
				if (tableName != null)
					break;
			}
		}
		return tableName;
	}

	/**
	 * get entity corresponding table primary key
	 * @param entityClazz Class<T>
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @return String primary key name
	 */
	public static <T> String getEntityID(Class<T> entityClazz) {
		String idName = null;
		for (Entry<Class<?>, IReflectHandler> entry : map.entrySet()) {
			Class<?> clzz = entry.getKey();
			if (clzz.isAssignableFrom(entityClazz)) {
				try {
					idName = entry.getValue().getEntityIDName(entityClazz);
				} catch (Exception e) {
					continue;
				}
				if (idName != null)
					break;
			}
		}
		return idName;
	}

	/**
	 * parse entity class to {@link ColumnAttributeMapping}
	 * @param entityClass Class<T><BR>
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @return {@link ColumnAttributeMapping}
	 */
	public static <T> ColumnAttributeMapping parseEntityClass(Class<T> entityClass) {
		ColumnAttributeMapping columnAttributeMapping = null;
		for (Entry<Class<?>, IReflectHandler> entry : map.entrySet()) {
			Class<?> clzz = entry.getKey();
			if (clzz.isAssignableFrom(entityClass)) {
				columnAttributeMapping = entry.getValue().parseEntityClass(entityClass);
			}
		}
		return columnAttributeMapping;
	}

	/**
	 * create template for standard sql by entity class<BR>
	 * 1. JPA annotation on param entityClass<BR>
	 * 2. subclass from {@link Entity}
	 * 
	 * @param entityClass
	 *            Class<T>
	 * @return Template
	 */
	public static <T> Template createSQLTemplate(Class<T> entityClass) {
		Template template = null;
		ColumnAttributeMapping columnAttributeMapping = parseEntityClass(entityClass);
		template = new StandardSQLTemplate(columnAttributeMapping, entityClass);
		return template;
	}

	//TODO
	public static <T> FilterTemplate createSQLTemplate(Class<T> entityClass, QueryFilter filter) {
		FilterTemplate template = null;
		ColumnAttributeMapping columnAttributeMapping = parseEntityClass(entityClass);
		template = new FilterSQLTemplate(columnAttributeMapping, entityClass);
		template.setFilter(filter);
		return template;
	}
}
