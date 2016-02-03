/**
 * 数据库实体访问包
 */
package com.openthinks.libs.sql.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.openthinks.libs.sql.data.Row;
import com.openthinks.libs.sql.entity.Entity;
import com.openthinks.libs.sql.lang.Condition;
import com.openthinks.libs.sql.lang.Configurator;

/**
 * 数据库实体访问基本接口
 * 
 * @author dmj
 * @version 2010/11/15
 */
public interface BaseDao {

	/**
	 * 数据库连接是否是通过池管理
	 * @return boolean 
	 */
	public boolean isUsePool();

	/**
	 * 取得数据库连接
	 * 
	 * @throws Exception ClassNotFoundException, SQLException
	 * @return Connection 连接对象
	 */
	public Connection getConn() throws Exception;

	/**
	 * 设置数据连接
	 * 
	 * @param connection
	 *            Connection连接对象
	 */
	public void setConn(Connection connection);

	/**
	 * 执行增删改标准sql语句
	 * 
	 * @param sql
	 *            增删改标准sql语句
	 * @return int 执行后影响的行数
	 */
	public int executeSql(String sql);

	/**
	 * 执行增删改标准sql语句
	 * 
	 * @param sql
	 *            增删改标准sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @return int 执行后影响的行数
	 */
	public int executeSql(String sql, String[] params);

	/**
	 * 根据条件类实例所生成的标准sql语句执行增删改
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return int 执行后影响的行数
	 */
	public int executeSql(Condition condition);

	/**
	 * 执行查询单个值的标准查询语句
	 * 
	 * @param sql
	 *            标准查询单个值的语句
	 * @return Object 查询的结果
	 */
	public Object executeScalar(String sql);

	/**
	 * 执行查询单个值的标准查询语句
	 * 
	 * @param sql
	 *            标准查询单个值的语句
	 * @param params
	 *            依赖的具体参数数组
	 * @return Object 查询的结果
	 */
	public Object executeScalar(String sql, String[] params);

	/**
	 * 执行查询单个值的标准查询语句
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return Object 查询的结果
	 */
	public Object executeScalar(Condition condition);

	/**
	 * 执行标准查询sql语句
	 * 
	 * @param sql
	 *            标准查询sql语句
	 * @return ResultSet 查询结果集
	 */
	public ResultSet executeQuery(String sql);

	/**
	 * 执行标准查询sql语句
	 * 
	 * @param sql
	 *            标准查询sql语句
	 * @param params
	 *            依赖的具体参数数组
	 * @return ResultSet 查询结果集
	 */
	public ResultSet executeQuery(String sql, String[] params);

