package com.openthinks.libs.sql.dhibernate.support.query.impl;

import com.openthinks.libs.utilities.Checker;

public class DirectFilter extends AbstractQueryFilter<DirectFilter> {
	private Object[] parameters;

	public DirectFilter() {
		super();
	}

	public DirectFilter(Class<?> filterClass, String filterName) {
		super(filterClass, filterName);
	}

	public DirectFilter(Class<?> filterClass) {
		super(filterClass);
	}

	public DirectFilter params(Object[] values) {
		this.parameters = values;
		return this;
	}

	@Override
	public StringBuffer toSQL() {
		StringBuffer buffer = getSQLPart();
		return buffer;
	}

	@Override
	public Object[] parameters() {
		if (parameters == null) {
			return new Object[0];
		}
		return parameters;
	}

	@Override
	protected StringBuffer getSQLPart() {
		String filterName = getFilterName();
		Checker.require(filterName).notNull();
		StringBuffer buffer = new StringBuffer();
		buffer.append(filterName);
		return buffer;
	}
}
