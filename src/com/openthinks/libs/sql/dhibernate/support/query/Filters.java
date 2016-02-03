/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
* @Title: Filters.java 
* @Package openthinks.libs.sql.dhibernate.support.query 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date 2015-7-30
* @version V1.0   
*/
package com.openthinks.libs.sql.dhibernate.support.query;

import com.openthinks.libs.sql.dhibernate.support.query.impl.AbstractQueryFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.ContainerFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.EqualsFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.NotEqualsFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.QueryFilterConnects;
import com.openthinks.libs.sql.dhibernate.support.query.impl.QueryFilterGroup;

/**
 * The helper for implemented {@link QueryFilter}
 * @author dailey.yet@outlook.com
 *
 */
public final class Filters {

	public static AbstractQueryFilter<EqualsFilter> eq(Object value) {
		return new EqualsFilter().eq(value);
	}

	public static AbstractQueryFilter<NotEqualsFilter> neq(Object value) {
		return new NotEqualsFilter().neq(value);
	}

	public static AbstractQueryFilter<ContainerFilter> include(Object value) {
		return new ContainerFilter().include(value);
	}

	public static AbstractQueryFilter<ContainerFilter> startWith(Object value) {
		return new ContainerFilter().startWith(value);
	}

	public static AbstractQueryFilter<ContainerFilter> endWith(Object value) {
		return new ContainerFilter().endWith(value);
	}

	public static QueryFilter eq(String attributeName, Object value) {
		return new EqualsFilter(null, attributeName).eq(value);
	}

	public static QueryFilter neq(String attributeName, Object value) {
		return new NotEqualsFilter(null, attributeName).neq(value);
	}

	public static QueryFilter include(String attributeName, Object value) {
		return new ContainerFilter(null, attributeName).include(value);
	}

	public static QueryFilter startWith(String attributeName, Object value) {
		return new ContainerFilter(null, attributeName).startWith(value);
	}

	public static QueryFilter endWith(String attributeName, Object value) {
		return new ContainerFilter(null, attributeName).endWith(value);
	}

	public static QueryFilterConnect or() {
		return QueryFilterConnects.or();
	}

	public static QueryFilterConnect and() {
		return QueryFilterConnects.and();
	}

	public static QueryFilterGroup group() {
		return new QueryFilterGroup();
	}
}