	/**
	 * 执行标准查询sql语句
	 * 
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @return ResultSet 查询结果集
	 */
	public ResultSet executeQuery(Condition condition);

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
	 * 返回满足查询条件的实体类的集合列表<br>
	 * <div style=color:gray;> 遵循实体类模板反射,可选择配置映射.
	 * <ol type=1>
	 * <li>外表映射文件</li>
	 * <li>实体类重写父类Entity的get,set方法</li>
	 * <li>无配置默认实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.</li>
	 * </ol>
	 * </div>
	 * @deprecated
	 * please use: {@link #list(Class, String)}
	 * @see Entity 
	 * @param clz
	 *            查询的实体Class类型,<font color=red><b>需是Entity的子类型</b></font>
	 * @param sql
	 *            标准查询sql语句,<span style=color:red>不支持跨表查询</span>
	 * @param <T> entity child class
	 * @return List 实体类Entity或其子类的集合列表
	 */
	@Deprecated
	public <T extends Entity> List<T> listEntity(Class<T> clz, String sql);

	/**
	 * 返回满足查询条件的实体类的集合列表<br>
	 * <div style=color:gray;> 遵循实体类模板反射,可选择配置映射.
	 * <ol type=1>
	 * <li>外表映射文件
	 * <li>实体类重写父类Entity的get,set方法
	 * <li>无配置默认实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.
	 * </ol></div>
	 * @deprecated
	 * please use: {@link #list(Class, String, String[])}
	 * @see Entity 
	 * @param clz
	 *            查询的实体Class类型,<font color=red><b>需是Entity的子类型</b></font>
	 * @param sql
	 *            标准查询sql语句,<span style=color:red>不支持跨表查询</span>
	 * @param params
	 *            sql语句依赖的具体参数数组
	 * @param <T> entity child class
	 * @return List 实体类Entity或其子类的集合列表
	 */
	@Deprecated
	public <T extends Entity> List<T> listEntity(Class<T> clz, String sql, String[] params);

	/**
	 * 返回满足查询条件的实体类的集合列表<br>
	 * <div style=color:gray;> 遵循实体类模板反射,可选择配置映射.
	 * <ol type=1>
	 * <li>外表映射文件
	 * <li>实体类重写父类Entity的get,set方法
	 * <li>无配置默认实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.
	 * </ol></div>
	 * @deprecated 
	 * please use: {@link #list(Class, Condition)}
	 * @see Entity 
	 * @param clz
	 *            查询的实体Class类型,<font color=red><b>需是Entity的子类型</b></font>
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @param <T> entity child class
	 * @return List 实体类Entity或其子类的集合列表
	 */
	@Deprecated
	public <T extends Entity> List<T> listEntity(Class<T> clz, Condition condition);

	/**
	 * 返回所有相应实体类的集合列表<br>
	 * <div style=color:gray;> 遵循数据库表结构反射. 实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.<br>
	 * <table border=1 >
	 * <tr>
	 * <th>数据库表字段类型</th>
	 * <td>整数</td>
	 * <td>布尔bit</td>
	 * <td>浮点数</td>
	 * <td>字符文本</td>
	 * <td>时间(datetime)</td>
	 * </tr>
	 * <tr>
	 * <th>Java实体类属性类型</th>
	 * <td>int<br>
	 * Integer</td>
	 * <td>boolean<br>
	 * Boolean</td>
	 * <td>double<br>
	 * Double</td>
	 * <td>String</td>
	 * <td>java.util.Date</td>
	 * </tr>
	 * </table>
	 * </div>
	 * 
	 * @param clz
	 *            查询的实体Class类型,<span style=color:red>可以不是Entity的子类</span><BR>
	 *            1.Entity子类<BR>
	 *            2.JPA注解方式
	 * @param <T> the entity class type
	 * @since 2010/11/17
	 * @return List 任何实体类的集合列表
	 */
	public <T> List<T> list(Class<T> clz);

	/**
	 * 返回满足查询条件的实体类的集合列表<br>
	 * <div style=color:gray;> 遵循数据库表结构反射. 实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.<br>
	 * <table border=1 >
	 * <tr>
	 * <th>数据库表字段类型</th>
	 * <td>整数</td>
	 * <td>布尔bit</td>
	 * <td>浮点数</td>
	 * <td>字符文本</td>
	 * <td>时间(datetime)</td>
	 * </tr>
	 * <tr>
	 * <th>Java实体类属性类型</th>
	 * <td>int<br>
	 * Integer</td>
	 * <td>boolean<br>
	 * Boolean</td>
	 * <td>double<br>
	 * Double</td>
	 * <td>String</td>
	 * <td>java.util.Date</td>
	 * </tr>
	 * </table>
	 * </div>
	 * 
	 * @param clz
	 *            查询的实体Class类型,<span style=color:red>可以不是Entity的子类</span>
	 * @param sql
	 *            标准查询sql语句
	 * @param <T> the entity class type
	 * @since 2010/11/17
	 * @return List 任何实体类的集合列表
	 */
	public <T> List<T> list(Class<T> clz, String sql);

	/**
	 * 返回满足查询条件的实体类的集合列表<br>
	 * <div style=color:gray;> 遵循数据库表结构反射. 实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.<br>
	 * <table border=1 >
	 * <tr>
	 * <th>数据库表字段类型</th>
	 * <td>整数</td>
	 * <td>布尔bit</td>
	 * <td>浮点数</td>
	 * <td>字符文本</td>
	 * <td>时间(datetime)</td>
	 * </tr>
	 * <tr>
	 * <th>Java实体类属性类型</th>
	 * <td>int<br>
	 * Integer</td>
	 * <td>boolean<br>
	 * Boolean</td>
	 * <td>double<br>
	 * Double</td>
	 * <td>String</td>
	 * <td>java.util.Date</td>
	 * </tr>
	 * </table>
	 * </div>
	 * 
	 * @param clz
	 *            查询的实体Class类型,<span style=color:red>可以不是Entity的子类</span>
	 * @param sql
	 *            标准查询sql语句
	 * @param params
	 *            sql依赖的具体参数数组
	 * @param <T> the entity class type
	 * @return List 任何实体类的集合列表
	 */
	public <T> List<T> list(Class<T> clz, String sql, String[] params);

	/**
	 * 返回满足查询条件的实体类的集合列表<br>
	 * <div style=color:gray;> 遵循数据库表结构反射. 实体类中的各属性字段与数据库表中的字段名称相同,区分大小写.<br>
	 * <table border=1 >
	 * <tr>
	 * <th>数据库表字段类型</th>
	 * <td>整数</td>
	 * <td>布尔bit</td>
	 * <td>浮点数</td>
	 * <td>字符文本</td>
	 * <td>时间(datetime)</td>
	 * </tr>
	 * <tr>
	 * <th>Java实体类属性类型</th>
	 * <td>int<br>
	 * Integer</td>
	 * <td>boolean<br>
	 * Boolean</td>
	 * <td>double<br>
	 * Double</td>
	 * <td>String</td>
	 * <td>java.util.Date</td>
	 * </tr>
	 * </table>
	 * </div>
	 * 
	 * @param clz
	 *            查询的实体Class类型,<span style=color:red>可以不是Entity的子类</span>
	 * @param condition
	 *            专用于生成带条件的sql语句的对象
	 * @param <T> the entity class type
	 * @since 2010/11/17
	 * @return List 任何实体类的集合列表
	 */
	public <T> List<T> list(Class<T> clz, Condition condition);

	/**
	 * 释放所有资源
	 * 
	 * @param conn
	 *            数据库连接
	 * @param st
	 *            状态集
	 * @param rs
	 *            结果集
	 */
	public void closeAll(Connection conn, Statement st, ResultSet rs);

	/**
	 * 释放与结果集相关的所有资源
	 * 
	 * @param rs
	 *            结果集
	 */
	public void closeAll(ResultSet rs);

	/**
	 * 释放数据库连接
	 * 
	 * @param conn
	 *            数据库连接
	 */
	public void closeConnection(Connection conn);

	/**
	 * 释放st状态集
	 * 
	 * @param st
	 *            状态集
	 */
	public void closeStatement(Statement st);

	/**
	 * 释放rs结果集
	 * 
	 * @param rs
	 *            结果集
	 */
	public void closeResultSet(ResultSet rs);

	/**
	 * 设置日志记录器
	 * 
	 * @param logger
	 *            日志记录器
	 */
	public void setLogger(Logger logger);

	/**
	 * 设置日志记录器
	 * 
	 * @return Logger
	 *            日志记录器
	 */
	public Logger getLogger();

	/**
	 * 设置数据库连接参数配置器
	 * 
	 * @param configurator
	 *            数据库连接参数配置器
	 */
	public void setConfigurator(Configurator configurator);

	/**
	 * 取得数据库连接配置器 configurator
	 * 
	 * @return 数据库连接配置器configurator
	 */
	public Configurator getConfigurator();
}
