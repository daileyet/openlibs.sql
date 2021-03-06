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
 * @Title: Query.java 
 * @Package openthinks.libs.sql.dhibernate.support.query 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package com.openthinks.libs.sql.dhibernate.support.query;

import java.util.List;

/**
 * <B>SELECT</B> SQL statement builder and executor interface
 * @param <T> the entity class
 * @author minjdai
 */
public interface Query<T> {

	/**
	 * set query entity class
	 * @param clz Class entity class
	 * @return Query
	 */
	public Query<T> queryObject(Class<T> clz);

	/**
	 * append the filter on this entity class
	 * @param filter {@link QueryFilter}
	 * @return Query
	 */
	public Query<T> addFilter(QueryFilter... filter);

	/**
	 * generate the query SQL and executed;fetch the result as entity List
	 * @return List
	 */
	public List<T> execute();

}
