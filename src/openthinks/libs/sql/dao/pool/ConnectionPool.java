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
* @Title: ConnectionPool.java 
* @Package openthinks.libs.sql.dao.pool 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Jul 30, 2015
* @version V1.0   
*/
package openthinks.libs.sql.dao.pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The pool for {@link Connection}
 * @author dailey.yet@outlook.com
 *
 */
public interface ConnectionPool {

	String MAX_ACTIVE = "";

	/**
	 * request a {@link Connection}, maybe it is a new or it is already existed
	 * @return Connection
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	Connection request() throws ClassNotFoundException, SQLException;

	/**
	 * recycle the {@link Connection}s
	 * @param conns Connection[]
	 */
	void recycle(Connection... conns);

	/**
	 * current count of free {@link Connection} in pool
	 * @return Integer
	 */
	int size();

	/**
	 * really close all holding {@link Connection}s, and reject other actions
	 */
	void shutdown();

	boolean isShutdown();
}
