/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
* @Title: QueryTest.java 
* @Package openthinks.libs.sql 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date 2015年7月30日
* @version V1.0   
*/
package openthinks.libs.sql;

import java.util.List;
import java.util.Locale;

import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.dhibernate.support.SessionFactory;
import openthinks.libs.sql.dhibernate.support.query.Query;
import openthinks.libs.sql.dhibernate.support.query.impl.EqualsFilter;
import openthinks.libs.sql.dhibernate.support.query.impl.NotEqualsFilter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class QueryTest {
	Session session;
	final MessageJPA testMsg = new MessageJPA();
	final MessageJPA testMsg2 = new MessageJPA();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		session = SessionFactory.getSession();
		session.disableAutoClose();
		testMsg.setMessageId("QueryTest_MSG_ID_1000");
		testMsg.setLocale(Locale.CHINA.toString());
		testMsg.setContent("简单测试");

		testMsg2.setMessageId("QueryTest_MSG_ID_1000");
		testMsg2.setLocale(Locale.US.toString());
		testMsg2.setContent("Simple Test");
		session.save(testMsg);
		session.save(testMsg2);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		session.delete(testMsg);
		session.delete(testMsg2);
		session.close();
	}

	@Test
	public void simpleQuerytest() {

		Query<MessageJPA> query = session.createQuery(MessageJPA.class);

		List<MessageJPA> msgs = query.addFilter(new EqualsFilter().filter("messageId").eq(testMsg.getMessageId()))
				.execute();

		Assert.assertEquals(2, msgs.size());

		for (MessageJPA e : msgs) {
			Assert.assertEquals(testMsg.getMessageId(), e.getMessageId());
		}

	}

	@Test
	public void simpleQuerytest2() {

		Query<MessageJPA> query = session.createQuery(MessageJPA.class);

		List<MessageJPA> msgs = query.addFilter(new EqualsFilter().filter("messageId").eq(testMsg.getMessageId()))
				.addFilter(new NotEqualsFilter().filter("locale").neq(Locale.CHINA.toString())).execute();

		Assert.assertEquals(1, msgs.size());

		MessageJPA msg = msgs.get(0);

		Assert.assertNotNull(msg);

		Assert.assertEquals(Locale.US.toString(), msg.getLocale());

	}
}
