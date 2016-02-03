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
 * @Title: QueryFilterGroup.java 
 * @Package openthinks.libs.sql.dhibernate.support.query.impl
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package com.openthinks.libs.sql.dhibernate.support.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import com.openthinks.libs.sql.dhibernate.support.query.Relativization;

/**
 * Used for multiple {@link QueryFilter} as bracket;<BR>
 * All children in {@link QueryFilterGroup} will be as a whole, "<B>()</B>"  bracket pair will wrapped outside.
 * 
 * @author minjdai
 * 
 */
public class QueryFilterGroup extends AbstractQueryFilter<QueryFilterGroup> {

	private final List<QueryFilter> queryFilters = new ArrayList<QueryFilter>();

	public QueryFilterGroup() {
	}

	public QueryFilterGroup(Class<?> filterClass) {
		super(filterClass);
	}

	/**
	 * add the given query filter into this group, as a part of this group<BR>
	 * different from {@link #append(QueryFilter)}
	 * @param filter the child of {@link QueryFilter}
	 * @return QueryFilterGroup
	 */
	public QueryFilterGroup push(QueryFilter filter) {
		if (filter instanceof AbstractQueryFilter) {
			((AbstractQueryFilter<?>) filter).filterClass(this.getFilterClass());
		}
		this.queryFilters.add(filter);

		return this;
	}

	@Override
	public StringBuffer toSQL() {
		StringBuffer buffer = new StringBuffer();
		if (queryFilters.size() > 0) {
			buffer.append(" ( ");
			for (QueryFilter filter : queryFilters) {
				buffer.append(processSingleFilter(filter));
			}
			buffer.append(" ) ");
		}
		return buffer;
	}

	/**
	 * @param filter QueryFilter
	 * @return
	 */
	private Object processSingleFilter(QueryFilter filter) {
		QueryFilter first = filter;
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			if (first instanceof Relativization) {
				if (first instanceof AbstractQueryFilter) {//set filter entity class again
					((AbstractQueryFilter<?>) first).filterClass(this.getFilterClass());
				}
				buffer.append(((Relativization) first).toSQL());
			}
			if (!first.hasNext()) {
				break;
			}
			first = first.next();
		}
		return buffer;
	}

	@Override
	public Object[] parameters() {
		Object[] paramters = new Object[0];
		if (queryFilters.size() > 0) {
			for (QueryFilter filter : queryFilters) {
				Object[] temps = processSingleFilterParameter(filter);
				if (temps == null || temps.length == 0)
					continue;
				Object[] newParameters = Arrays.copyOf(paramters, paramters.length + temps.length);
				System.arraycopy(temps, 0, newParameters, paramters.length, temps.length);
				paramters = newParameters;
			}
		}
		return paramters;
	}

	/**
	 * @param filter
	 * @return
	 */
	private Object[] processSingleFilterParameter(QueryFilter filter) {
		QueryFilter first = filter;
		Object[] paramters = new Object[0];
		for (;;) {
			if (first instanceof Relativization) {
				Object[] temps = ((Relativization) first).parameters();
				if (temps == null || temps.length == 0) {//nothing to do
				} else {
					Object[] newParameters = Arrays.copyOf(paramters, paramters.length + temps.length);
					System.arraycopy(temps, 0, newParameters, paramters.length, temps.length);
					paramters = newParameters;
				}
			}
			if (!first.hasNext()) {
				break;
			}
			first = first.next();
		}
		return paramters;
	}

	/**
	 * @return the queryFilters
	 */
	protected List<QueryFilter> getQueryFilters() {
		return queryFilters;
	}
}
