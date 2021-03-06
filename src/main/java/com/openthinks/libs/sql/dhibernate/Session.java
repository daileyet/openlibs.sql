package com.openthinks.libs.sql.dhibernate;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.openthinks.libs.sql.dao.BaseDao;
import com.openthinks.libs.sql.data.Row;
import com.openthinks.libs.sql.dhibernate.support.TransactionLevel;
import com.openthinks.libs.sql.dhibernate.support.query.Query;
import com.openthinks.libs.sql.entity.Entity;
import com.openthinks.libs.sql.exception.TransactionException;
import com.openthinks.libs.sql.lang.Condition;
import com.openthinks.libs.sql.lang.Configurator;

/**
 * 与数据库会话接口 Include high level and low level ways to access database<BR>
 * <B>High level DAO API - auto generate SQL</B>
 * <ul>
 * <li>{@link #createQuery(Class)}</li>
 * <li>{@link #load(Class, Serializable)}</li>
 * <li>{@link #save(Object)}</li>
 * <li>{@link #update(Object)}</li>
 * <li>{@link #delete(Object)}</li>
 * <li>{@link #list(Class)}</li>
 * </ul>
 * <B>Low level DAO API - manual generate SQL</B>
 * <ul>
 * <li>{@link #add(Condition)} {@link #add(String)}
 * {@link #add(String, String[])}</li>
 * <li>{@link #createCondition()}</li>
 * <li>{@link #delete(Condition)} {@link #delete(String)}
 * {@link #delete(String, String[])}</li>
 * <li>{@link #get(Class, Condition)} {@link #get(Class, String)}
 * {@link #get(Class, String, String[])}</li>
 * <li>{@link #list(Condition)} {@link #list(String)}
 * {@link #list(String, String[])}</li>
 * <li>{@link #list(Class, Condition)} {@link #list(Class, String)}
 * {@link #list(Class, String, String[])}</li>
 * <li>{@link #update(Condition)} {@link #update(String)}
 * {@link #update(String, String[])}</li>
 * </ul>
 * 
 * @author dmj
 * @version 2010/11/19
 * 
 */
public interface Session {

	/**
	 * 开启事务
	 * 
	 * @throws TransactionException
	 *             事务异常
	 */
	public void beginTransaction() throws TransactionException;

	/**
	 * 开启事务并设置事务级别
	 * 
	 * @param transactionLevel
	 *            TransactionLevel
	 * @throws TransactionException
	 *             TransactionException
	 */
	public void beginTransaction(TransactionLevel transactionLevel) throws TransactionException;

	/**
	 * 关闭事务
	 * 
	 * @throws TransactionException
	 *             事务异常
	 */
	public void endTransaction() throws TransactionException;

	/**
	 * 提交事务
	 * 
	 * @throws TransactionException
	 *             事务异常
	 */
	public void commit() throws TransactionException;

	/**
	 * 回滚事务
	 * 
	 * @throws TransactionException
	 *             事务异常
	 */
	public void rollback() throws TransactionException;

	/**
	 * 关闭与数据库会话
	 */
	public void close();

	/**
	 * 取得日志记录器 logger
	 * 
	 * @return 日志记录器logger
	 */
	public Logger getLogger();

	/**
	 * 取得数据库连接配置器 configurator
	 * 
	 * @return 数据库连接配置器configurator
	 */
	public Configurator getConfigurator();

	/**
	 * judge database connection is auto-close after execute DML
	 * 
	 * @return Boolean
	 */
	public Boolean isAutoClose();

	/**
	 * controller database connection auto-close when after execute DML
	 * 
	 */
	public void enableAutoClose();

	public void disableAutoClose();

	/*
	 * =============================================================================
	 * ============= High level DAO API, recommend
	 */
	/**
	 * create a {@link Query} for this given entity class
	 * 
	 * @param clz
	 *            Class entity class type
	 * @param <T>
	 *            the entity class type
	 * @return Query
	 */
	public <T> Query<T> createQuery(Class<T> clz);

	/**
	 * 根据id值获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz<BR>
	 *            1.{@link Entity}子类默认第一个属性为ID列,类名需与表名一致<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * @param id
	 *            Serializable 主键值
	 * @param <T>
	 *            the entity class type
	 * @return Object 数据库表所对应的实体对象
	 */
	public <T> T load(Class<T> clz, Serializable id);

	/**
	 * 持久化对象,进行Insert
	 * 
	 * @param <T>
	 *            the entity class type
	 * @param object
	 *            T 实体类型T<BR>
	 *            1.{@link Entity}子类默认第一个属性为ID列,类名需与表名一致<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 */
	public <T> void save(T object);

	/**
	 * 持久化对象,根据持久化的对象主键进行Update 实体类型T<BR>
	 * 1.{@link Entity}子类默认第一个属性为ID列,类名需与表名一致<BR>
	 * 2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * 
	 * @param object
	 *            T
	 * @param <T>
	 *            the entity class type
	 */
	public <T> void update(T object);

	/**
	 * 删除对象,根据持久化的对象主键进行Delete 实体类型T<BR>
	 * 1.{@link Entity}子类默认第一个属性为ID列,类名需与表名一致<BR>
	 * 2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * 
	 * @param object
	 *            T
	 * @param <T>
	 *            the entity class type
	 */
	public <T> void delete(T object);

	/**
	 * 删除所有对象
	 * 
	 * @param clazz
	 *            实体类型clazz<BR>
	 *            1.{@link Entity}子类默认第一个属性为ID列,类名需与表名一致<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * @param <T>
	 *            the entity class type
	 */
	public <T> void deleteAll(Class<T> clazz);

