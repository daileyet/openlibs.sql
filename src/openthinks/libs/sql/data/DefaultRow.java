package openthinks.libs.sql.data;

import openthinks.libs.sql.entity.ColumnAttribute;

/**
 * 默认实现数据行接口的抽象默认行类
 * @author dmj
 */
public class DefaultRow extends AbstractRow {

	/**
	 * 列的名称字符串数组
	 */
	private ColumnAttribute[] columnNams;
	/**
	 * 所有列值的数组
	 */
	private Object[] values;

	/**
	 * 构造方法
	 * @param columnNams 列的名称字符串数组
	 * @param values 所有列值的数组
	 */
	public DefaultRow(final ColumnAttribute[] columnNams, final Object[] values) {
		this.columnNams = columnNams;
		this.values = values;
	}

	@Override
	public Object get(String columnName) {
		for (int index = 0; index < getColumnAttributes().length; index++) {
			if (columnName.equalsIgnoreCase(getColumnAttributes()[index].getColumnName())) {
				return getColumns()[index].getValue();
			}
		}
		return null;
	}

	@Override
	public ColumnAttribute[] getColumnAttributes() {
		return columnNams;
	}

	@Override
	public void set(String columnName, Object e) {
		for (int index = 0; index < getColumnAttributes().length; index++) {
			if (columnName.equalsIgnoreCase(getColumnAttributes()[index].getColumnName())) {
				getColumns()[index].setValue(e);
				break;
			}
		}
	}

	@Override
	public Column[] getColumns() {
		if (this.columns == null) {
			this.columns = new Column[getColumnAttributes().length];
			for (int i = 0; i < this.columns.length; i++) {
				this.columns[i] = new DefaultColumn(getColumnAttributes()[i].getColumnName(), values[i]);
			}
		}
		return this.columns;
	}
}
