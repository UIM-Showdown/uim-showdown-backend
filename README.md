# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.3/reference/web/servlet.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.3/reference/web/spring-security.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.3/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Data Reactive Redis](https://docs.spring.io/spring-boot/3.4.3/reference/data/nosql.html#data.nosql.redis)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.4.3/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.4.3/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.3/reference/using/devtools.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* mysql: [`mysql:8.4.4`](https://hub.docker.com/layers/library/mysql/8.4.4/images/sha256-60832e27fa98532ef7b75e634b065dd3809fcfbbe0dc591d6adf30a386d4dcbe)
* redis: [`redis:7.4.2-alpine`](https://hub.docker.com/layers/library/redis/7.4.2-alpine/images/sha256-9cabfa9c15e13f9e4faee0f80d4373cd76e7b8d5a678b9036402b1b0ed9c661b)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# Local Development

### Dependencies
- [Java SE 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK 17](https://formulae.brew.sh/formula/openjdk@17)
- [Docker Desktop](https://www.docker.com/get-started/) (if using Docker)
- [Redis 7.4.2](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/) (if not using Docker)
- [MySQL 8.4.4](https://dev.mysql.com/downloads/mysql/8.4.html) (if not using Docker)

### Serving the Application via Docker
**NOTE:** Everytime changes are made, a new image build and container build are required.

1. Build the application image using the [`build-image` maven plugin](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
```
./mvnw spring-boot:build-image
```

2. Spin up running containers for dependencies (including the image created in step 1)
```
docker-compose up
```
