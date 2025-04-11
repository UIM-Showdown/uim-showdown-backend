# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.3/reference/web/servlet.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.3/reference/web/spring-security.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.3/reference/data/sql.html#data.sql.jpa-and-spring-data)
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

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* mysql: [`mysql:8.4.4`](https://hub.docker.com/layers/library/mysql/8.4.4/images/sha256-60832e27fa98532ef7b75e634b065dd3809fcfbbe0dc591d6adf30a386d4dcbe)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# Local Development

### Dependencies
- [Java SE 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK 17](https://formulae.brew.sh/formula/openjdk@17)
- [Docker Desktop](https://www.docker.com/get-started/)
- [MySQL 8.4.4](https://dev.mysql.com/downloads/mysql/8.4.html) (if not using Docker)

## Docker
### Serving the Application
**NOTE:** Everytime changes are made, a new image build and container build are required.

1. Navigate to the `src/main/resources/` directory. Make a copy of `application.properties.example` and rename it to `application.properties`.
```
cd src/main/resources/
cp application.properties.example application.properties
```

The example properties file is already configured to use the configured Docker environment. Feel free to change any pre-existing settings to match your setup.

2. Build the application image using the [`build-image` maven plugin](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
```
./mvnw spring-boot:build-image -DskipTests
```

3. Spin up running containers for dependencies (including the image created in the previous step)
```
docker-compose up --build
```

### Troubleshooting

I recently ran into the following issue:
```
Execution default-cli of goal org.springframework.boot:spring-boot-maven-plugin:3.4.3:build-image failed: OS must not be empty
```

Sometimes stale cache layers cause strange issues. Using `docker system prune -af` addressed the issue above and will clean the Docker build cache. **Be careful — this removes all stopped containers, unused networks, and dangling images.**