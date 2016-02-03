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
 * @Title: QueryFilter.java 
 * @Package openthinks.libs.sql.dhibernate.support.query
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package com.openthinks.libs.sql.dhibernate.support.query;

import com.openthinks.libs.sql.dhibernate.support.query.impl.AbstractQueryFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.AndFilterConnect;

/**
 * @author minjdai
 * 
 */
public interface QueryFilter {

	/**
	 * use {@link AndFilterConnect} as default {@link QueryFilterConnect}
	 * between current instance and the appended instance when appended instance is
	 * not {@link QueryFilterConnect} type
	 * 
	 * @param filter
	 *            QueryFilter
	 * @param T entity class type
	 * @return current QueryFilter instance, not appended instance
	 */
	public <T extends QueryFilter> T append(QueryFilter filter);

	/**
	 * judge has next {@link QueryFilter}
	 * @return boolean
	 */
	public boolean hasNext();

	/**
	 * get appended QueryFilter, could be type {@link QueryFilterConnect} or
	 * {@link AbstractQueryFilter}
	 * 
	 * @return QueryFilter
	 */
	public QueryFilter next();

}
