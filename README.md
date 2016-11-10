# openlibs.sql
The lib of java database ORM simple implementation

#### Get it by Maven

```xml
<dependency>
  <groupId>com.openthinks.libs</groupId>
  <artifactId>sql</artifactId>
  <version>2.0</version>
</dependency>
```
---

#### **Session基本用法**
```java
// get session instance by its factory
Session session = SessionFactory.getSession();
```
```java
// fetch a entity by its key
Message message = session.load(Message.class, "1000");
//TODO
session.close();// close session
```
```java
// save a entity to table
Message message = new Message();
message.setLocale(Locale.CHINA.toString());
message.setContent("HELLO");
message.setMessageId("2000");
session.save(message);
session.close();
```

#### **配置文件和类使用**

 - 配置文件名称: dbconfig.properties
 - 配置文件类: Configurator
 - 配置文件工厂类: ConfiguratorFactory

##### **数据库属性文件**

格式如下:
```properties
#driver name
DRIVER=com.mysql.jdbc.Driver
#database url
URL=jdbc:mysql://localhost:3306/opendb
#database user
USERNAME=root
#database pass
USERPWD=123456
#databse dailect : MYSQL,ORACLE,SQLSERVER
DIALECT=MYSQL
```
##### **配置类用法**

Configurator的属性一一对应配置文件dbconfig.properties里的键
所以可以新建一个配置类实例，替代配置文件:
``` java
Configurator conf = new Configurator();
conf.setDriver("xxx");
conf.setUrl("xxx");
conf.setUserName("xxx");
conf.setUserPwd("xxx");
// option, default value is false
conf.setUsePool(true);
// option, default value is Dialect.MYSQL
conf.setDialect(Dialect.ORACLE);
```
ConfiguratorFactory类负责创建Configurator的实例
```java
// 方法1: 获得默认的配置类实例,将从默认配置文件dbconfig.properties中读取配置并实例化
Configurator fileConf = ConfiguratorFactory.getDefaultInstance();
// 方法2: 获得新的配置类实例,将复制已有的配置实例
Configurator codeConf = ConfiguratorFactory.getInstance(conf);
// 方法3: 重新设置ConfiguratorFactory的默认配置实例,指向已有的配置类实例
ConfiguratorFactory.setConfigurator(conf);
Configurator overrideConf = ConfiguratorFactory.getDefaultInstance();
// 方法4: 读取外部文件实例化配置,配置文件名称可以自由命名
File file = new File("C:\path\filename.properties");
Configurator externalConf = ConfiguratorFactory.getInstance(file);
```

#### **Session高级用法**
##### **实体类定义**
目前支持两种类型的实体类: 继承自Entity类,带有JPA注解的POJO类

com.openthinks.libs.sql.entity.Entity实例简单对应数据库中的表的一条记录,因此要求该实体类定义时注意以下事项:

 1. 该实体类中的字段或属性名需要与对应表列名一样
 2. 该实体类中定义的第一个字段作为对应表的主键
 3. 该实体类的名称需与表名相同(仅在调用Session的高级API)

例子:
```
//table structure
create table message(
    message_id varchar(100),
    message_local varchar(100),
    message_content varchar(1000)
);
```
```java
public class Message extends Entity {
	private String message_id;
	private String message_locale;
	private String message_content;
	public String getContent() {
		return message_content;
	}
	public String getLocale() {
		return message_locale;
	}
	public String getId() {
		return message_id;
	}
	public void setId(String messageId) {
		this.message_id = messageId;
	}
	public void setLocale(String locale) {
		this.message_locale = locale;
	}
	public void setContent(String content) {
		this.message_content = content;
	}
}
```

JPA注解的POJO类没有额外的限制
```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "message")
public class MessageJPA {
	@Id
	@Column(name = "message_id")
	private String messageId;
	@Column(name = "message_locale")
	private String locale;
	@Column(name = "message_content")
	private String content;
	/*
	*getter/setter method
	*/
}
```

##### **增删改**
在Session的DAO API中分为两大类，一类是无需编写SQL(High level);另一类是需要自行构造SQL(Low level).

```java
//<T> void com.openthinks.libs.sql.dhibernate.Session.save(T object)
// High level
Session session = SessionFactory.getSession();
MessageJPA messageJPA = new MessageJPA();
messageJPA.setLocale(Locale.US.toString());
messageJPA.setContent("HELLO");
messageJPA.setMessageId("2000");
session.save(messageJPA);
Message messageEntity = new Message();
messageEntity.setLocale(Locale.CHINA.toString());
messageEntity.setContent("你好");
messageEntity.setId("3000");
session.save(messageEntity);

//int com.openthinks.libs.sql.dhibernate.Session.add(String sql, String[] params)
// Low level
String saveSQL = "INSERT INTO message(message_id,message_locale,message_content) values(?,?,?)";
String[] params = {"4000","en_US","Hello again"};
int affectRow = session.add(saveSQL,params);
session.close();
```

