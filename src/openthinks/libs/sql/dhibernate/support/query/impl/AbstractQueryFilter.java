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
 * @Title: AbstractQueryFilter.java 
 * @Package openthinks.libs.sql.dhibernate.support.query.impl
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query.impl;

import openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import openthinks.libs.sql.dhibernate.support.query.Relativization;
import openthinks.libs.sql.dhibernate.support.template.SQLDialectUtils;
import openthinks.libs.sql.entity.ColumnAttribute;
import openthinks.libs.sql.lang.reflect.ReflectEngine;
import openthinks.libs.utilities.Checker;

/**
 * the abstract implementation for {@link QueryFilter}
 * @author minjdai
 * 
 */
public abstract class AbstractQueryFilter<E extends QueryFilter> implements QueryFilter, Relativization {

	private Class<?> filterClass;
	private String filterName;
	protected QueryFilter appenedFilter;

	public AbstractQueryFilter() {
	}

	public AbstractQueryFilter(Class<?> filterClass) {
		super();
		this.filterClass = filterClass;
	}

	public AbstractQueryFilter(Class<?> filterClass, String filterName) {
		super();
		this.filterClass = filterClass;
		this.filterName = filterName;
	}

	/**
	 * @return the queryFilters
	 */
	public Class<?> getFilterClass() {
		return this.filterClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends QueryFilter> T append(QueryFilter filter) {
		if (this.appenedFilter == null) {
			this.appenedFilter = filter;
		} else {
			this.appenedFilter.append(filter);
		}

		return (T) this;
	}

	/**
	 * get the filter name, it represent the attribute name in entity class
	 * @return the filterName
	 */
	protected String getFilterName() {
		return filterName;
	}

	/**
	 * set the filter name, it represent the attribute name in entity class
	 * @param attributeName String
	 * @return E QueryFilter
	 */
	@SuppressWarnings("unchecked")
	public E filter(String attributeName) {
		this.filterName = attributeName;
		return (E) this;
	}

	/**
	 * @return the appenedFilter
	 */
	@Override
	public QueryFilter next() {
		return appenedFilter;
	}

	@Override
	public boolean hasNext() {
		return next() == null ? false : true;
	}

	/**
	 * set entity class
	 * @param clz Class
	 * @return QueryFilter
	 */
	@SuppressWarnings("unchecked")
	public E filterClass(Class<?> clz) {
		this.filterClass = clz;
		return (E) this;
	}

	/**
	 * generate the SQL include filter name 
	 * @return StringBuffer
	 */
	protected StringBuffer getSQLPart() {
		String filterName = getFilterName();
		Class<?> filterClass = getFilterClass();
		Checker.require(filterName).notNull();
		Checker.require(filterClass).notNull();
		ColumnAttribute columnAttribute = ReflectEngine.parseEntityClass(filterClass).findByAttribute(filterName);
		StringBuffer buffer = new StringBuffer();
		String wrappedColumnName = SQLDialectUtils.wrapColumnName(columnAttribute.getColumnName());
		buffer.append(wrappedColumnName);
		return buffer;
	}

}
