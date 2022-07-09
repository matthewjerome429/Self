mb-bizrule
==========
MicroService using springBoot providing MMB update/query interface。

########## environment required
1. JDK1.8
2. Redis
3. Mysql
4. Maven, dependency jars:
	 - mb-common-*.jar
	 - oneaconsumer-*.jar
	 - eodsconsumer-*.jar

########## project construction
├── src
	├── main
		├── java			// main java class
		├── resources
			├── config		// application and log configurations
			├── keystore	// key for calling third-party interface
	├── test
		├── java			// JUnit test class
		├── resources		// XML format data for JUnit test
├── pom.xml					// maven dependency configuration
├── README.md				// help

########## development environment
1. add "spring.profiles.active=local" into application.properties.

2. set properties below in application-local.properties:
spring.datasource.url=jdbc:${HOST}:${PORT}/${DB_name}
spring.datasource.username=${MYSQL_DB_USERNAME}
spring.datasource.password=${MYSQL_DB_PW}
spring.redis.host=${REDIS_HOST}
spring.redis.port=${REDIS_PORT}
spring.redis.database=${REDIS_DATABASE:0}

3. find Application.java => run as => Java Application.

4. open "http://localhost:8082/swagger-ui.html".

########## test
project root => run as => "Junit Test".