```java
//<T> void com.openthinks.libs.sql.dhibernate.Session.update(T object)
Session session = SessionFactory.getSession();
MessageJPA message = new MessageJPA();
message.setLocale(Locale.CHINA.toString());
message.setContent("中国你好");
message.setMessageId("2000");
session.update(message);

//int com.openthinks.libs.sql.dhibernate.Session.update(String sql, String[] params)
String updateSQL = "update message set message_content = ? where message_id =? and message_locale=?";
String[] params = {"您好","zh_CN","2000"};
session.update();
session.close();
```

```java
//<T> void com.openthinks.libs.sql.dhibernate.Session.delete(T object)
MessageJPA messageJPA = new MessageJPA();
messageJPA.setLocale(Locale.US.toString());
messageJPA.setContent("HELLO");
messageJPA.setMessageId("2000");
Session session = SessionFactory.getSession();
session.delete(messageJPA);

//int com.openthinks.libs.sql.dhibernate.Session.delete(String sql, String[] params)
String deleteSQL = "delete message where message_id =? and message_locale=?";
String[] params = {"zh_CN","2000"};
session.delete(deleteSQL,params);
session.close();
```
##### **条件查询**
对于简单根据单一主键获取记录或获取所有表中记录,推荐使用High level API:
```
// 根据id值获取clz类型的实体对象
<T> T com.openthinks.libs.sql.dhibernate.Session.load(Class<T> clz, Serializable id)
// 获取所有相应实体类的集合列表
<T> List<T> com.openthinks.libs.sql.dhibernate.Session.list(Class<T> clz)
```
而对于一些复杂的查询，可以使用接下来的方式
###### **直接SQL语句查询**
```java
Session session = SessionFactory.getSession();
String querySQL = "SELECT * FROM message WHERE message_id = ? and message_locale= ? ";
MessgaeJPA messageJPA = session.get(MessageJPA.class, querySQL,new String[]{"2000","zh_CN"});
//Messgae message = session.get(Message.class, querySQL,new String[]{"2000","zh_CN"});
List<MessageJPA> list = session.list(MessageJPA.class, "SELECT * FROM message",new String[]{});
//List<Message> list = session.list(Message.class, "SELECT * FROM message",new String[]{});
// 不指定实体类型时,将使用 openthinks.libs.sql.data.Row
List<Row> rows = session.list("SELECT * FROM message",new String[]{});
session.close();
```
###### **简单条件类查询**
简单条件类: com.openthinks.libs.sql.lang.Condition是非常底层的,也属于Low level.
Condition对象可以如下创建:
```
Session session = SessionFactory.getSession();
session.createCondition(); // same as Condition.build()
```
```
// where 1=1
Condition condition1 = Condition.build(); 
// SELECT * FROM message where 1=1
Condition condition2 = Condition.build("SELECT * FROM message");
// SELECT * FROM message where 1=1
Condition condition3 = Condition.build(MessageJPA.class);
```
如何使用:(暂支持 绝对匹配 *=*,模糊匹配 *like*,开始于 *>*,结束于 *<*)
参考以下 test case:
```java
public class ConditionTest {
	private static MessageJPA message = new MessageJPA();
	private static MessageJPA message2 = new MessageJPA();
	@BeforeClass
	public static void setUp() {
		Session session = SessionFactory.getSession();
		message.setMessageId("CONDITION_1");
		message.setLocale(Locale.US.toString());
		message.setContent("Condition class regular test");
		session.save(message);

		message2.setMessageId("CONDITION_2");
		message2.setLocale(Locale.US.toString());
		message2.setContent("Condition class other test");
		session.save(message2);
		session.close();
	}
	@Test
	public void regularTest() {
		Session session = SessionFactory.getSession();
		Condition condition = session.createCondition();
		condition.setSqlPart("SELECT * FROM message");
		// 等效于 Condition.build("SELECT * FROM message");
		
		//第一个参数为条件类型;第二个参数为表字段;第三参数为对应的条件值
		condition.addItem(Condition.ABSMATCH, "message_id", "CONDITION_1");
		MessageJPA msg = session.get(MessageJPA.class, condition);
		Assert.assertNotNull(msg);
		Assert.assertEquals(message.getContent(), msg.getContent());
		session.close();
	}
	@Test
	public void otherTest() {
		Condition condition = Condition.build(MessageJPA.class)
				.addItem(Condition.LIKEMATCH, "message_id", "CONDITION%")
				.addItem(Condition.ORDER, "message_id", Condition.Order.DESC);//排序
		Session session = SessionFactory.getSession();
		List<MessageJPA> list = session.list(MessageJPA.class, condition);
		Assert.assertTrue(list.size() == 2);
		Assert.assertEquals(message2.getMessageId(), list.get(0).getMessageId());
		session.close();
	}
	@AfterClass
	public static void tearDown() {
		Session session = SessionFactory.getSession();
		session.delete(message);
		session.delete(message2);
		session.close();
	}
}
```
###### **智能QueryFilter查询**
目前实现的filter有 

