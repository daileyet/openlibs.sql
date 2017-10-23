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
* @Title: SimpleConnectionPool.java 
* @Package openthinks.libs.sql.dao.pool.impl 
* @author dailey.yet@outlook.com  
* @date Jul 31, 2015
* @version V1.0   
*/
package com.openthinks.libs.sql.dao.pool.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.openthinks.libs.sql.dao.pool.ConnectionPool;
import com.openthinks.libs.sql.lang.Configurator;

/**
 * Simple and default implementation of {@link ConnectionPool}
 * @author dailey.yet@outlook.com
 *
 */
public class SimpleConnectionPool implements ConnectionPool {
	private final ConcurrentLinkedQueue<Connection> connectionQueue;
	private final Configurator configurator;
	private final Lock lock;
	private final AtomicInteger activeCount = new AtomicInteger(0);
	private final AtomicBoolean rejectAction = new AtomicBoolean(false);
	private final Logger logger = Logger.getLogger(SimpleConnectionPool.class);

	/**
	 * 
	 * @param configurator Configurator
	 */
	public SimpleConnectionPool(Configurator configurator) {
		this.connectionQueue = new ConcurrentLinkedQueue<>();
		this.lock = new ReentrantLock();
		this.configurator = configurator;
	}

	/* (non-Javadoc)
	 * @see openthinks.libs.sql.dao.pool.ConnectionPool#request()
	 */
	@Override
	public Connection request() throws ClassNotFoundException, SQLException {
		if (rejectAction.get())
			throw new IllegalStateException("This pool has been shutdown.");
		lock.lock();
		try {
			Connection conn = connectionQueue.poll();
			if (conn == null) {//no free connection, create new connection
				conn = createConnection();
			} else if (conn.isClosed()) {//process the closed connection, and recreate a new connection
				destroy(conn);
				conn = createConnection();
			} else {//directly get connections from free side
			}
			return conn;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Create a new {@link Connection}
	 * @return Connection
	 * @throws IllegalStateException occur when pool active connections exceed max account
	 * @throws ClassNotFoundException  occur when if the class cannot be located
	 * @throws SQLException occur when  if a database access error occurs or the url is null
	 */
	protected Connection createConnection() throws IllegalStateException, ClassNotFoundException, SQLException {
		Connection conn;
		if (activeCount.get() >= this.maxActive())
			throw new IllegalStateException("Reached the maximum number of database connections in pool.");
		Class.forName(configurator.getDriver());
		conn = DriverManager.getConnection(configurator.getUrl(), configurator.getUserName(),
				configurator.getUserPwd());
		activeCount.incrementAndGet();
		return conn;
	}

	/* (non-Javadoc)
	 * @see openthinks.libs.sql.dao.pool.ConnectionPool#recycle(java.sql.Connection[])
	 */
	@Override
	public void recycle(Connection... conns) {
		if (conns == null || conns.length == 0)
			return;
		if (rejectAction.get())
			throw new IllegalStateException("This pool has been shutdown.");
		lock.lock();
		try {
			for(Connection conn:conns) {
				if(idelSize()+1 > maxIdle()) {
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						logger.error("Database close error occurs", e);
						continue;
					}
				}else {
					connectionQueue.add(conn);
				}
			}
		} finally {
			lock.unlock();
		}

	}

	void destroy(Connection... conns) {
		if (conns == null || conns.length == 0)
			return;
		for (Connection conn : conns) {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("Database close error occurs", e);
				continue;
			}
			lock.lock();
			try {
				this.connectionQueue.remove(conn);
				activeCount.decrementAndGet();
			} finally {
				lock.unlock();
			}
		}

	}

	/* (non-Javadoc)
	 * @see openthinks.libs.sql.dao.pool.ConnectionPool#size()
	 */
	@Override
	public int size() {
		return activeCount.get();
	}
	
	protected int idelSize() {
		return connectionQueue.size();
	}

	public int maxActive() {
		String maxActive = (String) this.configurator.get(MAX_ACTIVE);
		return maxActive == null ? Integer.MAX_VALUE : Integer.valueOf(maxActive);
	}

	public int maxIdle() {
		String maxIdle = (String) this.configurator.get(MAX_IDLE);
		return maxIdle == null ? Integer.MAX_VALUE : Integer.valueOf(maxIdle);
	}

	/* (non-Javadoc)
	 * @see openthinks.libs.sql.dao.pool.ConnectionPool#shutdown()
	 */
	@Override
	public void shutdown() {
		rejectAction.compareAndSet(false, true);
		Connection conn = this.connectionQueue.poll();
		while (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("Database access error occurs", e);
				continue;
			}
			lock.lock();
			try {
				this.connectionQueue.remove(conn);
				activeCount.decrementAndGet();
			} finally {
				lock.unlock();
			}
			conn = this.connectionQueue.poll();
		}
	}

	@Override
	public boolean isShutdown() {
		return rejectAction.get();
	}

}
