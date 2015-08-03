package openthinks.libs.sql.lang;

import java.util.HashMap;
import java.util.Map;

import openthinks.libs.sql.entity.Entity;
import openthinks.libs.sql.lang.reflect.ReflectEngine;
import openthinks.libs.utilities.Checker;

/**
 * 条件类
 * 
 * @author dmj
 * @version 2010/06/10
 */
public class Condition {
	private Map<String, String> begin; // 开始
	private Map<String, String> end; // 结束
	private Map<String, String> absMatch; // 绝对匹配
	private Map<String, String> likeMatch; // 模糊匹配
	private Map<String, String> order; // 排序

	private Order defaultOrder = Order.ASC;// 升序排序常量字段

	/**
	 * Sort by order
	 */
	public enum Order {
		ASC, DESC;
	}

	/**
	 * 条件类型常量
	 */
	public static final int BEGIN = 0;// 开始即大于
	public static final int END = 1;// 结束即小于
	public static final int ABSMATCH = 2;
	public static final int LIKEMATCH = 3;
	public static final int ORDER = 4;

	private String sqlPart = null;// 前部分sql语句

	/**
	 * 设置前部分sql语句
	 * 
	 * @param sqlPart
	 *            前部分sql语句
	 * @return Condition
	 */
	public Condition setSqlPart(String sqlPart) {
		if (sqlPart == null)
			sqlPart = "";
		else
			this.sqlPart = sqlPart;
		return this;
	}

	/**
	 * 获取前部分sql语句
	 * 
	 * @return String 前部分sql语句
	 */
	public String getSqlPart() {
		return sqlPart;
	}

	/**
	 * 创建只查询有条件的Condition对象
	 */
	private Condition() {
		this("");
	}

	/**
	 * build the Condition which only has condition part(WHERE)
	 * @return Condition
	 */
	public static Condition build() {
		return new Condition();
	}

	/**
	 * build the complete Condition which both has condition part WHERE  and body part UPDATE,SELECT,DELETE
	 * @param sqlPart String body part UPDATE,SELECT,DELETE etc.
	 * @return Condition
	 */
	public static Condition build(String sqlPart) {
		return new Condition(sqlPart);
	}

	/**
	 * build the complete Condition which both has condition part WHERE  and body part UPDATE,SELECT,DELETE 
	 * @param queryObject Class 
	 * 	 		 实体类型<BR>
	 *            1.{@link Entity}子类默认第一个属性为ID列,类名需与表名一致<BR>
	 *            2.JPA标注的实体类标准 
	 * @return Condition
	 */
	public static Condition build(Class<?> queryObject) {

		return new Condition(queryObject);
	}

	/**
	 * 创建带前部分sql语句的Condition对象
	 * 
	 * @param sqlPart
	 */
	private Condition(String sqlPart) {
		setSqlPart(sqlPart);
		begin = new HashMap<String, String>();
		end = new HashMap<String, String>();
		absMatch = new HashMap<String, String>();
		likeMatch = new HashMap<String, String>();
		order = new HashMap<String, String>();
	}

	/**
	 * auto build the front query sql by the given parameter {@link Class}
	 * This parameter {@link Class} must be {@link Entity} or its sub class, or the class which include the annotation of JPA
	 * @param queryObject Class
	 */
	public Condition(Class<?> queryObject) {
		this();
		String persistName = ReflectEngine.getEntityTable(queryObject);
		Checker.require(persistName).notNull();
		setSqlPart("SELECT * FROM " + persistName);
	}

	/**
	 * 增加查询条件
	 * 
	 * @param type
	 *            :Integer 查询条件类型
	 * @param key
	 *            :String 查询条件字段
	 * @param value
	 *            :String 查询条件值
	 * @return Condition
	 */
	public Condition addItem(final int type, String key, String value) {
		if (getCondition(type) != null)
			getCondition(type).put(key, value);
		return this;
	}

	/**
	 * 增加查询条件
	 * 
	 * @param type
	 *            :int 查询条件类型
	 * @param key
	 *            :String 查询条件字段
	 * @param value
	 *            :Object 查询条件值
	 * @return Condition
	 */
	public Condition addItem(final int type, String key, Object value) {
		return addItem(type, key, value.toString());
	}

