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
 * @Title: Template.java 
 * @Package sql.dhibernate.support 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-8
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support;

/**
 * 
 * Template for standard SQL
 * @author dailey
 *
 */
public interface Template {

	/**
	 * set the template's type<BR>
	 * 1. {@link SQLType.SAVE} : insert/update sql<BR>
	 * 2. {@link SQLType.QUERY}: select sql
	 * @param type SQLType
	 */
	void setType(SQLType type);

	/**
	 * set the data for the template sql values part
	 * @param entityData
	 */
	<T> void setData(T entityData);

	/**
	 * generate standard sql by {@link SQLType} and entity data
	 * @return String
	 */
	String generateSQL();

}
