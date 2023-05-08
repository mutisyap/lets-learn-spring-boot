# Testing Spring Boot Applications

Whatever we do, we need to test our applications. Testing is so important in software development, that we have a whole branch of software development dedicated to it: Test Driven Development (TDD).

Test Driven Development simply is creating the test before the feature, and then writing the code to pass the test. This is a very good practice, and it is recommended to do it. However, it is not always possible to do it. Sometimes, we have to write the code first, and then write the test. This is called Test After Development (TAD).

## Different types of testing
There are different types of testing. The most common types are:
- Unit testing: Testing the smallest unit of a software. E.g Testing a method.
- Integration Testing: Testing how different parts of the software work together. E.g Testing database access.
- Acceptance Testing: Testing the software against the requirements, and usability. E.g Testing that the software does what it is supposed to do by giving it to users to test it.

For most of the software solutions, we need to do all of these types of testing. However, without unit testing, it is not possible to do the other types of testing. So, we will start with unit testing.


## What to test in our applications
One of the most important decision is what to test, or where to begin the testing.

Let's take an example program flow in Spring Boot:
- User makes request (Via browser, or API client)
- Spring Web MVC receives the request and does the routing, authentication, authorization, etc.
- Our controller picks the request and directs it to the logic layer, our service
- Our service does the business logic, and then directs the request to the data layer, our repository.

First thing we need to know is that `What can go wrong will go wrong` at one point. With this in mind, we cannot just assume that the routing of our applications will be okay, and test our applications from the Controller or service layers. We will therefore create a test client, and test our entire applications.

## Our testing tools.
Remember when we created our application it added a testing starter.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```
This starter adds the following dependencies among others:
- AssertJ: A library to help us write assertions (confirmations that our code is working as expected) in our tests.
- Harmcrest: A library to help us write assertions in our tests.
- junit-jupiter: A unit testing framework for Java. This refers to JUnit 5.
- Mockito: A way to create mock objects for our tests, so that we can focus on testing what we want to test, and not worry about the other parts of our application.
- Spring Test: A library to help us test our Spring applications.

There are more dependencies like harmcrest, skyscreamer, etc. We will not go into details of these dependencies.

## The test package
When we created our application, it created a test package for us. This is the package where we will write our tests. The package is located at `src/test/java/com/pmutisya/learnspringboot` (or your equivalent).
It is recommended that you keep your tests following a similar package structure as your main application. This will make it easier to find the tests for a particular class. Also, you may not need to import your classes in your tests if they are in the same package.
Remember that the test package is not part of the solution that will be deployed. It is only used for testing.

## Writing our first test
Create `web.rest` package in the `src/test/java/com/pmutisya/learnspringboot` (or your equivalent) folder. This is where we will write our tests for our REST controllers.

Let's write our first test. In this `web.rest` package, we will test our `HelloController` class. We will test that when we make a request to `/hello`, we get a response with the text `Hello World!`.

Create the class `HelloControllerTest` in the package (or your equivalent) folder. This is where we will write our tests.
```java
@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/"))
                .andExpectAll(
                        status().isOk(),
                        content().string("Hello World"),
                        content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                );

    }
}
```
Let's go through the code above:
First, we have annotated our class with `@WebMvcTest(HelloController.class)`. 
```
Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components.
Using this annotation will disable full auto-configuration and instead apply only configuration relevant to MVC tests (i.e. @Controller, @ControllerAdvice, @JsonComponent, Converter/GenericConverter, Filter, WebMvcConfigurer and HandlerMethodArgumentResolver beans but not @Component, @Service or @Repository beans).
```
Simply put, we are testing our controller, and we do not need to load the entire application context. We don't even need to have the entire server running. We will only load the Spring MVC components, and mock everything else.

Next, we have autowired the `MockMvc` class. This is the class that we will use to make requests to our application. It is a mock class, so we will not be making actual requests to our application. We will be making requests to a mock server.

Our first test is simple. It sends a Http Get request to `/`, and expects a response with status code 200, and the text `Hello World`. It also expects the response to be of type `text/plain` e.g `text/plain;charset=UTF-8`. We use the `andExpectAll` method to check all the assertions at once. If one of the assertions fails, the test will fail.


## Running our tests
You can run your tests from your IDE, or from the command line. To run the tests from the command line, run the following command:
```shell
mvn -Dtest=HelloControllerTest test
```

## Testing our Employee CRUD APIs
Unlike our `HelloController` class, our `Employeeresource` RestController is doing more that just printing a text. It is doing CRUD operations on our database. We will therefore need to test it differently.

### Mocking the Service
Tests need to be isolated. Since our focus is testing the controller, we will mock the `EmployeeService` object. This will allow us to focus on testing the controller, and not worry about the service.
Create class `EmployeeResourceTest` in the `web.rest` package (or your equivalent) folder and annotate it with `@WebMvcTest(EmployeeResource.class)`. Inside, we create a MockMVC and a MockBean of the `EmployeeService` class. 

A mock bean is a simple object that will be injected in our application, and will return a default value when called. For example, if we call the `EmployeeService.getEmployee` method, it will return null. If we call the `EmployeeService.getAllEmployees` method, it will return an empty list. This is what we want. We want to focus on testing the controller, and not worry about the service.

```java
@WebMvcTest(EmployeeResource.class)
public class EmployeeResourceTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmployeeService employeeService;
}

```java
