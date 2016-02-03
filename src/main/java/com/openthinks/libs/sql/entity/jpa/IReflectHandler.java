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
 * @Title: IReflectHandler.java 
 * @Package openthinks.libs.sql.lang.reflect 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-5
 * @version V1.0 
 */
package com.openthinks.libs.sql.entity.jpa;

import com.openthinks.libs.sql.entity.ColumnAttributeMapping;

/**
 * The interface to handle different entity object
 * @author dailey
 *
 */
public interface IReflectHandler {

	/**
	 * parse entity class to {@link ColumnAttributeMapping}
	 * @param entityClass Class<BR>
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @param <T> entity class type
	 * @return {@link ColumnAttributeMapping}
	 */
	public <T> ColumnAttributeMapping parseEntityClass(Class<T> entityClass);

	/**
	 * set column value to corresponding entity object
	 * @param entity entity instance
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @param columnName 	column name in database table
	 * @param columnValue	column name value in database table row
	 * @param <T> entity class type
	 * @return boolean handle success or not
	 */
	public <T> boolean handColumnField(T entity, String columnName, Object columnValue);

	/**
	 * get entity corresponding table name
	 * @param entityClazz Class
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @param <T> entity class type
	 * @return String table name
	 */
	public <T> String getEntityTableName(Class<T> entityClazz);

	/**
	 * get entity corresponding table primary key
	 * @param entityClazz Class
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @param <T> entity class type
	 * @return String primary key name
	 */
	public <T> String getEntityIDName(Class<T> entityClazz);

}