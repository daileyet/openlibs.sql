package openthinks.libs.sql.dhibernate;

import java.io.Serializable;
import java.util.List;

import openthinks.libs.sql.data.Row;
import openthinks.libs.sql.dhibernate.support.query.Query;
import openthinks.libs.sql.entity.Entity;
import openthinks.libs.sql.exception.TransactionException;
import openthinks.libs.sql.lang.Condition;
import openthinks.libs.sql.lang.Configurator;

import org.apache.log4j.Logger;

/**
 * 与数据库会话接口
 * 
 * @author dmj
 * @version 2010/11/19
 * 
 */
public interface Session {

	/**
	 * create a {@link Query} for this given entity class
	 * @param clz Class<T> entity class type
	 * @return Query<T>
	 */
	public <T> Query<T> createQuery(Class<T> clz);

	/**
	 * 根据id值获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz<BR>
	 *            1.{@link Entity}子类默认第一个属性为ID列<BR>
	 *            2.JPA标注的实体类标准 {@link javax.persistence.Id}
	 * @param id Serializable
	 *            主键值
	 * @return Object 数据库表所对应的实体对象
	 */
	public <T> T load(Class<T> clz, Serializable id);

	/**
	 * 持久化对象,进行Insert
	 * 
	 * @param object
	 */
	public <T> void save(T object);

	/**
	 * 持久化对象,根据持久化的对象主键进行Update
	 * 
	 * @param object
	 */
	public <T> void update(T object);

	/**
	 * 
	 * @param object
	 */
	public <T> void delete(T object);

	/**
	 * 根据查询语句获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param sql
	 *            标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @return Object 数据库表所对应的实体对象
	 */
	public <T> T get(Class<T> clz, String sql);

	public <T extends Entity> T get(Entity entity, String sql);

	/**
	 * 根据查询语句获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param sql
	 *            标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @param params
	 *            sql语句依赖的参数数组
	 * @return Object 数据库表所对应的实体对象
	 */
	public <T> T get(Class<T> clz, String sql, String[] params);

	public <T extends Entity> T get(Entity entity, String sql, String[] params);

	/**
	 * 根据查询语句获取clz类型的实体对象
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param condition
	 *            专用于生成带条件的sql语句的对象,<span style=color:red;>不支持多表查询</span>
	 * @return Object 数据库表所对应的实体对象
	 */
	public <T> T get(Class<T> clz, Condition condition);

	public <T extends Entity> T get(Entity entity, Condition condition);

	/**
	 * 返回所有相应实体类的集合列表<br>
	 * 
	 * @param clz
	 *            查询的实体Class类型,<span style=color:red>可以不是Entity的子类</span><BR>
	 *            1.{@link Entity}子类<BR>
	 *            2.JPA注解方式
	 * @since 2010/11/17
	 * @return List<E> 任何实体类的集合列表
	 */
	public <T> List<T> list(Class<T> clz);

	/**
	 * 根据查询语句获取clz类型的实体对象对象列表
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param sql
	 *            标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @return List<T> 数据库表所对应的实体对象列表集合
	 */
	public <T> List<T> list(Class<T> clz, String sql);

	/**
	 * 根据查询语句获取clz类型的实体对象对象列表
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param sql
	 *            标准查询语句,<span style=color:red;>不支持多表查询语句</span>
	 * @param params
	 *            sql语句依赖的参数数组
	 * @return List<T> 数据库表所对应的实体对象列表集合
	 */
	public <T> List<T> list(Class<T> clz, String sql, String[] params);

	/**
	 * 根据查询语句获取clz类型的实体对象对象列表
	 * 
	 * @param clz
	 *            实体类型clz
	 * @param condition
	 *            专用于生成带条件的sql语句的对象,<span style=color:red;>不支持多表查询</span>
	 * @return List<T> 数据库表所对应的实体对象列表集合
	 */
	public <T> List<T> list(Class<T> clz, Condition condition);

	/**
	 * 返回满足查询条件的所有行的集合列表<br>
	 * 
	 * @param sql
	 *            标准查询sql语句,<span style=color:red>支持跨表查询</span>
	 * @param params
	 *            sql语句依赖的具体参数数组
	 * @return List<Row> 实现Row接口类型的集合列表
	 */
	public List<Row> list(String sql, String[] params);

	/**
	 * 返回满足查询条件的所有行的集合列表<br>
	 * 
	 * @param sql
	 *            标准查询sql语句,<span style=color:red>支持跨表查询</span>
	 * @return List<Row> 实现Row接口类型的集合列表
	 */
	public List<Row> list(String sql);

	/**
	 * 返回满足查询条件的所有行的集合列表<br>
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return List<Row> 实现Row接口类型的集合列表
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
	 * @param int 影响行数
	 */
	public int add(String sql);

	/**
	 * 执行增加标准sql语句
	 * 
	 * @param sql
	 *            增加标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @param int 影响行数
	 */
	public int add(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行增加
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @param int 影响行数
	 */
	public int add(Condition condition);

	/**
	 * 执行更新标准sql语句
	 * 
	 * @param sql
	 *            更新标准sql语句
	 * @param int 影响行数
	 */
	public int update(String sql);

	/**
	 * 执行更新标准sql语句
	 * 
	 * @param sql
	 *            更新标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @param int 影响行数
	 */
	public int update(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行更新
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @param int 影响行数
	 */
	public int update(Condition condition);

	/**
	 * 执行删除标准sql语句
	 * 
	 * @param sql
	 *            删除标准sql语句
	 * @param int 影响行数
	 */
	public int delete(String sql);

	/**
	 * 执行删除标准sql语句
	 * 
	 * @param sql
	 *            删除标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @param int 影响行数
	 */
	public int delete(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行删除
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @param int 影响行数
	 */
	public int delete(Condition condition);

	/**
	 * 开启事务
	 * 
	 * @throws TransactionException
	 *             事务异常
	 */
	public void beginTransaction() throws TransactionException;

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
	 * @return Boolean
	 */
	public Boolean isAutoClose();

	/**
	 * controller database connection auto-close when after execute DML
	 * 
	 */
	public void enableAutoClose();

	public void disableAutoClose();

}
