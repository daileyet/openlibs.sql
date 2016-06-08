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
* @Title: MyConnectionPool.java 
* @Package openthinks.libs.sql 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Aug 3, 2015
* @version V1.0   
*/
package com.openthinks.libs.sql;

import com.openthinks.libs.sql.dao.pool.impl.SimpleConnectionPool;
import com.openthinks.libs.sql.lang.Configurator;
import com.openthinks.libs.sql.lang.ConfiguratorFactory;

/**
 * @author dailey.yet@outlook.com
 *
 */
public class MyConnectionPool extends SimpleConnectionPool {

	/**
	 * @param configurator
	 */
	public MyConnectionPool(Configurator configurator) {
		super(configurator);
		System.out.println("Constructor");
	}

	public MyConnectionPool() {
		super(ConfiguratorFactory.getDefaultInstance());
		System.out.println("no Constructor");
	}

}
