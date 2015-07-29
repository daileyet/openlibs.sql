package openthinks.libs.sql.data;

import openthinks.libs.sql.data.Row;
import openthinks.libs.sql.entity.ColumnAttribute;

/**
 * 实现数据行接口的抽象行类
 * 
 * @author dmj
 */
public abstract class AbstractRow implements Row {

	/**
	 * 所有列的集合
	 */
	protected Column[] columns = null;

	/**
	 * 返回该行中所有列的字符串表示
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			buff.append(getColumns()[i]);
			buff.append("\n");
		}
		return buff.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(int index) {
		if (index < 0 || index >= size())
			throw new ArrayIndexOutOfBoundsException(index);
		return getColumns() == null ? null : getColumns()[index].getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int index, Object e) {
		if (index < 0 || index >= size())
			throw new ArrayIndexOutOfBoundsException(index);
		set(getColumnAttributes()[index].getColumnName(), e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return getColumnAttributes() == null ? 0 : getColumnAttributes().length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Column[] getColumns() {
		if (columns == null) {
			columns = new Column[getColumnAttributes().length];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = new DefaultColumn(getColumnAttributes()[i].getColumnName(),
						this.get(getColumnAttributes()[i].getColumnName()));
			}
		}
		return columns;
	}

	// ----------------------------抽象方法------------------------------------//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract ColumnAttribute[] getColumnAttributes();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void set(String columnName, Object e);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Object get(String columnName);

}
