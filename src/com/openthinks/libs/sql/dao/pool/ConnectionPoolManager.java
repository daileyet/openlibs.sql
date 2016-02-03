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
* @Title: ConnectionPoolManager.java 
* @Package openthinks.libs.sql.dao.pool 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Jul 30, 2015
* @version V1.0   
*/
package com.openthinks.libs.sql.dao.pool;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.openthinks.libs.sql.dao.pool.impl.SimpleConnectionPool;
import com.openthinks.libs.sql.lang.Configurator;
import com.openthinks.libs.utilities.Checker;
import com.openthinks.libs.utilities.InstanceUtilities;

/**
 * The management of {@link ConnectionPool}
 * @author dailey.yet@outlook.com
 *
 */
public final class ConnectionPoolManager {
	private static ConnectionPool singletonPoolInstance;
	private static Lock lock = new ReentrantLock();
	private static Logger logger = Logger.getLogger(ConnectionPoolManager.class);

	/**
	 * @param configurator Configurator
	 * @return ConnectionPool
	 */
	public static ConnectionPool getInstance(Configurator configurator) {
		Checker.require(configurator).notNull();
		if (!configurator.isUsePool())
			throw new IllegalArgumentException("Current configuration for database without using pool.");
		lock.lock();
		try {
			if (singletonPoolInstance == null || singletonPoolInstance.isShutdown()) {
				String clazzName = (String) configurator.get(ConnectionPool.POOL_CLASS);
				if (clazzName == null) {
					singletonPoolInstance = new SimpleConnectionPool(configurator);
				} else {
					singletonPoolInstance = initialConnectionPool(configurator);
				}
			}
		} finally {
			lock.unlock();
		}
		return singletonPoolInstance;
	}

	/**
	 * @param configurator Configurator
	 */
	@SuppressWarnings("unchecked")
	private static ConnectionPool initialConnectionPool(Configurator configurator) {
		String clazzName = (String) configurator.get(ConnectionPool.POOL_CLASS);
		ConnectionPool connPool = null;
		try {
			Class<? extends ConnectionPool> clazz = (Class<? extends ConnectionPool>) Class.forName(clazzName);
			if (ConnectionPool.class.isAssignableFrom(clazz)) {
				boolean isThrowed = false;
				try {// try Constructor with Configurator firstly
					connPool = InstanceUtilities.create(clazz, null, new Object[] { configurator });
					if (connPool == null)
						throw new IllegalAccessException("Failed to instance the constructor of " + clazz.getName()
								+ " with parameter " + Configurator.class.getName());
				} catch (Exception e) {
					isThrowed = true;
				}
				if (isThrowed) {
					connPool = InstanceUtilities.create(clazz, null, new Object[] {});
					if (connPool == null)
						throw new IllegalAccessException("Failed to instance the constructor of " + clazz.getName()
								+ " with no parameter.");
				}
			} else {
				throw new IllegalArgumentException("Not the implementation type from ConnectionPool.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			connPool = new SimpleConnectionPool(configurator);
		}
		return connPool;
	}

	/**
	 * set the instance of ConnectionPool by the given parameter;<BR>
	 * it can be other complex implementation for {@link ConnectionPool}
	 * @param singletonPoolInstance the singletonPoolInstance to set
	 */
	public static void setSingletonPoolInstance(ConnectionPool singletonPoolInstance) {
		ConnectionPoolManager.singletonPoolInstance = singletonPoolInstance;
	}

}
