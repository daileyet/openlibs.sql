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
 * @Title: ColumnAttributeMapping.java 
 * @Package openthinks.libs.sql.entity 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-9
 * @version V1.0 
 */
package com.openthinks.libs.sql.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The maps between entity property name and  table column name
 * @author dailey
 *
 */
public class ColumnAttributeMapping implements Iterable<ColumnAttribute> {
	private Set<ColumnAttribute> set = new HashSet<>();

	public void map(String columnName, String attributeName) {
		set.add(new ColumnAttribute(columnName, attributeName));
	}

	public void map(ColumnAttribute columnAttribute) {
		set.add(columnAttribute);
	}

	public boolean isEmpty() {
		return set.size() == 0 ? true : false;
	}

	/**
	 * @param columnName String
	 * @return ColumnAttribute
	 */
	public ColumnAttribute findByColumn(String columnName) {
		ColumnAttribute findColumnAttribute = null;
		for (ColumnAttribute columnAttribute : this) {
			if (columnAttribute != null && columnAttribute.getColumnName().equals(columnName)) {
				findColumnAttribute = columnAttribute;
				break;
			}
		}
		return findColumnAttribute;
	}

	/**
	 * find {@link ColumnAttribute} instance by attribute name
	 * @param attributeName attribute name
	 * @return ColumnAttribute
	 */
	public ColumnAttribute findByAttribute(String attributeName) {
		ColumnAttribute findColumnAttribute = null;
		for (ColumnAttribute columnAttribute : this) {
			if (columnAttribute != null && columnAttribute.getAttributeName().equals(attributeName)) {
				findColumnAttribute = columnAttribute;
				break;
			}
		}
		return findColumnAttribute;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ColumnAttribute> iterator() {
		return set.iterator();
	}

	/**
	 * @return ColumnAttribute[]
	 */
	public ColumnAttribute[] toArray() {
		return set.toArray(new ColumnAttribute[0]);
	}

}
