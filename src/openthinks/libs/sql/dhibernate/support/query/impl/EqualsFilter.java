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
 * @Title: EqualsFilter.java 
 * @Package openthinks.libs.sql.dhibernate.support.query.impl
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query.impl;

/**
 * Equal query filter as <B>=</B> in SQL statement
 * @author minjdai
 * 
 */
public class EqualsFilter extends AbstractQueryFilter<EqualsFilter> {
	private Object eqValue;

	public EqualsFilter() {
	}

	public EqualsFilter eq(Object value) {
		this.eqValue = value;
		return this;
	}

	/**
	 * @return the eqValue
	 */
	Object getEqValue() {

		return eqValue;
	}

	@Override
	public StringBuffer toSQL() {
		StringBuffer buffer = super.getSQLPart();
		if (getEqValue() == null) {
			buffer.append(" IS NULL ");
		} else {
			buffer.append(" = ? ");
		}
		return buffer;
	}

	@Override
	public Object[] parameters() {
		if (getEqValue() != null) {
			return new Object[] { getEqValue() };
		} else {
			return new Object[0];
		}
	}

}
