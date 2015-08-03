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
* @Title: ConditionTest.java 
* @Package openthinks.libs.sql 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 3, 2015
* @version V1.0   
*/
package openthinks.libs.sql;

import java.util.List;
import java.util.Locale;

import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.dhibernate.support.SessionFactory;
import openthinks.libs.sql.lang.Condition;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class ConditionTest {
	private static MessageJPA message = new MessageJPA();
	private static MessageJPA message2 = new MessageJPA();

	@BeforeClass
	public static void setUp() {
		Session session = SessionFactory.getSession();
		message.setMessageId("CONDITION_1");
		message.setLocale(Locale.US.toString());
		message.setContent("Condition class regular test");
		session.save(message);

		message2.setMessageId("CONDITION_2");
		message2.setLocale(Locale.US.toString());
		message2.setContent("Condition class other test");
		session.save(message2);
		session.close();
	}

	@Test
	public void regularTest() {
		Session session = SessionFactory.getSession();
		Condition condition = session.createCondition();
		condition.setSqlPart("SELECT * FROM message ");
		condition.addItem(Condition.ABSMATCH, "message_id", "CONDITION_1");
		MessageJPA msg = session.get(MessageJPA.class, condition);
		Assert.assertNotNull(msg);
		Assert.assertEquals(message.getContent(), msg.getContent());
		session.close();
	}

	@Test
	public void otherTest() {
		Condition condition = Condition.build(MessageJPA.class)
				.addItem(Condition.LIKEMATCH, "message_id", "CONDITION%")
				.addItem(Condition.ORDER, "message_id", Condition.Order.DESC);
		Session session = SessionFactory.getSession();
		List<MessageJPA> list = session.list(MessageJPA.class, condition);
		Assert.assertTrue(list.size() == 2);
		Assert.assertEquals(message2.getMessageId(), list.get(0).getMessageId());
		session.close();
	}

	@AfterClass
	public static void tearDown() {
		Session session = SessionFactory.getSession();
		session.delete(message);
		session.delete(message2);
		session.close();
	}
}
