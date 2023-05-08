# Adding persistence to Spring Boot with JPA
- [Introduction](#introduction)
So far, we've done our CRUD (Create, Read, Update, Delete) operations using in-memory data structures. In the real world, we need to persist our data to a database. In this section, we'll look at how to add persistence to our Spring Boot application using spring-data-jpa and a database of our choice.
- [Spring Data JPA](#spring-data-jpa)
We start by adding the spring-boot-starter-data-jpa dependency to our pom.xml file.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
_Remember: We don't need to define the version since we have our starter parent for that._
+ As usual, let's take a look at the dependencies that are pulled in by this starter by running `mvn dependency:tree` in the terminal.
```
+- org.springframework.boot:spring-boot-starter-data-jpa:jar:3.0.6:compile
|  +- org.springframework.boot:spring-boot-starter-aop:jar:3.0.6:compile
|  |  \- org.aspectj:aspectjweaver:jar:1.9.19:compile
|  +- org.springframework.boot:spring-boot-starter-jdbc:jar:3.0.6:compile
|  |  +- com.zaxxer:HikariCP:jar:5.0.1:compile
|  |  \- org.springframework:spring-jdbc:jar:6.0.8:compile
|  +- org.hibernate.orm:hibernate-core:jar:6.1.7.Final:compile
|  |  +- jakarta.persistence:jakarta.persistence-api:jar:3.1.0:compile
|  |  +- jakarta.transaction:jakarta.transaction-api:jar:2.0.1:compile
|  |  +- org.jboss.logging:jboss-logging:jar:3.5.0.Final:runtime
|  |  +- org.hibernate.common:hibernate-commons-annotations:jar:6.0.6.Final:runtime
|  |  +- org.jboss:jandex:jar:2.4.2.Final:runtime
|  |  +- com.fasterxml:classmate:jar:1.5.1:runtime
|  |  +- net.bytebuddy:byte-buddy:jar:1.12.23:runtime
|  |  +- org.glassfish.jaxb:jaxb-runtime:jar:4.0.2:runtime
|  |  |  \- org.glassfish.jaxb:jaxb-core:jar:4.0.2:runtime
|  |  |     +- org.eclipse.angus:angus-activation:jar:2.0.0:runtime
|  |  |     +- org.glassfish.jaxb:txw2:jar:4.0.2:runtime
|  |  |     \- com.sun.istack:istack-commons-runtime:jar:4.1.1:runtime
|  |  +- jakarta.inject:jakarta.inject-api:jar:2.0.0:runtime
|  |  \- org.antlr:antlr4-runtime:jar:4.10.1:runtime
|  +- org.springframework.data:spring-data-jpa:jar:3.0.5:compile
|  |  +- org.springframework.data:spring-data-commons:jar:3.0.5:compile
|  |  +- org.springframework:spring-orm:jar:6.0.8:compile
|  |  +- org.springframework:spring-tx:jar:6.0.8:compile
|  |  \- org.slf4j:slf4j-api:jar:2.0.7:compile
|  \- org.springframework:spring-aspects:jar:6.0.8:compile
```

+ Here we see multiple dependencies to aid data access availed to us by  spring-boot-starter-data-jpa:
  + spring-boot-starter-jdbc: This starter provides the JDBC API and the HikariCP connection pool.
  + Hibernate-core: This is the ORM (Object Relational Mapping) framework that we'll use to map our Java objects to database tables.
  + Spring Data JPA: This is the Spring Data module that provides a JPA-based repository abstraction layer. We'll use this to interact with our database.

## [Configuring the database](#configuring-the-database) <a name="configuring-the-database"></a>
+ We will start by using H2 as our database. H2 is an in-memory database that is very useful for development and testing. It is very easy to set up and use. We'll use it to get our application up and running quickly.
+ Since we are using spring data jpa, we don't need to worry about the underlying database. We can easily switch to a different database by changing the database driver and connection properties.
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```
+ To configure the database properties, we will go to our application.properties file. 
```properties
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:learning
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

+ An optional step is to change the file from .properties to `.yml`. I prefer .yml because it is easier and less repetitive. To do this, we need to rename the file to application.yml and change the properties to the following:
```yml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:learning
    username: user
    password: password
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

+ The database we create will be in-memory, and will be destroyed once the application stops. To create a persistent database, we can change the url to `jdbc:h2:file:~/learning` or `jdbc:h2:file:./learning` depending on whether we want the database to be created in the home directory or the current directory respectively. We can also change the username and password to whatever we want.

# Creating tables <a name="creating-tables"></a>
- We will need to create the tables where we put our data. We will be looking at more production-suited solutions in the future, but for now, we will use the inbuilt hibernate feature to create the tables for us. This is called _data migration_.
+ _Remember: The hibernate ddl feature is only for testing, and can be a huge mess in production_ 

+ Let's introduce a setting to create the tables for us. We will add the following to our application.yml file:

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:learning
    username: user
    password: password
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create
```

## Converting out Employee entity to a JPA entity <a name="converting-out-employee-entity-to-a-jpa-entity"></a>
- We need to make our Employee class a Jpa Entity. We do this by adding the @Entity annotation, and then add the @Table annotation to point it to the table to use.
- Doing so will make hibernate create the table for us. We will also add the @Id annotation to the id field to make it the primary key.
```java
package com.pmutisya.learnspringboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String company;

    public Employee() {
    }

    public Employee(Integer id, String name, String company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }
    // getters and setters
}
```
- We have made our Employee class a Jpa Entity, told it to refer to the table called employee, and made the id field the primary key. The GenerationType.IDENTITY tells hibernate to use the database's auto-increment feature to generate the id for us. Other options include GenerationType.AUTO, GenerationType.SEQUENCE, and GenerationType.TABLE. We will look at these in the future.

# Creating our repository for data access
- [SPring Data JPA](ttps://docs.spring.io/spring-data/jpa/docs/current/reference/html/) provides repository support for the Jakarta Persistence API (JPA). It eases development of applications that need to access JPA data sources.
- Spring data jpa helps reduce boilerplate SQL code in our applications. It does this by providing a repository abstraction layer. This layer provides CRUD operations for our entities. We can also add custom methods to the repository to perform more complex operations.
- Create a package called repository in the com.pmutisya.learning package. This is where we will put our repositories.
- To create a repository, we need to create an interface that extends the JpaRepository interface. We will create a repository for our User entity. We will call it UserRepository. We will create it in the repository package.

```java
package com.pmutisya.learning.repository;

import com.pmutisya.learnspringboot.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
```

+ Our interface extends the JpaRepository interface, which takes Generic specifications of the Entity, and Its Id type. In our case, the entity is Employee, and the Id type is Integer. We don't need to add any methods to the interface. Spring Data JPA will provide the CRUD methods for us.
+ JpaRepository extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T>. All these are sub-interfaces of the Repository interface, and provide different methods for accessing data. We will look at them in the future.
+ By extending the Respository interface, or its sub-interfaces, the interface becomes annotated with @Repository. This means that we can autowire it in our classes without having to annotate it with @Autowired. We can also add the @Repository annotation to the interface to make it more explicit. _Remember that annotations are comments_

## Changing our service to stop caching and start persisting
- We start by adding our EmployeeRepository object in the EmployeeService, but instead of initializing it, we just set it in the constructor, and let Spring Dependency Injection do the rest.
```java
package com.pmutisya.learnspringboot.service;

import com.pmutisya.learnspringboot.entity.Employee;
import com.pmutisya.learnspringboot.repository.EmployeeRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee, Integer id) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> read(Integer id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> readAll() {
        return employeeRepository.findAll();
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }
}
```
+ Some changes we've made here. We've forst of all removed the map, and replaced map methods with the employeereporitory methods of save, findById, findAll, and deleteById. We've also removed the caching, as we don't need it anymore.
+ The read method which was returning Employee now returns Optional. Optional is a wrapper for objects that returns empty instead of null, and is helpful in reducing null pointer checks and exceptions. We will look at it in the future.
+ Let's change the Resource to accommodate the findby id change
```java
@GetMapping("/employees/{id}")
public ResponseEntity<Employee> read(@PathVariable Integer id) {

    Optional<Employee> optional = employeeService.read(id);

    return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
}
```
- Here, we've changed return type to ResponseEntity to give us more control over the return type, since we wnt to return the httpStatus 404 if the employee is not found. We used optional mapping into the entity, and if it is not found, we return a not found response entity.
- Let's test our application. We will use postman to test our application as we did in the last example when we were using cache.

## More queries
- Spring data enables us to do 

References:
* Spring Data JPA: [https://docs.spring.io/spring-data/jpa/docs/current/reference/html/](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
* Hibernate ORM: [https://hibernate.org/orm/](https://hibernate.org/orm/)
* Jakarta Persistence API: [https://jakarta.ee/specifications/persistence/2.2/](https://jakarta.ee/specifications/persistence/2.2/)
* Liquibase [https://www.liquibase.org/](https://www.liquibase.org/)
  * Using Liquibase with Spring Boot: [https://docs.liquibase.com/tools-integrations/springboot/springboot.html](https://docs.liquibase.com/tools-integrations/springboot/springboot.html)