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
 * @Title: Queryer.java 
 * @Package openthinks.libs.sql.dhibernate.support.query.impl
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query.impl;

import java.util.List;

import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.dhibernate.support.FilterTemplate;
import openthinks.libs.sql.dhibernate.support.query.Query;
import openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import openthinks.libs.sql.lang.reflect.ReflectEngine;
import openthinks.libs.utilities.Converter;

/**
 * The implementation of {@link Query}
 * @author minjdai
 * 
 */
public class Queryer<T> implements Query<T> {
	private Class<T> queryObjectType;
	//the last query filter element
	private QueryFilter queryFilter;
	//the first query filter element 
	private QueryFilter firstFilter;
	private final Session session;

	/**
	 * initial a instance by given {@link Session}
	 * @param session Session
	 */
	public Queryer(Session session) {
		this.session = session;
	}

	@Override
	public Queryer<T> queryObject(Class<T> clz) {
		this.queryObjectType = clz;
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Query<T> addFilter(QueryFilter... filters) {
		if (filters != null) {
			for (QueryFilter filter : filters) {
				if (filter instanceof AbstractQueryFilter) {
					((AbstractQueryFilter) filter).filterClass(queryObjectType);
				}
				if (this.firstFilter == null) {
					this.firstFilter = filter;
				} else {
					this.queryFilter.append(filter);
				}
				this.queryFilter = filter;
			}
		}
		return this;
	}

	@Override
	public List<T> execute() {
		FilterTemplate template = ReflectEngine.createSQLTemplate(queryObjectType, this.firstFilter);
		String sql = template.generateSQL();
		Object[] parameters_ = template.getParameters();
		String[] parameters = Converter.source(parameters_).convert2Array(String.class);
		return session.list(queryObjectType, sql, parameters);
	}
}
