package openthinks.libs.sql.data;
import openthinks.libs.sql.data.Column;
/**
 * 默认实现数据列接口的抽象默认列类
 * @author dmj
 *
 */
public class DefaultColumn implements Column {
	/**
	 * 列名称
	 */
	private String name;
	/**
	 * 列值
	 */
	private Object value;
	
	public DefaultColumn(){}
	
	public DefaultColumn(String name,Object value){
		this.name=name;
		this.value=value;
	}
	
	public String toString(){
		return name+":"+value;
	}
	
	@Override
	public String getName() {		
		return name;
	}

	@Override
	public Object getValue() {		
		return value;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public void setValue(Object value) {
		this.value= value;
	}

}
