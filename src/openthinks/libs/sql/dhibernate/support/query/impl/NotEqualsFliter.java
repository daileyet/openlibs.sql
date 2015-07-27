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
 * @Title: NotEqualsFliter.java 
 * @Package sql.dhibernate.support.query.impl 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-3
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query.impl;

/**
 * @author minjdai
 * 
 */
public class NotEqualsFliter extends AbstractQueryFilter<NotEqualsFliter> {

	private Object neqValue;

	/**
	 * 
	 */
	public NotEqualsFliter() {
	}

	public AbstractQueryFilter<NotEqualsFliter> neq(Object value) {
		this.neqValue = value;
		return this;
	}

	/**
	 * @return the eqValue
	 */
	Object getNeqValue() {

		return neqValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sql.dhibernate.support.query.impl.AbstractQueryFilter#toSQL()
	 */
	@Override
	public StringBuffer toSQL() {
		StringBuffer buffer = super.getSQLPart();
		if (getNeqValue() == null) {
			buffer.append(" IS NOT NULL ");
		} else {
			buffer.append(" <> ? ");
		}
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sql.dhibernate.support.query.impl.Relativization#parameters()
	 */
	@Override
	public Object[] parameters() {
		if (getNeqValue() != null) {
			return new Object[] { getNeqValue() };
		} else {
			return new Object[0];
		}
	}

}