	/**
	 * 获取查询条件映射
	 * 
	 * @param type
	 *            :查询条件类型
	 * @return Map<String,String> :查询条件映射
	 */
	private Map<String, String> getCondition(final int type) {
		Map<String, String> ret = null;
		switch (type) {
		case 0:
			ret = this.getBegin();
			break;
		case 1:
			ret = this.getEnd();
			break;
		case 2:
			ret = this.getAbsMatch();
			break;
		case 3:
			ret = this.getLikeMatch();
			break;
		case 4:
			ret = this.getOrder();
			break;
		}
		return ret;
	}

	/**
	 * 清空相应类型的查询条件映射
	 * 
	 * @param type
	 *            :查询条件类型
	 * @return Condition
	 */
	public Condition clearCondtitionItem(final int type) {
		if (getCondition(type) != null)
			getCondition(type).clear();
		return this;
	}

	/**
	 * 查询相应的查询条件映射是否为空
	 * 
	 * @param type
	 *            :查询条件类型
	 * @return boolean
	 */
	public boolean empty(final int type) {
		return getCondition(type) == null ? true : getCondition(type).isEmpty();
	}

	/**
	 * 查询相应的查询条件映射的个数
	 * 
	 * @param type
	 *            :查询条件类型
	 * @return int
	 */
	public int size(final int type) {
		return getCondition(type) == null ? 0 : getCondition(type).size();
	}

	/**
	 * 返回完整的带条件的sql语句
	 * 
	 * @param condition
	 *            :查询条件对象
	 * @return String :带完整的查询条件的查询改删SQL语句
	 */
	public static String getFullSql(Condition condition) {
		return condition == null ? null : getFullSql(condition.getSqlPart(), condition);
	}

	/**
	 * 返回完整的查询条件语句
	 * 
	 * @param sqlpart
	 *            :部分查询改删SQL语句
	 * @param condition
	 *            :查询条件对象
	 * @return String :带完整的查询条件的查询改删SQL语句
	 */
	public static String getFullSql(String sqlpart, Condition condition) {
		StringBuffer ret = new StringBuffer("");
		if (sqlpart != null && sqlpart.toLowerCase().indexOf("where") == -1) {
			condition.setSqlPart(sqlpart);
			ret.append(" where 1=1 ");
		}
		if (condition != null) {
			if (!condition.empty(Condition.ABSMATCH)) {// 绝对匹配
				for (String key : condition.getAbsMatch().keySet()) {
					String value = condition.getAbsMatch().get(key);
					ret.append(" and " + key + " = '" + value + "'");
				}
			}
			if (!condition.empty(Condition.LIKEMATCH)) {// 模糊匹配
				for (String key : condition.getLikeMatch().keySet()) {
					String value = condition.getLikeMatch().get(key);
					ret.append(" and " + key + " like '" + value + "'");
				}
			}
			if (!condition.empty(Condition.BEGIN)) {// 起始范围
				for (String key : condition.getBegin().keySet()) {
					String value = condition.getBegin().get(key);
					ret.append(" and " + key + " > '" + value + "'");
				}
			}
			if (!condition.empty(Condition.END)) {// 结束位置
				for (String key : condition.getEnd().keySet()) {
					String value = condition.getEnd().get(key);
					ret.append(" and " + key + " < '" + value + "'");
				}
			}
			if (!condition.empty(Condition.ORDER)) {
				ret.append(" order by ");
				for (String key : condition.getOrder().keySet()) {
					ret.append(key);
					ret.append(" ");
					String value = condition.getOrder().get(key);
					if (value == null) {
						ret.append(condition.getDefaultOrder());
					} else {
						ret.append(value);
					}
					ret.append(",");
				}
				ret.deleteCharAt(ret.length() - 1);

			}
		}
		return sqlpart + ret;
	}

	private Map<String, String> getBegin() {
		return begin;
	}

	private Map<String, String> getEnd() {
		return end;
	}

	private Map<String, String> getLikeMatch() {
		return likeMatch;
	}

	private Map<String, String> getOrder() {
		return order;
	}

	private Map<String, String> getAbsMatch() {
		return absMatch;
	}

	/**
	 * 是否默认升序排序顺序
	 * 
	 * @return boolean
	 */
	public Order getDefaultOrder() {
		return defaultOrder;
	}

	/**
	 * 设置是否默认升序排序顺序
	 * 
	 * @param defaultOrder
	 *            :boolean
	 * @return Condition
	 */
	public Condition setDefaultOrder(Order defaultOrder) {
		this.defaultOrder = defaultOrder;
		return this;
	}

}
