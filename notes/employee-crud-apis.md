# Employee APIs
We are done with the detour. I hope it was worth it looking at REST APIs. Let's get back to our spring boot project. We'll start by creating EmployeeController class, and then add several functions to it. We'll start with the GET endpoints, and then move to the POST, PUT, PATCH, and DELETE endpoints.
To ease your learning, think of a RESTController class as containing all the endpoints for a particular resource. In our case, we'll have EmployeeController class, which will contain all the endpoints for the Employee resource.
The functions will then represent the individual endpoints. For example, we'll have a function called `getEmployees`, which will represent the endpoint `/employees`. We'll have a function called `getEmployee`, which will represent the endpoint `/employees/:id`. We'll have a function called `createEmployee`, which will represent the endpoint `/employees`. We'll have a function called `updateEmployee`, which will represent the endpoint `/employees/:id`. We'll have a function called `deleteEmployee`, which will represent the endpoint `/employees/:id`.
Remember: Keep your functions public. A private function will not be properly mapped to the endpoint, and you will receive a 404 error when you try to access the endpoint.

## Employee Entity
Let's start by creating the Employee entity. We'll create a new package called `entity` in the `com.pmutisya.learnspringboot` package. Then we'll create a new class called `Employee` in the `entity` package. We'll add the following fields to the class, and their getters and setters.

```java
package com.pmutisya.learnspringboot.domain;

public class Employee {
    private Integer id;
    private String name;
    private String company;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
```
*Later on, we will refactor the entity package into two major categories: `domain` and `dto`. The `domain` package will contain the persistent entities, and the `dto` package will contain the data transfer objects (objects we use for communicating with our clients). For now, we'll keep everything in the `entity` package.*

## Employee Controller class
We start by creating a EmployeeResource class in the web.rest package. Keep in mind we've used the suffix `Resource` here because employee is an entity. For Hello, we used `HelloController` because hello is not an entity. We'll use the suffix `Controller` for entities, and the suffix `Resource` for non-entities.
Annotate the class with `@RestController` and `@RequestMapping("/api")`. The `@RestController` annotation tells Spring that this class is a REST controller. The `@RequestMapping("/api")` annotation tells Spring that all the endpoints in this class will start with `/api`. 
*This is not a requirement, but a good practice, that enables us to add non-api calls such as static files on our application without confusion. Assuming we have an employee html page served by our spring boot application, we can access it using `/employee` and it won't conflict with our api endpoints. More on static files later.*

This means that if we have a function called `getEmployees`, then the endpoint will be `/api/employees`. If we have a function called `getEmployee`, then the endpoint will be `/api/employees/:id`. If we have a function called `createEmployee`, then the endpoint will be `/api/employees`. If we have a function called `updateEmployee`, then the endpoint will be `/api/employees/:id`. If we have a function called `deleteEmployee`, then the endpoint will be `/api/employees/:id`.

### A service to hold our data temporarily
To perform CRUD operations on the employee object, we will need a way to hold our data. For this case, we will create a simple `EmployeeService` class that saves the data into a map data structure. Remember that data held in memory is not persistent, and will be lost when the application is restarted. We will see how to persist data in a few.
Create a new package called `service` in the `com.pmutisya.learnspringboot` package. Then create a new class called `EmployeeService` in the `service` package. Add the following code to the class:
```java

@Service
public class EmployeeService {
    private final Map<Integer, Employee> employeeMap = new HashMap<>();

    public Employee create(Employee employee) {
        employeeMap.put(employee.getId(), employee);
        return employee;
    }

    public Employee update(Employee employee, Integer id) {
        employeeMap.put(id, employee);
        return employee;
    }

    public Employee read(Integer id) {
        return employeeMap.get(id);
    }

    public List<Employee> readAll() {
        return new ArrayList<>(employeeMap.values());
    }
}
```

We have annotated the class with `@Service`. This tells Spring that this class is a spring bean, of type service class. When we go to our controller class, we will be able to inject this service class into our controller class.

We then created a Map with key as Integer and value as Employee, since our employee unique identifier is an Integer id. This map will hold our data. We have also created a few functions to perform CRUD operations on the map. We will see how to use this service in a few.

### Injecting the service into our controller
There are two common ways of injecting a service into a controller. The first one is by using the `@Autowired` annotation. The second one is by using the constructor. We will use the constructor method, since it gives us control when testing our application. We can comfortably mock the service class when testing our controller class.
Add a final field of class EmployeeService in the EmployeeResource class. We use final because we don't want to change the service once it has been injected. And also to remind us to initialize it in the constructor. So proceed and do both:
```java
private final EmployeeService employeeService;

public EmployeeResource(EmployeeService employeeService) {
    this.employeeService = employeeService;
}
```

### Create employee
Remember that we represent individual resources with functions, and these functions are annotated to indicate the message and url (or URL part in our case). For create, we have this:

```java
@PostMapping("/employees")
public Employee create(@RequestBody Employee employee) {
    return employeeService.create(employee);
}
```
There is a lot going on here. Let's break it down.
The `@PostMapping("/employees")` annotation tells Spring that this function will handle POST requests to the `/api/employees` endpoint. This is similar to what we have at `HelloController`, just that it uses the HTTP POST Method. 

When creating a resource, we need to send the resource data in the request body. The `@RequestBody Employee employee` annotation tells Spring that the request body should be mapped to the employee parameter. The `@RequestBody` annotation is used to map the request body (JSON in our case) to a Java object. This is used as our function parameter. There are more parameters we can use in this case, and we will see them in a few.

