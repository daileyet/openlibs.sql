package openthinks.libs.sql.data;

/**
 *  数据表的列接口
 * @author dmj
 *
 */
public interface Column {
	/**
	 * 取得该列的列名称
	 * @return String 该列的列名称
	 */
	public String getName();

	/**
	 *设置列名称 
	 * @param name String
	 */
	public void setName(String name);

	/**
	 * 取得该列的值
	 * @return Object 列值
	 */
	public Object getValue();

	/**
	 * 设置列的值
	 * @param value Object
	 */
	public void setValue(Object value);
}
