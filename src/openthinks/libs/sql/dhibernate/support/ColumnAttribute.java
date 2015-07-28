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
 * @Title: ColumnAttribute.java 
 * @Package openthinks.libs.sql.dhibernate.support 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-9
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support;

/**
 * The information which holds the attribute name and column name in entity and table
 * @author dailey
 *
 */
public class ColumnAttribute {
	private String columnName;
	private String attributeName;
	private IDType idType;

	/**
	 * 
	 */
	public ColumnAttribute() {
	}

	/**
	 * @param columnName
	 * @param attributeName
	 */
	public ColumnAttribute(String columnName, String attributeName) {
		super();
		this.columnName = columnName;
		this.attributeName = attributeName;
	}

	/**
	 * @param idType
	 *            the idType to set
	 */
	public void setIdType(IDType idType) {
		this.idType = idType;
	}

	/**
	 * @return the idType
	 */
	public IDType getIdType() {
		return idType;
	}

	public boolean isKey() {
		return idType == null ? false : true;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ColumnAttribute))
			return false;
		ColumnAttribute other = (ColumnAttribute) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		return true;
	}

}