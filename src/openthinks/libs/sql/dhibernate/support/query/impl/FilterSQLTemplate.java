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
 * @Title: FilterSQLTemplate.java 
 * @Package openthinks.libs.sql.dhibernate.support.query.impl
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-2
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import openthinks.libs.sql.dhibernate.support.ColumnAttributeMapping;
import openthinks.libs.sql.dhibernate.support.FilterTemplate;
import openthinks.libs.sql.dhibernate.support.SQLType;
import openthinks.libs.sql.dhibernate.support.StandardSQLTemplate;
import openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import openthinks.libs.sql.dhibernate.support.query.QueryFilterConnect;
import openthinks.libs.sql.dhibernate.support.query.Relativization;
import openthinks.libs.sql.exception.IllegalQueryFilterException;

/**
 * The implementation of {@link FilterTemplate} by standard SQL
 * @author minjdai
 * 
 */
public class FilterSQLTemplate extends StandardSQLTemplate implements FilterTemplate {
	private QueryFilter queryFilter;

	public FilterSQLTemplate(ColumnAttributeMapping columnAttributeMapping, Class<?> entityType) {
		super(columnAttributeMapping, entityType);
		setType(SQLType.QUERY);
	}

	/**
	 * process the first head QueryFilter and iterate the next element;<BR>
	 * it will go to add default QueryFilterConnect between two AbstractQueryFilter;<BR>
	 * after the above operations, it will return the last element QueryFilter.  
	 * @param first QueryFilter the head of QueryFilter
	 * @return QueryFilter
	 */
	protected QueryFilter getProcessNext(QueryFilter first) {
		QueryFilter next = null;
		if (first == null)
			return next;

		if (first instanceof QueryFilterGroup) {//process group add query filter connect between abstract query filters
			processGroup((QueryFilterGroup) first);
		}

		while (first.hasNext()) {//iterate the next element
			next = first.next();
			if (first instanceof QueryFilterConnect && next instanceof QueryFilterConnect)
				throw new IllegalQueryFilterException();

			// add AndFilterConnect between AbstractQueryFilters
			if (!(first instanceof QueryFilterConnect) && !(next instanceof QueryFilterConnect)) {
				QueryFilter qf = QueryFilterConnects.and().append(next);
				((AbstractQueryFilter<?>) first).appenedFilter = qf;
			}
			first = next;

			if (first instanceof QueryFilterGroup) {
				processGroup((QueryFilterGroup) first);
			}

		}
		return next;
	}

	/**
	 * process the QueryFilterGroup element;<BR>
	 * it will go to add default QueryFilterConnect between two AbstractQueryFilter in this group;<BR>
	 * @param filterGroup QueryFilterGroup
	 */
	protected void processGroup(QueryFilterGroup filterGroup) {
		final List<QueryFilter> filters = filterGroup.getQueryFilters();
		LinkedList<QueryFilter> filtersTemp = new LinkedList<QueryFilter>();
		for (int index = 0, next_index = index + 1, count = filters.size(); index < count; index++) {
			QueryFilter e = filters.get(index);
			QueryFilter next_e = next_index < count ? filters.get(next_index) : null;
			getProcessNext(e);
			filtersTemp.add(e);
			//only add default AndFilterConnect when current and next element are both AbstractQueryFilter
			if (e instanceof AbstractQueryFilter && next_e instanceof AbstractQueryFilter) {
				filtersTemp.add(QueryFilterConnects.and());
			}
		}
		if (filtersTemp.getLast() instanceof QueryFilterConnect) {
			filtersTemp.removeLast();
		}
		filters.clear();
		filters.addAll(filtersTemp);
	}

	@Override
	public String generateSQL() {
		if (this.queryFilter == null) {
			return super.generateSQL();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.generateSQL());
		buffer.append(" WHERE ");
		QueryFilter first = this.queryFilter;

		getProcessNext(first);

		first = this.queryFilter;
		for (;;) {
			if (first instanceof AbstractQueryFilter) {
				((AbstractQueryFilter<?>) first).filterClass(this.entityType);
			}
			if (first instanceof Relativization) {
				Relativization relativization = (Relativization) first;
				buffer.append(relativization.toSQL());
				List<Object> objectList = Arrays.asList(relativization.parameters());
				parametersCollections.addAll(objectList);
			}
			if (!first.hasNext()) {
				break;
			}
			first = first.next();
		}
		return buffer.toString();
	}

	private final Collection<Object> parametersCollections = new ArrayList<Object>();

	@Override
	public void setFilter(QueryFilter filter) {
		this.queryFilter = filter;
	}

	@Override
	public Object[] getParameters() {
		return parametersCollections.toArray(new Object[0]);
	}

}
