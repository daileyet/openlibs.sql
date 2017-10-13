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
 * @Title: SQLDialectUtils.java 
 * @Package openthinks.libs.sql.dhibernate.support.template
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-2
 * @version V1.0 
 */
package com.openthinks.libs.sql.dhibernate.support.template;

import com.openthinks.libs.sql.lang.Configurator;
import com.openthinks.libs.sql.lang.ConfiguratorFactory;

/**
 * The helper for SQL dialect
 * @author minjdai
 */
public final class SQLDialectUtils {
	/**
	 * wrap database column name with different database dialect 
	 * @param columnName String column name in table
	 * @return String
	 */
	public static String wrapColumnName(String columnName) {
		String retVal = columnName;
		Configurator configurator = ConfiguratorFactory.getDefaultInstance();
		if (configurator != null) {
			switch (configurator.getDialect()) {
			case MYSQL:
				retVal = "`" + columnName + "`";
				break;
			case ORACLE:
			case DERBY:
				retVal = "\"" + columnName + "\"";
				break;
			case SQLSERVER:
				retVal = "[" + columnName + "]";
				break;
			}
		}
		return retVal;
	}

	/**
	 * wrap database column value with different database dialect 
	 * @param value Object
	 * @return String
	 */
	public static String wrapColumnValue(Object value) {
		String retVale = null;
		if (value != null) {
			retVale = "'" + value + "'";
		}
		return retVale;
	}
}
