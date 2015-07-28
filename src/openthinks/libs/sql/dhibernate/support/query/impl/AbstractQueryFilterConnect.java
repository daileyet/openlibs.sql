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
 * @Title: AndConnect.java 
 * @Package openthinks.libs.sql.dhibernate.support.query.impl 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-4
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query.impl;

import openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import openthinks.libs.sql.dhibernate.support.query.QueryFilterConnect;
import openthinks.libs.sql.dhibernate.support.query.Relativization;

public abstract class AbstractQueryFilterConnect implements QueryFilterConnect, Relativization {
	private QueryFilter appenedFilter;

	/**
	 * @return the appenedFilter
	 */
	@Override
	public QueryFilter next() {
		return appenedFilter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends QueryFilter> T append(QueryFilter filter) {
		if (appenedFilter == null) {
			this.appenedFilter = filter;
		} else {
			this.appenedFilter.append(filter);
		}
		return (T) this;
	}

	/**
	 * get last appended {@link QueryFilter}, it will iterate the appended filter, until no next filter appended to it
	 * @return QueryFilter
	 */
	protected QueryFilter getLastAppendedFilter() {
		QueryFilter tmp = this.appenedFilter;
		if (tmp == null)
			return null;
		while (tmp.hasNext()) {
			tmp = tmp.next();
		}
		return tmp;
	}

	@Override
	public boolean hasNext() {
		return next() == null ? false : true;
	}

	@Override
	public StringBuffer toSQL() {
		return new StringBuffer(toString());
	}

	@Override
	public Object[] parameters() {
		return new Object[0];
	}

}