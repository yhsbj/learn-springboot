@[spring boot完善]()

# spring boot完善

## 日志组件

- slf4j 切面抽象

```
Logger log = LoggerFactory.getLogger(TestLog4J.class);
log.info("info {}", "boy!");
log.debug("debuging...");
```

- logback spring boot默认日志组件

> [https://logback.qos.ch/manual/layouts.html](https://logback.qos.ch/manual/layouts.html)

```
<dependency>
	<groupId>ch.qos.logback</groupId>
	<artifactId>logback-classic</artifactId>
	<version>1.2.3</version>
</dependency>
```

- logback.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!-- debug="true" : 打印logback内部状态（默认当logback运行出错时才会打印内部状态 ），配置该属性后打印条件如下（同时满足）： 
	1、找到配置文件 2、配置文件是一个格式正确的xml文件 也可编程实现打印内部状态，例如： LoggerContext lc = (LoggerContext) 
	LoggerFactory.getILoggerFactory(); StatusPrinter.print(lc); scan="true" ： 
	自动扫描该配置文件，若有修改则重新加载该配置文件 scanPeriod="30 seconds" : 配置自动扫面时间间隔（单位可以是：milliseconds, 
	seconds, minutes or hours，默认为：milliseconds）， 默认为1分钟，scan="true"时该配置才会生效 packagingData="true"可以在日志后看到依赖jar包名和版本，很费性能，不建议开启 -->
<configuration debug="false" scan="false"
	scanPeriod="30 seconds" packagingData="false">
	<!-- 设置 logger context 名称,一旦设置不可改变，默认为default -->
	<property name="home" value="logs" />
	<property name="name" value="frame" />
	<contextName>${name}</contextName>

	<appender name="console"
		class="ch.qos.logback.core.ConsoleAppender">
		<!-- encoders are by default assigned the type ch.qos.logback.classic.encoder.PatternLayoutEncoder -->
		<encoder>
			<pattern>%d [%-5level] [%thread] %logger{0}.%M.%L - %msg%n</pattern>
		</encoder>
	</appender>

	<appender name="dayFile"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 当前活动日志文件名 -->
		<file>${home}/${name}.log</file>
		<!-- 文件滚动策略根据%d{patter}中的“patter”而定，此处为每天产生一个文件 -->
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 归档文件名“.zip或.gz结尾”,表示归档文件自动压缩 -->
			<FileNamePattern>${home}/${name}%d{yyyyMMdd}.log.gz</FileNamePattern>
			<maxHistory>30</maxHistory>
		</rollingPolicy>
		<encoder>
			<pattern>%d [%-5level] [%thread] %logger{0}.%M.%L - %msg%n</pattern>
		</encoder>
	</appender>

<!-- springProfile 在application.properties先加载时有用。 
  加载优先级 logback > application > logback-spring -->
	<springProfile name="prod">
		<!-- logger使用匹配度最大的，降级到root，找到logger为止 -->
		<!-- addtivity:是否向上级loger传递打印信息。默认是true。 -->
		<logger name="com.line" level="INFO" additivity="false">
			<appender-ref ref="dayFile" />
		</logger>
		<root level="WARN">
			<appender-ref ref="dayFile" />
		</root>
	</springProfile>

	<springProfile name="test">
		<root level="INFO">
			<appender-ref ref="console" />
		</root>
	</springProfile>
</configuration>
```

## 关于测试，mock，生产多环境的配置问题

- application.properties

>spring.profiles.active=@profileActive@

- application-dev.properties

- application-test.properties

- application-prod.properties

- logback-spring.xml

- maven pom.xml 加载多环境文件

```
<!--
第一个resource加载目录下不符合application*.properties的所有资源文件
第一个resource加载目录下只符合两条include的资源文件
-->
<build>
		<resources>
			<resource>
				<directory>src/main/resources</directory>
				<excludes>
					<exclude>application*.properties</exclude>
				</excludes>
			</resource>
			<resource>
				<filtering>true</filtering>
				<directory>src/main/resources</directory>
				<includes>
					<include>application-${profileActive}.properties</include>
					<include>application.properties</include>
				</includes>
			</resource>
		</resources>
<build>
```

## springboot jackson配置

- Date的配置

```
# 时间转化为long型
spring.jackson.serialization.write-dates-as-timestamps=true
```
```
# 时间转化为相关格式
spring.jackson.serialization.write-dates-as-timestamps=true
spring.jackson.time-zone=GMT+8
```
或者
```
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date curr;
```

- 空-> ""   |  空 -> []

> 未找到spring的支持配置，如下是自定义增加部分

```
//仿照MappingJackson2HttpMessageConverter的bean提供，其中增加ObjectMapper的key
@Configuration
public class MappingJackson2HttpMessageConverterConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new BeanSerializerModifier() {

            private JsonSerializer<Object> nullJson = new JsonSerializer<Object>() {

                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString("");
                }
            };

            private JsonSerializer<Object> nullArr = new JsonSerializer<Object>() {

                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    if (value == null) {
                        gen.writeStartArray();
                        gen.writeEndArray();
                    }
                }
            };

            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

                for (int i = 0; i < beanProperties.size(); i++) {
                    BeanPropertyWriter writer = beanProperties.get(i);
                    // 判断字段的类型，如果是array，list，set则注册nullSerializer
                    JavaType type = writer.getType();
                    if (type.isArrayType() || type.isCollectionLikeType()) {
                        // 给writer注册一个自己的nullSerializer
                        writer.assignNullSerializer(nullArr);
                    } else {
                        writer.assignNullSerializer(nullJson);
                    }
                }

                return super.changeProperties(config, beanDesc, beanProperties);
            }

        }));

        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
```