We have then called our service class to create the employee. The service class will return the created employee, and we will return it to the client.

### Update Employee
For update, we have this:
```java
@PutMapping("/employees/{id}")
public Employee update(@RequestBody Employee employee, @PathVariable Integer id) {
    return employeeService.update(employee, id);
}
```

Here, we've used the `@PutMapping("/employees/{id}")` annotation to tell Spring that this function will handle PUT requests to the `/api/employees/:id` endpoint. The `@PathVariable Integer id` annotation tells Spring that the id part of the URL should be mapped to the id parameter. The `@RequestBody Employee employee` annotation tells Spring that the request body should be mapped to the employee parameter.

As the name suggests, @PathVariable is used as part of the url. When writing URLs, it's common to see a full colon immediately followed by a variable, e.g :id, :name, :age, :email, etc. These are called path variables. They are used to pass data to the server. For example, if we have a url like `/api/employees/:id`, then we can pass the id as a number, e.g `/api/employees/1` and 1 becomes our id.

*Note: The type of the variable is not defined in the endpoint. If the user passed `q` as our id, i.e: `/api/employees/q`, then the endpoint will be mapped correctly, but our parameter will not be mapped to id, since it's not an Integer. The parameter type commonly goes as part of the API documentation*

### Read one Employee
For read one, we have this:
```java
@GetMapping("/employees/{id}")
public Employee read(@PathVariable Integer id) {
    return employeeService.read(id);
}
```
Here, we have a HTTP Method GET to `/employees/:id`, we supply an id as part of URL using the `@PathVariable` annotation, and read it into an Integer. We then call our service class to read the employee with the given id.

### Read all Employees
For read all, we have this:
```java
@GetMapping("/employees")
public List<Employee> readAll() {
    return employeeService.readAll();
}
```
Here, we have a HTTP Method GET to `/employees`, without any additional parameter or request body. The function then calls our service class to read all employees.

### Delete Employee
For delete, we have this:
```java
@DeleteMapping("/employees/{id}")
public void delete(@PathVariable Integer id) {
    employeeService.delete(id);
}
```
HTTP method is Delete, then we pass the id as part of the URL, just as we did with GET, and call our service class to delete the employee with the given id.

### Testing our API
So, we're just about done. Let's test our API. We will use Postman to test our API. If you don't have Postman, you can download it from [here](https://www.postman.com/downloads/). We will use Postman to send requests to our API, and see the responses. We will also use Postman to test our API.

Start the application, refer to section 1, and open PostMan. I will also provide cURL commands for those who prefer using the terminal.

#### Create Employee
```bash
curl --location 'http://localhost:8080/api/employees' \
--header 'Content-Type: application/json' \
--data '{
    "id":1,
    "name":"Peter Mutisya",
    "company":"Meliora Technologies"
}'

# Response
{"id":1,"name":"Peter Mutisya","company":"Meliora Technologies"}
```

```bash
curl --location 'http://localhost:8080/api/employees' \
--header 'Content-Type: application/json' \
--data '{
    "id":2,
    "name":"Samuel Kamochu",
    "company":"Meliora Technologies"
}'

# Response
{"id":2,"name":"Samuel Kamochu","company":"Meliora Technologies"}
```

#### Update Employee
```bash
curl --location --request PUT 'http://localhost:8080/api/employees/1' \
--header 'Content-Type: application/json' \
--data '{
    "id":1,
    "name":"Peter Kivulu",
    "company":"Meliora Technologies"
}'

# Response
{"id":1,"name":"Peter Kivulu","company":"Meliora Technologies"}
```

#### Read one Employee
```bash
curl --location 'http://localhost:8080/api/employees/1'

# Response
{"id":1,"name":"Peter Kivulu","company":"Meliora Technologies"}
```

```bash
curl --location 'http://localhost:8080/api/employees/2'

# Response
{"id":2,"name":"Samuel Kamochu","company":"Meliora Technologies"}
```

#### Read all Employees
```bash
curl --location 'http://localhost:8080/api/employees'

# Response
[{"id":1,"name":"Peter Kivulu","company":"Meliora Technologies"},{"id":2,"name":"Samuel Kamochu","company":"Meliora Technologies"}]
```

#### Delete Employee
```bash
curl --location --request DELETE 'http://localhost:8080/api/employees/1'

# Response
```

#### Read all Employees
```bash
curl --location 'http://localhost:8080/api/employees'

# Response
[{"id":2,"name":"Samuel Kamochu","company":"Meliora Technologies"}]
```
Employee id 1 has been deleted, and we can see that it's no longer in the list of employees.

### Validations
When we create software, we tend to stick to the happy path. Let's try creating an invalid employee, and see what happens.
```bash
curl --location 'http://localhost:8080/api/employees' \
--header 'Content-Type: application/json' \
--data '{
    "abcd":"eert",
    "efgh":"rrtt"
}'

# Response
{"id":null,"name":null,"company":null}
```

Get all employees
```bash
curl --location 'http://localhost:8080/api/employees'

# Response
[{"id":null,"name":null,"company":null},{"id":2,"name":"Samuel Kamochu","company":"Meliora Technologies"}]
```

That doesn't look like an employee, right? How did they get added to our list of employees? Well, we didn't add any validations to our code. Let's add some validations.

#### @Valid Annotation
We will use the `@Valid` annotation to validate our employee object. We will add it to our `create` and `update` functions, like this:
```java
@PostMapping("/employees")
    public Employee create(@RequestBody Employee employee) {
        return employeeService.create(employee);
    }

