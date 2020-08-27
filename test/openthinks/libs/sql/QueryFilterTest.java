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
 * @Title: QueryFilterTest.java 
 * @Package sql 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-2
 * @version V1.0 
 */
package openthinks.libs.sql;

import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.dhibernate.support.query.Query;
import openthinks.libs.sql.dhibernate.support.query.QueryFilter;
import openthinks.libs.sql.dhibernate.support.query.impl.ContainerFilter;
import openthinks.libs.sql.dhibernate.support.query.impl.EqualsFilter;
import openthinks.libs.sql.dhibernate.support.query.impl.NotEqualsFliter;
import openthinks.libs.sql.dhibernate.support.query.impl.QueryFilterConnects;
import openthinks.libs.sql.dhibernate.support.query.impl.QueryFilterGroup;
import openthinks.libs.sql.dhibernate.support.test.TestSession;

/**
 * @author minjdai
 * 
 */
public class QueryFilterTest {

	void testEqual() {
		Session session = new TestSession();
		Query<Message> query = session.createQuery(Message.class);
		query.addFilter(new EqualsFilter().filter("messageId").eq("123"));
		query.addFilter(new ContainerFilter().filter("content").include("abc"));
		query.addFilter(QueryFilterConnects.or());

		QueryFilter appended1 = new QueryFilterGroup();

		QueryFilter lasted = null;

		lasted = new ContainerFilter().filter("content").startWith("BEGIN");
		appended1.append(lasted);
		lasted = new NotEqualsFliter().filter("messageId").neq("234");
		appended1.append(lasted);
		lasted = new NotEqualsFliter().filter("locale").neq("CN");
		appended1.append(lasted);

		query.addFilter(appended1);
		query.execute();
	}

	public static void main(String[] args) {
		QueryFilterTest tester = new QueryFilterTest();
		tester.testEqual();
	}
}
