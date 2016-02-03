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
* @Title: ConnectionManager.java 
* @Package openthinks.libs.sql.dao 
* @Description: TODO
* @author dailey.yet@outlook.com  
* @date Jul 30, 2015
* @version V1.0   
*/
package com.openthinks.libs.sql.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.openthinks.libs.sql.dao.pool.ConnectionPool;
import com.openthinks.libs.sql.dao.pool.ConnectionPoolManager;
import com.openthinks.libs.sql.lang.Configurator;
import com.openthinks.libs.utilities.Checker;

/**
 * @author dailey.yet@outlook.com
 *
 */
public final class ConnectionManager {
	private static Logger logger = Logger.getLogger(ConnectionManager.class.getName());
	/**
	 * @param configurator Configurator
	 * @return Connection
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static Connection getConnection(Configurator configurator) throws ClassNotFoundException, SQLException {
		Checker.require(configurator).notNull();
		Connection conn = null;
		if(configurator.isUsePool()){
			ConnectionPool connPool=ConnectionPoolManager.getInstance(configurator);
			conn =  connPool.request();
		}else{
			Class.forName(configurator.getDriver());
			conn = DriverManager.getConnection(configurator.getUrl(),
					configurator.getUserName(), configurator.getUserPwd());
			
		}
		return conn;
	}

	/**
	 * @param configurator Configurator
	 * @param conns Connection[]
	 */
	public static void closeConnection(Configurator configurator, Connection... conns) {
		if(conns==null || conns.length==0) return;
		Checker.require(configurator).notNull();
		if(configurator.isUsePool()){
			ConnectionPool connPool=ConnectionPoolManager.getInstance(configurator);
			connPool.recycle(conns);
		}else{
			for(Connection conn:conns){
				if (conn != null) {
					try {
						if (!conn.isClosed()) {
							conn.close();
							conn = null;
						}
					} catch (SQLException e) {
						logger.error(e);
					}
				}
			}
			
		}
	}

	
}
