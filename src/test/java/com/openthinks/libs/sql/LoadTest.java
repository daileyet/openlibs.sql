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
 * @Title: LoadTest.java 
 * @Description: TODO
 * @author dailey 
 * @date 2012-11-5
 * @version V1.0 
 */
package com.openthinks.libs.sql;

import java.util.List;

import org.junit.Test;

import com.openthinks.libs.sql.dhibernate.Session;
import com.openthinks.libs.sql.dhibernate.support.SessionFactory;

/**
 * @author dailey
 *
 */
public class LoadTest {

	@Test
	public void testListEntity() {
		Session session = SessionFactory.getSession();
		List<Message> list = session.list(Message.class, "SELECT * FROM message");
		for (Message entity : list) {
			System.out.println(entity);
		}

		list = session.list(Message.class, "SELECT * FROM message");
		for (Message entity : list) {
			System.out.println(entity);
		}
		session.close();

	}

	//	@Test
	public void testJPAList() {
		Session session = SessionFactory.getSession();
		List<MessageJPA> list = session.list(MessageJPA.class, "SELECT * FROM message");
		for (MessageJPA entity : list) {
			System.out.println(entity);
		}
		session.close();
	}

	//	@Test
	public void testListByClass() {
		Session session = SessionFactory.getSession();
		List<MessageJPA> list = session.list(MessageJPA.class);
		for (MessageJPA entity : list) {
			System.out.println(entity);
		}
		session.close();
	}

	//	@Test
	public void testGetById() {
		Session session = SessionFactory.getSession();
		MessageJPA message = session.load(MessageJPA.class, "1000");
		System.out.println(message);
		session.close();
	}

}
