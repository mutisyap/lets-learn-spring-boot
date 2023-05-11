# Data Migration
- So far, we've let hibernate (an Object-Relation Mapping solution), take care of creating our tables. At this point, we will need to take charge of our data by doing the table creation, and populating some data to it.

## Liquibase
- Liquibase is a database migration tool that allows us to manage our database changes using a version control system. It allows us to create, and manage our database changes using a declarative approach. This means that we will be able to define our database changes using a YAML, or XML file. Liquibase will then take care of applying these changes to our database. This is very useful when working with a team, as it allows us to track our database changes using a version control system such as git.
- To use liquibase, we will need to add the following dependencies to our pom.xml file:
```xml
<dependency>
  <groupId>org.liquibase</groupId>
  <artifactId>liquibase-core</artifactId>
</dependency>
```
- We will also need to add the following properties to our application.yaml file:
```yaml
spring:
  liquibase:
    change-log: classpath:liquibase/master.xml
```
- Liquibase needs a master file, which tells it where the files containing our migration instructions (Change Log files) are to be found. We will create this file in the resources/liquibase folder. We will call it master.yaml. We will also create a changelog folder in the resources/liquibase folder. This is where we will store our Change Log files. We will create a file called 0001-create-company-table.yaml in this folder. This file will contain the instructions for creating our company table. We will also create a file called 0002-create-department-table.yaml in the same folder. This file will contain the instructions for creating our department table. Our master.yaml file will look like this:
```yaml 
databaseChangeLog:
    - includeAll:
        path: liquibase/changelog
```
- It simply tells liquibase to go to the `resources/liquibase/changelog` folder, and execute all the files in that folder may they be xml or yaml change log files.