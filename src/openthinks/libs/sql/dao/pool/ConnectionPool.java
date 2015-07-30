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

/**
 * The pool for {@link Connection}
 * @author dailey.yet@outlook.com
 *
 */
public interface ConnectionPool {

	/**
	 * request a {@link Connection}, maybe it is a new or it is already existed
	 * @return Connection
	 */
	Connection request();

	/**
	 * recycle the {@link Connection}s
	 * @param conns Connection[]
	 */
	void recycle(Connection... conns);
	
	/**
	 * really close the given {@link Connection}s
	 * @param conns Connection[]
	 */
	void destroy(Connection... conns);
	
	/**
	 * current count of active {@link Connection} in pool
	 * @return Integer
	 */
	int size();
	
	/**
	 * the capacity or max count of the pool for holding {@link Connection}
	 * @return Integer
	 */
	int capacity();
	
	/**
	 * really close all holding {@link Connection}s
	 */
	void destroyAll();
}
