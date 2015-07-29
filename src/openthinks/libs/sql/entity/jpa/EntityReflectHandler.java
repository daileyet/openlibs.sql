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
 * @Title: EntityReflectHandler.java 
 * @Package openthinks.libs.sql.entity.jpa 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-6
 * @version V1.0 
 */
package openthinks.libs.sql.entity.jpa;

import java.lang.reflect.Field;

import openthinks.libs.sql.entity.ColumnAttribute;
import openthinks.libs.sql.entity.ColumnAttributeMapping;
import openthinks.libs.sql.entity.Entity;

import org.apache.log4j.Logger;

/**
 * The simple {@link IReflectHandler} implementation for {@link Entity}
 * @author dailey
 *
 */
public class EntityReflectHandler extends JPAReflectHandler implements IReflectHandler {

	Logger logger = Logger.getLogger(getClass());

	@Override
	public <T> boolean handColumnField(T entity, String columnName, Object columnValue) {
		// TODO Checker.require(entity).isExtendsFrom(Entity.class);
		boolean isSuccess = super.handColumnField(entity, columnName, columnValue);
		if (isSuccess) {
			return true;
		}
		if (entity instanceof Entity) {
			try {
				((Entity) entity).set(columnName, columnValue);
				isSuccess = true;
			} catch (Exception e) {
				isSuccess = false;
			}
		}
		return isSuccess;
	}

	/**
	 * get the table name from the entity class name
	 */
	@Override
	public <T> String getEntityTableName(Class<T> entityClazz) {
		String tableName = super.getEntityTableName(entityClazz);
		if (tableName == null) {
			tableName = entityClazz.getSimpleName();
		}
		return tableName;
	}

	/**
	 * get the first field as primary id in entity class
	 */
	@Override
	public <T> String getEntityIDName(Class<T> entityClazz) {
		String idName = super.getEntityIDName(entityClazz);
		if (idName == null) {
			Field[] fields = entityClazz.getFields();
			if (fields.length > 0) {
				idName = fields[0].getName();
			}
		}
		return idName;
	}

	@Override
	public <T> ColumnAttributeMapping parseEntityClass(Class<T> entityClass) {
		ColumnAttributeMapping columnAttributeMapping = super.parseEntityClass(entityClass);

		if (columnAttributeMapping.isEmpty() && Entity.class.isAssignableFrom(entityClass)) {
			try {
				Entity entityInstance = (Entity) entityClass.newInstance();
				for (ColumnAttribute columnA : entityInstance.getColumnAttributes()) {
					columnAttributeMapping.map(columnA);
				}
			} catch (InstantiationException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			}
		}
		return columnAttributeMapping;
	}

}