	/**
	 * 返回所有相应实体类的集合列表<br>
	 * 
	 * @param clz
	 *            查询的实体Class类型,<span style=color:red>可以不是Entity的子类</span><BR>
	 *            1.{@link Entity}子类子类默认第一个属性为ID列,类名需与表名一致<BR>
	 *            2.JPA注解方式
	 * @param <T>
	 *            the entity class type
	 * @since 2010/11/17
	 * @return List 任何实体类的集合列表
	 */
	public <T> List<T> list(Class<T> clz);

	/*
	 * =============================================================================
	 * ============= Low level DAO API, recommend
	 */

	/**
	 * 根据查询语句获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz<BR>
	 *            1.{@link Entity}子类<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * @param sql
	 *            完整标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @param <T>
	 *            the entity class type
	 * @return T 数据库表所对应的实体对象
	 */
	public <T> T get(Class<T> clz, String sql);

	// @Deprecated
	// public <T extends Entity> T get(Entity entity, String sql);

	/**
	 * 根据查询语句获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz<BR>
	 *            1.{@link Entity}子类<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * @param sql
	 *            完整标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @param params
	 *            sql语句依赖的参数数组
	 * @param <T>
	 *            the entity class type
	 * @return T 数据库表所对应的实体对象
	 */
	public <T> T get(Class<T> clz, String sql, String[] params);

	// @Deprecated
	// public <T extends Entity> T get(Entity entity, String sql, String[] params);
	/**
	 * 根据查询语句获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz<BR>
	 *            1.{@link Entity}子类<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Entity}
	 * @param condition
	 *            专用于生成带条件的完整查询语句的对象,<span style=color:red;>不支持多表查询</span>
	 * @param <T>
	 *            the entity class type
	 * @return T 数据库表所对应的实体对象
	 */
	public <T> T get(Class<T> clz, Condition condition);

	// @Deprecated
	// public <T extends Entity> T get(Entity entity, Condition condition);
	/**
	 * 根据查询语句获取clz类型的实体对象对象列表
	 * 
	 * @see Session#list(Class, String, String[])
	 * @param clz
	 *            实体类型clz
	 * @param sql
	 *            标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @param <T>
	 *            the entity class type
	 * @return List 数据库表所对应的实体对象列表集合
	 */
	public <T> List<T> list(Class<T> clz, String sql);

	/**
	 * 根据查询语句获取clz类型的实体对象对象列表
	 * 
	 * @see BaseDao#list(Class, String, String[])
	 * @param clz
	 *            实体类型clz
	 * @param sql
	 *            标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @param params
	 *            sql语句依赖的参数数组
	 * @param <T>
	 *            the entity class type
	 * @return List 数据库表所对应的实体对象列表集合
	 */
	public <T> List<T> list(Class<T> clz, String sql, String[] params);

	/**
	 * 根据查询语句获取clz类型的实体对象对象列表
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param condition
	 *            专用于生成带条件的sql语句的对象,<span style=color:red;>不支持多表查询</span>
	 * @param <T>
	 *            the entity class type
	 * @return List 数据库表所对应的实体对象列表集合
	 */
	public <T> List<T> list(Class<T> clz, Condition condition);

	/**
	 * 返回满足查询条件的所有行的集合列表<br>
	 * 
	 * @param sql
	 *            标准查询sql语句,<span style=color:red>支持跨表查询</span>
	 * @param params
	 *            sql语句依赖的具体参数数组
	 * @return List 实现Row接口类型的集合列表
	 */
	public List<Row> list(String sql, String[] params);

	/**
	 * 返回满足查询条件的所有行的集合列表<br>
	 * 
	 * @param sql
	 *            标准查询sql语句,<span style=color:red>支持跨表查询</span>
	 * @return List 实现Row接口类型的集合列表
	 */
	public List<Row> list(String sql);

	/**
	 * 返回满足查询条件的所有行的集合列表<br>
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return List 实现Row接口类型的集合列表
	 */
	public List<Row> list(Condition condition);

	/**
	 * 创建用于生成带条件的sql语句的对象
	 * 
	 * @return Condition
	 */
	public Condition createCondition();

	/**
	 * 执行增加标准sql语句
	 * 
	 * @param sql
	 *            增加标准sql语句
	 * @return int 影响行数
	 */
	public int add(String sql);

	/**
	 * 执行增加标准sql语句
	 * 
	 * @param sql
	 *            增加标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @return int 影响行数
	 */
	public int add(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行增加
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return int 影响行数
	 */
	public int add(Condition condition);

	/**
	 * 执行更新标准sql语句
	 * 
	 * @param sql
	 *            更新标准sql语句
	 * @return int 影响行数
	 */
	public int update(String sql);

	/**
	 * 执行更新标准sql语句
	 * 
	 * @param sql
	 *            更新标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @return int 影响行数
	 */
	public int update(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行更新
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return int 影响行数
	 */
	public int update(Condition condition);

	/**
	 * 执行删除标准sql语句
	 * 
	 * @param sql
	 *            删除标准sql语句
	 * @return int 影响行数
	 */
	public int delete(String sql);

	/**
	 * 执行删除标准sql语句
	 * 
	 * @param sql
	 *            删除标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @return int 影响行数
	 */
	public int delete(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行删除
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return int 影响行数
	 */
	public int delete(Condition condition);

}
