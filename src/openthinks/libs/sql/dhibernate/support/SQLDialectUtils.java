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
 * @Package sql.dhibernate.support 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-2
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support;

import openthinks.libs.sql.lang.Configurator;
import openthinks.libs.sql.lang.ConfiguratorFactory;

/**
 * @author minjdai
 * 
 */
public final class SQLDialectUtils {
	public static String wrapColumnName(String columnName) {
		String retVal = columnName;
		Configurator configurator = ConfiguratorFactory.getDefaultInstance();
		if (configurator != null) {
			switch (configurator.getDialect()) {
			case MYSQL:
				retVal = "`" + columnName + "`";
				break;
			case ORACLE:
				retVal = "\"" + columnName + "\"";
				break;
			case SQLSERVER:
				retVal = "[" + columnName + "]";
				break;
			}
		}
		return retVal;
	}

	public static String wrapColumnValue(Object value) {
		String retVale = null;
		if (value != null) {
			retVale = "'" + value + "'";
		}
		return retVale;
	}
}
