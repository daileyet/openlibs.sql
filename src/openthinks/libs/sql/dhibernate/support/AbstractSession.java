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
 * @Title: AbstractSession.java 
 * @Package openthinks.libs.sql.dhibernate.support 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-2
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

import openthinks.libs.sql.dao.BaseDao;
import openthinks.libs.sql.data.Row;
import openthinks.libs.sql.dhibernate.Session;
import openthinks.libs.sql.dhibernate.support.query.Query;
import openthinks.libs.sql.dhibernate.support.query.impl.Queryer;
import openthinks.libs.sql.dhibernate.support.template.SQLType;
import openthinks.libs.sql.dhibernate.support.template.Template;
import openthinks.libs.sql.exception.TransactionBackException;
import openthinks.libs.sql.exception.TransactionBeginException;
import openthinks.libs.sql.exception.TransactionEndException;
import openthinks.libs.sql.exception.TransactionException;
import openthinks.libs.sql.lang.Condition;
import openthinks.libs.sql.lang.Configurator;
import openthinks.libs.sql.lang.reflect.ReflectEngine;
import openthinks.libs.utilities.Checker;

import org.apache.log4j.Logger;

/**
 * @author minjdai
 * 
 */
public abstract class AbstractSession implements Session {

	/**
	 * 操作数据库的接口
	 */
	private Boolean autoClose = true;

	/**
	 * 
	 */
	public AbstractSession() {
		super();
	}

	/**
	 * @param autoClose
	 *            the autoClose to set
	 */
	protected void setAutoClose(Boolean autoClose) {
		this.autoClose = autoClose;
		// ((SessionDaoImpl) this.getBaseDao()).setAutoClose(this.autoClose);
	}

	/**
	 * provider the {@link BaseDao}
	 * @return BaseDao
	 */
	public abstract BaseDao getBaseDao();

	/**
	 * @return Boolean if is autoClose
	 */
	@Override
	public Boolean isAutoClose() {
		return autoClose;
	}

	@Override
	public <T> T load(Class<T> clz, Serializable id) {
		Checker.require(id).notNull();
		String persistName = ReflectEngine.getEntityTable(clz);
		Checker.require(persistName).notNull();
		String persistIdName = ReflectEngine.getEntityID(clz);
		Checker.require(persistIdName).notNull();
		String sql = "SELECT * FROM " + persistName + " WHERE " + persistIdName + " = ?";
		return get(clz, sql, new String[] { id.toString() });
	}

	@Override
	public <T> void save(T object) {
		Checker.require(object).notNull();
		Template template = ReflectEngine.createSQLTemplate(object.getClass());
		template.setType(SQLType.INSERT);
		template.setData(object);
		String sql = template.generateSQL();
		getBaseDao().executeSql(sql);
	}

	@Override
	public <T> void update(T object) {
		Checker.require(object).notNull();
		Template template = ReflectEngine.createSQLTemplate(object.getClass());
		template.setType(SQLType.UPDATE);
		template.setData(object);
		String sql = template.generateSQL();
		getBaseDao().executeSql(sql);

	}

	@Override
	public <T> void delete(T object) {
		Checker.require(object).notNull();
		Template template = ReflectEngine.createSQLTemplate(object.getClass());
		template.setType(SQLType.DELETE);
		template.setData(object);
		String sql = template.generateSQL();
		getBaseDao().executeSql(sql);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Class<T> clz, String sql) {
		return get(clz, sql, null);
	}

