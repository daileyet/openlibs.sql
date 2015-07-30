# openlibs.sql
The lib of java database ORM simple implementation

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

配置文件名称:&nbsp;&nbsp;&nbsp;&nbsp;dbconfig.properties
配置文件类:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Configurator
配置文件工厂类: ConfiguratorFactory

##### **数据库属性文件**

格式如下:
```
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
Configurator extenConf = ConfiguratorFactory.getInstance(file);
```

#### **Session高级用法**
##### **实体类定义**
##### **增删改**

##### **查询**
###### **直接SQL语句查询**
###### **简单条件类查询**
###### **智能QueryFilter查询**

##### **简单事务**

##### **连接池**
