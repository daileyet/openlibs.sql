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
 * @Title: SaveTest.java 
 * @Package sql 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-9
 * @version V1.0 
 */
package openthinks.libs.sql;

import java.util.Locale;

import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.dhibernate.support.SessionFactory;

import org.junit.Test;

/**
 * @author dailey
 *
 */
public class SaveTest {

	//	@Test
	public void testSaveJPA() {
		Session session = SessionFactory.getSession();
		MessageJPA message = new MessageJPA();
		message.setLocale(Locale.CHINA.toString());
		message.setContent("HELLO");
		message.setMessageId("2000");
		session.save(message);
		session.close();
	}

	//	@Test
	public void testUpdate() {
		Session session = SessionFactory.getSession();
		MessageJPA message = new MessageJPA();
		message.setLocale(Locale.CHINA.toString());
		message.setContent("中国你好");
		message.setMessageId("2000");
		session.update(message);
		session.close();
	}

	@Test
	public void testSaveEntity() {
		Message message = new Message();
		message.setLocale(Locale.CHINA.toString());
		message.setContent("实体类1");
		message.setId("3000");
		Session session = SessionFactory.getSession();
		session.save(message);

		message = new Message();
		message.setLocale(Locale.CHINA.toString());
		message.setContent("实体类2");
		message.setId("3001");
		session.save(message);
		session.close();
	}
}
