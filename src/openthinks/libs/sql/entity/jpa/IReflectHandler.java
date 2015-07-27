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
 * @Package sql.lang.reflect 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-5
 * @version V1.0 
 */
package openthinks.libs.sql.entity.jpa;

import openthinks.libs.sql.dhibernate.support.ColumnAttributeMapping;

/**
 * @author dailey
 *
 */
public interface IReflectHandler {
	
	/**
	 * parse entity class to {@link ColumnAttributeMapping}
	 * @param entityClass Class<T><BR>
	 * 		1. JPA annotation<BR>
	 * 		2. Subclass of {@link Entity}
	 * @return
	 */
	public <T>  ColumnAttributeMapping parseEntityClass(Class<T> entityClass);

	/**
	 * set column value to corresponding entity object
	 * @param entity entity instance
	 * @param columnName 	column name in database table
	 * @param columnValue	column name value in database table row
	 * @return boolean
	 */
	public <T> boolean handColumnField(T entity, String columnName, Object columnValue);

	/**
	 * get entity corresponding table name
	 * @param entityClazz Class<T>
	 * @return String
	 */
	public <T> String getEntityTableName(Class<T> entityClazz);

	/**
	 * get entity corresponding table primary key
	 * @param entityClazz Class<T>
	 * @return String
	 */
	public <T> String getEntityIDName(Class<T> entityClazz);

}