/**
 * 数据库实体访问实现包
 */
package openthinks.libs.sql.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import openthinks.libs.sql.dao.BaseDao;
import openthinks.libs.sql.data.DefaultRow;
import openthinks.libs.sql.data.Row;
import openthinks.libs.sql.entity.ColumnAttributeMapping;
import openthinks.libs.sql.entity.Entity;
import openthinks.libs.sql.lang.Condition;
import openthinks.libs.sql.lang.Configurator;
import openthinks.libs.sql.lang.ConfiguratorFactory;
import openthinks.libs.sql.lang.reflect.ReflectEngine;
import openthinks.libs.utilities.Checker;

import org.apache.log4j.Logger;

/**
 * BaseDao接口默认实现类
 * 
 * @author dmj
 * @param <E>
 */
public abstract class BaseDaoImpl implements BaseDao {
	/**
	 * 日志记录器对象
	 */
	private Logger logger = null;
	/**
	 * 数据库连接参数配置器对象
	 */
	private Configurator configurator = null;

	/**
	 * 默认构造 默认初始化日志记录器 logger 默认初始化数据库连接配置器 configurator
	 */
	public BaseDaoImpl() {
		try {
			if (logger == null) {
				Logger logger = Logger.getLogger(this.getClass().getName());
				if (logger != null)
					setLogger(logger);
			}
			if (configurator == null) {
				Configurator config = ConfiguratorFactory.getDefaultInstance(this.getClass());
				if (config != null)
					setConfigurator(config);
			}
		} catch (Exception ex) {
			if (logger != null) {
				logger.error(ex);
			}
		}
	}

	/**
	 * 带参构造
	 * 
	 * @param logger
	 *            日志记录器
	 * @param configurator
	 *            数据库连接配置器
	 */
	public BaseDaoImpl(Logger logger, Configurator configurator) {
		setLogger(logger);
		setConfigurator(configurator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Logger getLogger() {
		return logger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configurator getConfigurator() {
		return configurator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUsePool() {
		return this.configurator.isUsePool();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultSet executeQuery(String sql) {
		return executeQuery(sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultSet executeQuery(String sql, String[] params) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			ps = conn.prepareStatement(sql);
			if (ps != null) {
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						ps.setString((i + 1), params[i]);
					}
				}
				rs = ps.executeQuery();
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return rs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultSet executeQuery(Condition condition) {
		return condition == null ? null : executeQuery(Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object executeScalar(String sql) {
		return executeScalar(sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object executeScalar(String sql, String[] params) {
		ResultSet rs = null;
		Object result = null;
		try {
			rs = executeQuery(sql, params);
			if (rs != null) {
				if (rs.next()) {
					result = rs.getObject(1);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			closeAll(rs);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object executeScalar(Condition condition) {
		return condition == null ? null : executeScalar(Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int executeSql(String sql) {
		return executeSql(sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int executeSql(String sql, String[] params) {
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = getConn();
			ps = conn.prepareStatement(sql);
			if (ps != null) {
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						ps.setString((i + 1), params[i]);
					}
				}
				result = ps.executeUpdate();
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			closeAll(conn, ps, null);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int executeSql(Condition condition) {
		return executeSql(Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConn() throws ClassNotFoundException, SQLException {
		Class.forName(configurator.getDriver());
		Connection conn = DriverManager.getConnection(configurator.getUrl(), configurator.getUserName(),
				configurator.getUserPwd());
		return conn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Row> list(String sql, String[] params) {
		List<Row> list = new ArrayList<Row>();
		ResultSet rs = executeQuery(sql, params);
		if (rs != null) {
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();// 查询的列数
				ColumnAttributeMapping columnAttributeMapping = new ColumnAttributeMapping();
				for (int i = 1; i <= count; i++) {
					String columnName = rsmd.getColumnName(i);
					columnAttributeMapping.map(columnName, columnName);
				}
				while (rs.next()) {
					Object[] values = new Object[count];
					for (int i = 1; i <= count; i++) {
						values[i - 1] = rs.getObject(rsmd.getColumnName(i));
					}
					Row row = new DefaultRow(columnAttributeMapping.toArray(), values);// 创建一行记录
					list.add(row);
				}
			} catch (SQLException ex) {
				getLogger().error(ex);
			} finally {
				closeAll(rs);
			}
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Row> list(String sql) {
		return list(sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Row> list(Condition condition) {
		return list(Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public <T extends Entity> List<T> listEntity(Class<T> clz, String sql) {
		return listEntity(clz, sql, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public <T extends Entity> List<T> listEntity(Class<T> clz, String sql, String[] params) {
		return list(clz, sql, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public <T extends Entity> List<T> listEntity(Class<T> clz, Condition condition) {
		return condition == null ? Collections.<T> emptyList() : listEntity(clz, Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> list(Class<T> clz) {
		String persistName = ReflectEngine.getEntityTable(clz);
		Checker.require(persistName).notNull();
		return list(clz, "SELECT * FROM " + persistName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2010/11/17
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
		if (clz == null)
			throw new NullPointerException("传入的Class类型不能为空");
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		try {
			rs = executeQuery(sql, params);
			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				while (rs.next()) {
					T entity = clz.newInstance();
					for (int i = 1; i <= count; i++) {
						try {
							String columnName = rsmd.getColumnName(i);
							Object columnValue = rs.getObject(columnName);
							ReflectEngine.propertyReflect(entity, columnName, columnValue);
						} catch (Exception e) {
							logger.warn(e);
							continue;
						}
					}
					list.add(entity);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			closeAll(rs);
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2010/11/17
	 */
	@Override
	public <T> List<T> list(Class<T> clz, Condition condition) {
		return condition == null ? Collections.<T> emptyList() : list(clz, Condition.getFullSql(condition));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeAll(Connection conn, Statement st, ResultSet rs) {
		closeResultSet(rs);
		closeStatement(st);
		closeConnection(conn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeAll(ResultSet rs) {
		if (rs != null) {
			try {
				Statement st = rs.getStatement();
				Connection conn = st.getConnection();
				closeAll(conn, st, rs);
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeConnection(Connection conn) {
		if (conn != null && isUsePool() == false) {
			try {
				if (!conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		if (conn != null && isUsePool()) {
			//TODO put it back to pool
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				closeStatement(rs.getStatement());
				rs.close();
				rs = null;
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
				st = null;
			} catch (SQLException e) {
				logger.error(e);
			}
		}

	}

}
