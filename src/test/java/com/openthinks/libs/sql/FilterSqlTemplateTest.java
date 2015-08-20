package com.openthinks.libs.sql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import com.openthinks.libs.sql.dhibernate.support.query.Relativization;
import com.openthinks.libs.sql.dhibernate.support.query.impl.EqualsFilter;
import com.openthinks.libs.sql.dhibernate.support.query.impl.OrFilterConnect;
import com.openthinks.libs.sql.dhibernate.support.query.impl.QueryFilterGroup;
import com.openthinks.libs.sql.dhibernate.support.template.FilterTemplate;
import com.openthinks.libs.sql.lang.reflect.ReflectEngine;

public class FilterSqlTemplateTest {
	Class<?> queryObjectType;
	QueryFilter firstFilter;
	Relativization relativization;

	@Before
	public void setUp() {
		queryObjectType = MessageJPA.class;
	}

	@Test
	public void testQueryFilter() {
		relativization = new EqualsFilter(queryObjectType).filter("messageId").eq("");
		String actual = relativization.toSQL().toString();
		String expected = "`message_id` = ?";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase().trim());

		relativization = new EqualsFilter(queryObjectType).filter("messageId");
		actual = relativization.toSQL().toString();
		expected = "`message_id` is null";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase().trim());
	}

	@Test
	public void testQueryFilterGroup() {
		QueryFilterGroup group = new QueryFilterGroup();
		group.push(new EqualsFilter().filter("messageId").eq(""));
		group.push(new OrFilterConnect());
		group.push(new EqualsFilter().filter("messageId").eq(""));
		this.firstFilter = group;

		FilterTemplate template = ReflectEngine.createSQLTemplate(queryObjectType, this.firstFilter);
		String actual = template.generateSQL();
		String expected = "select * from `message` where  ( `message_id` = ?  or `message_id` = ?  ) ";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());
	}

	@Test
	public void testQueryFilterGroupEmbed() {
		QueryFilterGroup group = new QueryFilterGroup();
		group.push(new EqualsFilter().filter("messageId").eq(""));
		group.push(new OrFilterConnect());
		this.firstFilter = group;
		QueryFilterGroup embedGrp = new QueryFilterGroup();
		embedGrp.push(new EqualsFilter().filter("messageId").eq(""));
		group.push(embedGrp);

		FilterTemplate template = ReflectEngine.createSQLTemplate(queryObjectType, this.firstFilter);
		String actual = template.generateSQL();
		String expected = "select * from `message` where  ( `message_id` = ?  or  ( `message_id` = ?  )  ) ";
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());
	}
}
