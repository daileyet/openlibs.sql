package com.openthinks.libs.sql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.openthinks.libs.sql.dhibernate.support.query.Filters;
import com.openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import com.openthinks.libs.sql.dhibernate.support.query.Relativization;
import com.openthinks.libs.sql.dhibernate.support.query.impl.DirectFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.QueryFilterGroup;
import com.openthinks.libs.sql.dhibernate.support.template.FilterTemplate;
import com.openthinks.libs.sql.lang.reflect.ReflectEngine;

public class FilterSqlTemplateTest2 {
	Class<?> queryObjectType;
	QueryFilter firstFilter;
	Relativization relativization;

	@Before
	public void setUp() {
		queryObjectType = MessageJPA.class;
	}

	@Test
	public void testQueryFilter() {

		relativization = Filters.eq("").filter("messageId").filterClass(MessageJPA.class);
		String actual = relativization.toSQL().toString();
		String expected = "`message_id` = ?";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase().trim());

		relativization = Filters.eq(null).filter("messageId").filterClass(MessageJPA.class);
		actual = relativization.toSQL().toString();
		expected = "`message_id` is null";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase().trim());
	}

	@Test
	public void testQueryFilterGroup() {
		QueryFilterGroup group = Filters.group();
		group.push(Filters.eq("").filter("messageId"));
		group.push(Filters.or());
		group.push(Filters.eq("").filter("messageId"));
		this.firstFilter = group;

		FilterTemplate template = ReflectEngine.createSQLTemplate(queryObjectType, this.firstFilter);
		String actual = template.generateSQL();
		String expected = "select * from `message` where  ( `message_id` = ?  or `message_id` = ?  ) ";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());
	}

	@Test
	public void testQueryFilterGroupEmbed() {
		QueryFilterGroup group = Filters.group();
		group.push(Filters.eq("messageId", ""));
		group.push(Filters.or());
		this.firstFilter = group;
		QueryFilterGroup embedGrp = Filters.group();
		embedGrp.push(Filters.eq("messageId", ""));
		group.push(embedGrp);

		FilterTemplate template = ReflectEngine.createSQLTemplate(queryObjectType, this.firstFilter);
		String actual = template.generateSQL();
		String expected = "select * from `message` where  ( `message_id` = ?  or  ( `message_id` = ?  )  ) ";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());
	}

	@Test
	public void testDirectQueryFilter() {
		DirectFilter qf = new DirectFilter();
		this.firstFilter = qf.filter("limit ? , ?").params(new String[] { "5", "10" });
		FilterTemplate template = ReflectEngine.createSQLTemplate(queryObjectType, this.firstFilter);

		String actual = template.generateSQL();
		String expected = "select * from `message` where limit ? , ?";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());

	}
}