	//	@Deprecated
	//	@Override
	//	public <T extends Entity> T get(Entity entity, String sql) {
	//		return get(entity, sql, null);
	//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Class<T> clz, String sql, String[] params) {
		if (clz == null)
			throw new NullPointerException("传入的Class类型不能为空");
		T entity = null;
		ResultSet rs = null;
		try {
			rs = getBaseDao().executeQuery(sql, params);
			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				while (rs.next()) {

					entity = clz.newInstance();
					for (int i = 1; i <= count; i++) {
						try {
							String columnName = rsmd.getColumnName(i);
							Object columnValue = rs.getObject(columnName);
							ReflectEngine.propertyReflect(entity, columnName, columnValue);
						} catch (Exception e) {
							getBaseDao().getLogger().warn(e.getMessage());
							continue;
						}
					}
					break;
				}
			}
		} catch (Exception ex) {
			getBaseDao().getLogger().error(ex.getMessage());
		} finally {
			getBaseDao().closeAll(rs);
		}
		return entity;
	}

	//	@Deprecated
	//	@Override
	//	@SuppressWarnings("unchecked")
	//	public <T extends Entity> T get(Entity entity, String sql, String[] params) {
	//
	//		if (entity == null)
	//			throw new NullPointerException("传入的entity不能为空");
	//		ResultSet rs = null;
	//		try {
	//			rs = getBaseDao().executeQuery(sql, params);
	//			if (rs != null) {
	//				ResultSetMetaData rsmd = rs.getMetaData();
	//				int count = rsmd.getColumnCount();
	//				while (rs.next()) {
	//					for (int i = 1; i <= count; i++) {
	//						try {
	//							String columnName = rsmd.getColumnName(i);
	//							Object columnValue = rs.getObject(columnName);
	//							ReflectEngine.propertyReflect(entity, columnName, columnValue);
	//							// entity.set(columnName, columnValue);
	//						} catch (Exception e) {
	//							getBaseDao().getLogger().warn(e.getMessage());
	//							continue;
	//						}
	//					}
	//					break;
	//				}
	//			}
	//		} catch (Exception ex) {
	//			getBaseDao().getLogger().error(ex.getMessage());
	//		} finally {
	//			getBaseDao().closeAll(rs);
	//		}
	//		return ((T) entity);
	//	}

	@Override
	public <T> List<T> list(Class<T> clz) {
		return getBaseDao().list(clz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Class<T> clz, Condition condition) {
		return condition == null ? null : get(clz, Condition.getFullSql(condition));
	}

	//	@Deprecated
	//	@SuppressWarnings("unchecked")
	//	@Override
	//	public <T extends Entity> T get(Entity entity, Condition condition) {
	//		return (T) (condition == null ? null : get(entity, Condition.getFullSql(condition)));
	//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> list(Class<T> clz, String sql) {
		return list(clz, sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> list(Class<T> clz, String sql, String[] params) {
		List<T> list = null;
		list = getBaseDao().list(clz, sql, params);
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> list(Class<T> clz, Condition condition) {
		return list(clz, Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Row> list(String sql, String[] params) {
		return getBaseDao().list(sql, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Row> list(String sql) {
		return getBaseDao().list(sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Row> list(Condition condition) {
		return getBaseDao().list(condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Condition createCondition() {
		return new Condition();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int add(String sql) {
		return getBaseDao().executeSql(sql);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int add(String sql, String[] params) {
		return getBaseDao().executeSql(sql, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int add(Condition condition) {
		return getBaseDao().executeSql(condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String sql) {
		return getBaseDao().executeSql(sql);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String sql, String[] params) {
		return getBaseDao().executeSql(sql, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(Condition condition) {
		return getBaseDao().executeSql(condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String sql) {
		return getBaseDao().executeSql(sql);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String sql, String[] params) {
		return getBaseDao().executeSql(sql, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(Condition condition) {
		return getBaseDao().executeSql(condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTransaction() throws TransactionException {
		try {
			getBaseDao().getConn().setAutoCommit(false);
		} catch (Exception e) {
			throw new TransactionBeginException(e.getCause());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() throws TransactionException {
		try {
			getBaseDao().getConn().commit();
		} catch (Exception e) {
			throw new TransactionEndException(e.getCause());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() throws TransactionException {
		try {
			getBaseDao().getConn().rollback();
		} catch (Exception e) {
			throw new TransactionBackException(e.getCause());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			getBaseDao().closeConnection(getBaseDao().getConn());
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configurator getConfigurator() {
		return getBaseDao().getConfigurator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Logger getLogger() {
		return getBaseDao().getLogger();
	}

	@Override
	public void disableAutoClose() {
		setAutoClose(false);
	}

	@Override
	public void enableAutoClose() {
		setAutoClose(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> Query<T> createQuery(Class<T> clz) {
		return new Queryer(this).queryObject(clz);
	}

}