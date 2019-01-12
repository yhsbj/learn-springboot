@[spring boot使用入门]()


>快速入门：[https://projects.spring.io/spring-boot/#quick-start](https://projects.spring.io/spring-boot/#quick-start)

>文档：[https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/](https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/)

# spring boot入门
## 搭建HTTP服务
- 创建maven工程，引入依赖

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.example</groupId>
	<artifactId>myproject</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<!-- Inherit defaults from Spring Boot -->
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.1.RELEASE</version>
	</parent>

	<!-- Add typical dependencies for a web application -->
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	</dependencies>

</project>
```
- 创建启动类1

```
/**
* @EnableAutoConfiguration springboot的注解 会这个注解告诉Spring Boot根据添加的jar依赖猜测你想如何配置Spring上下文。
* 由于spring-boot-starter-web添加了Tomcat和Spring MVC，所以auto-configuration将假定你正在开发一个web应用，
* 并对Spring* 进行相应地设置
* @SpringBootApplication包含@EnableAutoConfiguration、@ComponentScan
* 其中@ComponentScan扫描注册范围，使@bean等其他注入大盘spring容器，默认扫描当前包及子包
*/
@Controller
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
```

- 测试

>http://ip:port/

>Hello World!

- 创建启动类2

```
/**
* @EnableAutoConfiguration springboot的注解 会这个注解告诉Spring Boot根据添加的jar依赖猜测你想如何配置Spring上下文。
* 由于spring-boot-starter-web添加了Tomcat和Spring MVC，所以auto-configuration将假定你正在开发一个web应用，
* 并对Spring* 进行相应地设置
* @SpringBootApplication包含@EnableAutoConfiguration、@ComponentScan
* 其中@ComponentScan扫描注册范围，使@bean等其他注入大盘spring容器，默认扫描当前包及子包
*/
@Controller
@EnableAutoConfiguration
public class Starter{

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
```
```
//子包下的controller
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("test")
    private String test() {
        return "Hello World";
    }

}
```

- 测试

>http://ip:port/user/test

>Hello user!


## 整合mybatis mysql
- 引入依赖jar

```
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.3.2</version>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.0.15</version>
</dependency>
```

- 注入德鲁伊druid数据源

```
// 将druid注入到spring 容器
@Configuration
public class Config {

    /**
     * 注册德鲁伊监控 url对应的页面
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> reg = new ServletRegistrationBean<>();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", "druid");
        reg.addInitParameter("loginPassword", "druid");
        reg.addInitParameter("logSlowSql", "true");
        return reg;
    }

    /**
     * 注册德鲁伊统计拦截器
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    /**
     * 注册德鲁伊数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "druid")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

}
```

- sql通用配置 mybatis-cfg.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD SQL Map Config 3.0//EN"  
	"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!-- 开启驼峰匹配 -->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!-- 这个配置使全局的映射器启用或禁用缓存。系统默认值是true，设置只是为了展示出来 -->
        <setting name="cacheEnabled" value="true" />
        <!-- 全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载。 系统默认值是true，设置只是为了展示出来 -->
        <setting name="lazyLoadingEnabled" value="true" />
        <!-- 允许或不允许多种结果集从一个单独的语句中返回（需要适合的驱动）。 系统默认值是true，设置只是为了展示出来 -->
        <setting name="multipleResultSetsEnabled" value="true" />
        <!--使用列标签代替列名。不同的驱动在这方便表现不同。参考驱动文档或充分测试两种方法来决定所使用的驱动。 系统默认值是true，设置只是为了展示出来 -->
        <setting name="useColumnLabel" value="true" />
        <!--允许 JDBC 支持生成的键。需要适合的驱动。如果设置为 true 则这个设置强制生成的键被使用，尽管一些驱动拒绝兼容但仍然有效（比如 
            Derby）。 系统默认值是false，设置只是为了展示出来 -->
        <setting name="useGeneratedKeys" value="false" />
        <!--配置默认的执行器。SIMPLE 执行器没有什么特别之处。REUSE 执行器重用预处理语句。BATCH 执行器重用语句和批量更新 系统默认值是SIMPLE，设置只是为了展示 -->
        <setting name="defaultExecutorType" value="SIMPLE" />
        <!--设置超时时间，它决定驱动等待一个数据库响应的时间。 系统默认值是null，设置只是为了展示出来 -->
        <setting name="defaultStatementTimeout" value="10000" />
    </settings>
</configuration> 
```

- 配置文件

> application.yml或application.properties默认位置在classpath:/config/application*、classpath:/application*

>目前2.0的spb不支持druid的数据源的线程池配置，在DataSourceBuilder类里面有支持列表：com.zaxxer.hikari.HikariDataSource、org.apache.tomcat.jdbc.pool.DataSource、org.apache.commons.dbcp2.BasicDataSource三种

```
# application.properties
#dataSource 
#druid Poo 【dataSource &useSSL=true】
druid.url=jdbc:mysql://ip:3306/x?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
druid.driver-class-name=com.mysql.jdbc.Driver
druid.username=root
druid.password=passwd
druid.type=com.alibaba.druid.pool.DruidDataSource
druid.testWhileIdle=true
druid.initialSize=1  
druid.minIdle=1  
druid.maxActive=20  
druid.maxWait=60000  
druid.timeBetweenEvictionRunsMillis=60000     
druid.minEvictableIdleTimeMillis=300000  
druid.validationQuery=SELECT 1 FROM DUAL 
druid.testWhileIdle=true  
druid.testOnBorrow=false  
#druid.exceptionSorter=true  
druid.testOnReturn=false  
druid.poolPreparedStatements=true
druid.filters= stat,wall
druid.maxPoolPreparedStatementPerConnectionSize=20  
druid.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500  
# 合并多个DruidDataSource的监控数据
druid.useGlobalDataSourceStat=true  

#mybatis
mybatis.mapper-locations=classpath:mybatis/*/*.xml
mybatis.config-location=classpath:mybatis/mybatis-cfg.xml
```

- mapper代码

```
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface Mapper{
  int insert(Object o);
}
```

- demo code
>https://github.com/yhsbj/learn-springboot.git


- 数据库IO测试
从controller 到 service 到dao 到数据库，一整套的测试

## 事务
>spring boot无xml化，只用在需要事务的类、方法上增加@Transactional注解即可

### 事务隔离级别
>隔离级别是指若干个并发的事务之间的隔离程度。TransactionDefinition 接口中定义了五个表示隔离级别的常量

- TransactionDefinition.ISOLATION_DEFAULT：这是默认值，表示使用底层数据库的默认隔离级别。对大部分数据库而言，通常这值就是TransactionDefinition.ISOLATION_READ_COMMITTED。
- TransactionDefinition.ISOLATION_READ_UNCOMMITTED：该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据。该级别不能防止脏读，不可重复读和幻读，因此很少使用该隔离级别。比如PostgreSQL实际上并没有此级别。
- TransactionDefinition.ISOLATION_READ_COMMITTED：该隔离级别表示一个事务只能读取另一个事务已经提交的数据。该级别可以防止脏读，这也是大多数情况下的推荐值。
- TransactionDefinition.ISOLATION_REPEATABLE_READ：该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询，并且每次返回的记录都相同。该级别可以防止脏读和不可重复读。
- TransactionDefinition.ISOLATION_SERIALIZABLE：所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。

### 事务传播行为

>所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。在TransactionDefinition定义中包括了如下几个表示传播行为的常量：

- TransactionDefinition.PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
- TransactionDefinition.PROPAGATION_REQUIRES_NEW：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
- TransactionDefinition.PROPAGATION_SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
- TransactionDefinition.PROPAGATION_NOT_SUPPORTED：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
- TransactionDefinition.PROPAGATION_NEVER：以非事务方式运行，如果当前存在事务，则抛出异常。
- TransactionDefinition.PROPAGATION_MANDATORY：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
- TransactionDefinition.PROPAGATION_NESTED：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。


## junit测试
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
```
```
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Starter.class)
public class JunitTest {
    @Autowired
    private UserService userSvc;
    @Test
    public void test1() {

    }
}
```