> Filters.eq, Filters.neq
> Filters.include, Filters.startWith, Filters.endWith
> Filters.and, Filters.or, Filters.group

```java
Session session =  SessionFactory.getSession();
// 获取Query对象
Query<MessageJPA> query = session.createQuery(MessageJPA.class);
// 定义具体的QueryFilter
QueryFilterGroup group = Filters.group();
group.push(Filters.eq("messageId", "4000"));
group.push(Filters.or());
QueryFilterGroup embedGrp = Filters.group();
embedGrp.push(Filters.eq("messageId", "2000"));
embedGrp.push(Filters.eq("locale", "zh_CN"));
group.push(embedGrp);
// 最终SQL(MYSQL):
// select * from `message` where  ( `message_id` = '4000'  or  ( `message_id` = '2000'  and message_locale = 'zh_CN')  ) 
// 添加到Query对象
query.addFilter(group);
List<MessageJPA> list = query.execute();
session.close();
```
##### **简单事务**

 - 开启事务 `session.beginTransaction();` 另外
   `session.beginTransaction(TransactionLevel.TRANSACTION_READ_UNCOMMITTED)`
   设置事务级别
 - 关闭事务 `session.endTransaction();` 另外 `session.close();`
   也会默认关闭事务,如果开启的话.

例子:
```java
public class TransactionTest {
	private static MessageJPA message = new MessageJPA();
	@BeforeClass
	public static void setUp() {
		Session session = SessionFactory.getSession();
		message.setMessageId("TRANSACTION");
		message.setLocale(Locale.US.toString());
		message.setContent("Transaction test");
		session.save(message);
		session.close();
	}
	@Test
	public void beginTransactionTest() throws TransactionException {
		Session session = SessionFactory.getSession();
		session.beginTransaction();
		message.setContent("Transaction test rollback");
		session.update(message);
		int result = session.add("insert into message");//错误语法,新增不成功
		if (result == 0) {
			session.rollback();
		} else {
			session.commit();
		}
		session.endTransaction();
        
		MessageJPA msg = session.load(MessageJPA.class, message.getMessageId());
		Assert.assertNotNull(msg);
		Assert.assertEquals("Transaction test", msg.getContent());
		session.close();
	}
	@AfterClass
	public static void tearDown() {
		Session session = SessionFactory.getSession();
		session.delete(message);
		session.close();
	}
}
```

##### **连接池**
目前支持简单的连接池实现 `com.openthinks.libs.sql.dao.pool.impl.SimpleConnectionPool`;
可以自行扩展 `com.openthinks.libs.sql.dao.pool.ConnectionPool`接口,并替换默认的实现:
```java
MyConnectionPool connPool = new MyConnectionPool();// your implemented ConnectionPool
ConnectionPoolManager.setSingletonPoolInstance(connPool);// make it replace the default pool
```
当然使用连接池前,必须配置,在配置文件 dbconfig.properties 中, 加入:
```
USEPOOL=true
#可选,不配的话默认无限制
MAX_ACTIVE=100
#可选,不配的话默认无限制
MAX_IDLE=10
```
对于扩展的ConnectionPool, 除了如上所述使用Code替换默认实现, 亦可以在配置文件中指明实现类名称
```
USEPOOL=true
POOL_CLASS=com.openthinks.libs.sql.MyConnectionPool
```
需要注意事项是构造函数的定义:

 1. MyConnectionPool(Configuration) 和 MyConnectionPool() 至少需要定义一个
 2. MyConnectionPool() 需要自行获取配置类,以确保获取到数据库连接
 3. 优先使用 MyConnectionPool(Configuration) 构造函数
