/**
 * 数据库数据模型包
 * Database data models
 */
package com.openthinks.libs.sql.data;

import com.openthinks.libs.sql.entity.ColumnAttribute;

/**
 * 数据表的一行实体记录接口
 * Represent the structure of row in database table
 * @author dmj
 *
 */
public interface Row {
	/**
	 * 取得该行的第index列的值
	 * @param index 第index列
	 * @return Object 该列的值
	 */
	public Object get(int index);

	/**
	 * 设置index列的值
	 * @param index 第index列
	 * @param e       设置新列的值
	 */
	public void set(int index, Object e);

	/**
	 * 返回该行的列数
	 * @return int 列数
	 */
	public int size();

	/**
	 * 返回所有列的名称
	 * @return ColumnAttribute[] 列名称字符串数组
	 */
	public ColumnAttribute[] getColumnAttributes();

	/**
	 * 返回该行的所有列的集合
	 * @return Column[]
	 */
	public Column[] getColumns();

	/**
	 * 根据列名称返回该列的值
	 * @param columnName  列名称
	 * @return Object 该列的值
	 */
	public Object get(String columnName);

	/**
	 * 设置列名称为columnName的列值为e
	 * @param columnName 列名称
	 * @param e Object
	 */
	public void set(String columnName, Object e);
}
