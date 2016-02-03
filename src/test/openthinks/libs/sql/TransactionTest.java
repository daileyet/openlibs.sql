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
* @Title: TransactionTest.java 
* @Package openthinks.libs.sql 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 3, 2015
* @version V1.0   
*/
package openthinks.libs.sql;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.openthinks.libs.sql.dhibernate.Session;
import com.openthinks.libs.sql.dhibernate.support.SessionFactory;
import com.openthinks.libs.sql.exception.TransactionException;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class TransactionTest {
	private static MessageJPA message = new MessageJPA();

	@BeforeClass
	public static void setUp() {
		Session session = SessionFactory.getSession();
		message.setMessageId("TRANSACTION");
		message.setLocale(Locale.US.toString());
		message.setContent("Transaction test");
		session.save(message);
		session.close();
	}

	@Test
	public void beginTransactionTest() throws TransactionException {
		Session session = SessionFactory.getSession();
		session.beginTransaction();
		message.setContent("Transaction test rollback");
		session.update(message);
		int result = session.add("insert into message");
		if (result == 0) {
			session.rollback();
		} else {
			session.commit();
		}
		session.endTransaction();

		MessageJPA msg = session.load(MessageJPA.class, message.getMessageId());
		Assert.assertNotNull(msg);
		Assert.assertEquals("Transaction test", msg.getContent());
		session.close();
	}

	@AfterClass
	public static void tearDown() {
		Session session = SessionFactory.getSession();
		session.delete(message);
		session.close();
	}

}
