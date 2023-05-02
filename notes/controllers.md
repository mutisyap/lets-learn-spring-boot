<!-- Frontmatter -->
---
author: Peter Mutisya
title: REST API Controllers
sections:
    - creating a static Hello World REST controller
    - Understanding the REST API principles
    - Creating Employee memory cached CRUD REST API
---
<!-- /Frontmatter -->

# Our project structure
- The organization of your code is very important. You need to place classes in the right packages and folders so that it's easier to find and maintain them. Keep checking the kind of packages we create and the classes we place in them.
- Whichever structure you use, make sure it's consistent. If you are working in a team, make sure everyone follows the same structure.

# Annotations
- You are about to hear another monster word: annotations. A simple way to understand annotations is just to think of them like comments. The comments however, are not just for humans, they are for the compiler too. The compiler can decide to process the file differently depending on the annotations it finds.
- For example, the `@SpringBootApplication` annotation is just a comment that tells the compiler that this class is the main class of the application.
- We will come across more annotations as we go along. For now, just think of them as comments that the compiler can understand and act on. 
- Whenever we encounter one we will remember to reflect at how the compiler acts on it.
- Some spring annotations also server one additional function: They denote spring beans. Think of spring beans as classes that you will never need to initialize, instead, spring (not spring boot), will do this for us.


# Creating a static Hello World REST controller
- At the root package (same level as your Main class - mine is `com.pmutisya.learnspringboot`), Create a new package called `web`. Some people create a `controllers` package but I prefer `web` because it's more generic and can contain other web related classes put in packages such as `errors`, `interceptors`, `advisors` etc. More on those later on.
- Inside the `web` package, create another package called `rest`. This will contain all our REST controllers.
- Inside the `rest` package, create a new class called `HelloController`. This will be our first REST controller.
- Add the `@RestController` annotation to the class. This tells Spring that this class is a REST controller and it should be scanned for REST endpoints. Refer to annotations above. This also makes this class a spring bean. We will not need to initialize it, spring will do that for us.
- Lets create a function that returns a string. Add the following function to the class:
```java
public String sayHello() {
    return "Hello World";
}
```
- Now, let's 'decorate' it so that we can use it as a REST endpoint. Add the `@GetMapping` annotation to the function. Follow the annotation with a path '/'. This tells spring that this function should be called when a GET request is made to the path specified in the annotation. The entire class should look like this:
```java
@RestController
public class HelloController {
    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }
}
```
***Remember**: We don't include imports in our code snippets. You can get them from the code in the repository.*
- Restart the application and open your browser to `http://localhost:8080`. You should see the string `Hello World` displayed on the page.
![Hello World](assets/hello-world.png)
- You can also use curl to test the endpoint. Open a terminal and run the following command:
```bash
curl http://localhost:8080
```
- You should see the string `Hello World` displayed on the terminal.
We've seen how to create a simple REST controller. Let's now take a little bit of time to understand the REST API and its principles.

## Naming your RestController classes
In the example we've created above, we've used HelloController, taking our resource `hello`, and appending the suffix `controller` to it. Another common convention is using `resource` name suffixed with `Resource`. E.g HelloResource. My preference is combining the two in this case: If we are talking about a resource entity, like an Employee, then I use `Resource` suffix. Here, we commonly have different CRUD operations. 
You will find that we will have apis which don't refer to entities directly, but instead a different functionality in the system. For example, `Hello` is not an entity, but a way to just print a simple `Hello World` to the screen. Another example is Authentication of users. While this operation may go directly to the database and retrieve users while checking their password, this may not be the only way. You could have other users using Social Login, others using LDAP authentication, OAuth2.0 among others. For that case, I can create a `AuthenticationController` for user authentication, and `UserResource` for User Create, Retrieve, Update, Delete (CRUD) operations.

# Understanding the REST API
- REST was introduced by Roy Fielding in his 2000 PhD dissertation. He was one of the principal authors of the HTTP specification. He was also involved in the development of URI and MIME.
- The aim was to create a standard that would allow two servers to communicate and exchange data.
- REST stands for Representational State Transfer. It's a resource oriented architectural style for building APIs. Other than REST, there are other architectural styles such as SOAP, GraphQL etc.
![REST API Architecture](https://www.knowledgehut.com/_next/image?url=https%3A%2F%2Fd2o2utebsixu4k.cloudfront.net%2Fmedia%2Fimages%2F1670320665343-What%20is%20REST%20API-01.png&w=1080&q=75)


## How REST works
- REST is based on the HTTP protocol. It uses the HTTP verbs to perform CRUD operations on resources. The HTTP verbs are GET, POST, PUT, PATCH and DELETE.
- Assuming you have an e-commerce web application, accessed via web and mobile clients. You maintain a server, an application that stores the objects such as Users, and Stock items. The web and mobile clients are the consumers of your API, and will need to create, read, update and delete (CRUD operations) the objects in your application.
- A resource is an object that can be accessed via a URI. For example, a user is a resource. A stock item is a resource. A user can be accessed via the URI `/users`. A stock item can be accessed via the URI `/stock-items`.
- The data can then be sent to the client in almost any format. The most common formats are JSON and XML. JSON is the most popular format because it's lightweight and easy to read, and it's the format we will be using in this course.
- Non-text formats such as images, videos, pdf, audio etc can also be sent to the client. The client can then use the data to display it to the user.
- Some of the benefits in using REST include:
  - Scalability: We are able to separate the client from the server. We can also scale the client and server independently. For example, we can have multiple clients accessing a single server. We can also have multiple servers serving a single client, and load balancing between them.
  - Flexibility and portability: We can rewrite or even add new clients without affecting the server, and vice versa. Assuming we have an authentication and authorization server, we can adapt it to work for multiple clients, across multiple projects.
  - Independence: The client and server are fully isolated form each other. They can be developed by different teams, using different languages, as long as the REST principles are followed

## REST terminology
- Resource: A resource is an object that can be accessed via a URI. For example, a user is a resource.
- Client: The initiator of an API request. It can be a web application, a mobile application, a desktop application, or even another server.
- Server: The receiver of an API request. It can be a web server, an application server, a database server, or even another server.
- Request: A request is an action performed by the client on the server. It can be a GET, POST, PUT, PATCH or DELETE request. A client sends an HTTP request to the server, and the server responds with an HTTP response.
- Endpoint: A Uniform Resource Identifier (URI) indicating where and how to find the resource.
- Payload or body: The data sent to the server in the request. It can be in JSON, XML, or any other format.

## Principles of REST
### 1. Client-Server decoupling
The client only needs to know the URI of the requested resource, and no additional interaction with the server is needed. The server is also decoupled from the client. It doesn't need to know anything about the client. It just needs to respond to the requests made by the client.
### 2. Uniform Interface
API requests for the same request should be uniform, regardless of where they come from. 
### 3. Statelessness
A request must contain all the information needed to process it. This is common in creating pagination (The delivery of large amounts of data in chunks, called pages). In this case, it's the work of the client to send the page they need, not the server to know the data that a client had already fetched before. In short, the server should not store any information about the client. This makes the server more scalable and easier to maintain.
### 4. Layered System
The client should not be able to tell whether it's communicating with the end server or an intermediary. This allows for the introduction of proxies and load balancers. 
### 5. Cacheable
Wherever possible, caching should be used to save up network bandwidth, and speed response times. The server must additionally indicate whether caching is authorized for a particular resource. This is done using the `Cache-Control` header.
### 6. Code on demand
Server responses typically contain static resources, but they could also respond with executable code (e.g Java applets) to the client. In this case, the client needs to perform the necessary code. This is rarely used.

## REST API Methods
REST uses the HTTP verbs to perform CRUD operations on resources. The HTTP verbs are GET, POST, PUT, PATCH and DELETE.

### GET
Used to retrieve a resource. It's the most common method used in REST APIs. GET should be ONLY used for read-only operations. For security reasons GET should not be used to create, update or delete resources. It should also not be used to send sensitive data such as passwords. If the data has not been changed, GET should always return the same results every time.

### POST
We use post to CREATE a resource. In this case, it sends data to a server, and normally expects a resource to access the date. POST is not idempotent. This means that if you send the same request multiple times, you will get different results. For example, if you send a POST request to create a user, you will get a different user every time you send the request. This is because the server will create a new user every time you send the request.

### PUT
This is used to UPDATE a resource by total replacement. It sends data to a server, and expects the server to update the resource. PUT is idempotent. This means that if you send the same request multiple times, you will get the same results. For example, if you send a PUT request to update a user, you will get the same user every time you send the request. This is because the server will update the user every time you send the request.

### PATCH
Patch alters resource contents instead of total replacement used in PUT. We can use PATCH to update the stock count of an item when a purchase is made. In that case, not much about the item has changed, just the stock count.

### DELETE
Results in complete DELETION of a resource. Implementation of delete can be a little inconsistent. Instead of fully removing a resource, it can be marked as deleted, and hidden from the client. This is because the server may need to keep a record of the resource for other purposes like auditing, or need to keep the record for the sake of other child entities.


## REST API Best practices
REST is not a standard, but an architecture. It defines set of principles that can be implemented in different ways. There are however some best practices that should be followed when implementing REST APIs.
### 1. Avoid returning plaintext
JSON is the most popular format for REST APIs. Unless going for backward compatibility with existing systems, you need to use JSON for your REST APIs. In that case, prefer sending the server specification, and I'm guessing it's not plaintext.
### 2. Use nouns instead of verbs in URLs
The HTTP Methods we discussed above are sufficient to indicate the action to be performed on a resource. The URL should only contain the resource name, and not the action to be performed on the resource. For example, instead of using `/get-users`, we should use `GET` method, with URL `/users`. Instead of using `/create-user`, we should use `/users`. Instead of using `/update-user`, we should use `/users`. Instead of using `/delete-user`, we should use `/users`.
In cases where the Methods are not sufficient, you can add additional nouns in the url. For example, assuming we can to create a user, who is a little different from other users in that they're anonymous, we can use the URL `/users/anonymous`. This is better than using `/users/create-anonymous`.
#### What about filters.
I must admit that I am a great victim of breaking this principle when it comes to filter. 
`stock-items/filter` is still a bad idea. instead of this, we can see the reason for the filter. If we're filtering by shop, we can do `/stock-items/shop/:shopId` and if we want to filter by category, we can do `/stock-items/category/:categoryId`.
If we want to combine filter, well and good. Let's just try to avoid the word filter (a verb) in the URL. e.g `/stock-items/shop/:shopId/category/:categoryId` is better than `/stock-items/filter?shopId=1&categoryId=2`

### Use query parameters sparingly, moreso on the default GET endpoints
Let's assume we have periodic offers on stock items, and we need our clients to easily fetch this data.
`/stock-items/offer` is better than `/stock-items?offer=true`. The first one is more readable, and easier to understand. The second one is more technical, and requires the client to understand the query parameters.
Remember the KISS principle. Keep It Simple Stupid.

### 3. Use plural nouns for consistency
You see what we just did up there? Instead of using `/stock-item`, we used `/stock-items`. This is because it fits most of the endpoints way better. We should always use plural nouns for consistency.

### 4. Return error details in the response body
So much can go wrong at the server. Assist the client by returning error details in the response body.

### 5. Use HTTP status codes correctly
Though common behavior for server-to-server communications to return status Ok, with error details in the body, this is a huge anti-pattern. And I'm a victim. 
While at it, when a resource is created, return 201 instead of 200. When a resource is being deleted return 204 instead of 200. When a resource is being updated, return 200.
```
GET: 200 OK
POST: 201 Created
PUT: 200 OK
PATCH: 200 OK
DELETE: 204 No Content
```

### 6. Don't nest resources
This is a very common anti-pattern. Let's assume we have a shop, and we want to get all the stock items in the shop. We can do `/shops/:shopId/stock-items`. This is a bad idea. Instead, we can do `/stock-items?shopId=:shopId`. This is better because it's more readable, and easier to understand. The first one is more technical, and requires the client to understand the query parameters.
Understand first what you're retriving. Is it shops or stock items, and then start with that. In this case, we're retrieving stock items, so we start with `/stock-items`. If we were retrieving shops, we would start with `/shops`, and place it with other shops endpoints.

### 6.Trailing slashes
When getting all stock items, you can find yourself doing this: `/stock-items/`. This can lead you into issues with your REST api framework if you happen to have `/stock-items/:id`, and think you passed a null as an id, resulting in errors. To avoid this, always use `/stock-items` instead of `/stock-items/`.

### 7. Make use of query parameters for filtering and pagination
AT times, the endpoint is just not enough. Don't struggle making it too long. Instead, just use query parameters. For example, if we want to get all stock items in a shop, we can do `/stock-items?shopId=:shopId`. If we want to get all stock items in a shop, and in a category, we can do `/stock-items?shopId=:shopId&categoryId=:categoryId`. If we want to get all stock items in a shop, and in a category, and in a price range, we can do `/stock-items?shopId=:shopId&categoryId=:categoryId&minPrice=:minPrice&maxPrice=:maxPrice`. This is better than `/stock-items/shop/:shopId/category/:categoryId/min-price/:minPrice/max-price/:maxPrice`. The first one is more readable, and easier to understand. The second one is more technical, and requires the client to understand the query parameters.

Okay, there is still more recommendations to using REST correctly. Remember, the goal is not just to create an API that serves clients well, but one that is easy to maintain. The more you follow the best practices, the easier it will be to maintain your API. The more you break the best practices, the harder it will be to maintain your API.

# Employee APIs
We are done with the detour. I hope it was worth it looking at REST APIs. Let's get back to our spring boot project. We'll start by creating EmployeeController class, and then add several functions to it. We'll start with the GET endpoints, and then move to the POST, PUT, PATCH, and DELETE endpoints.
To ease your learning, think of a RESTController class as containing all the endpoints for a particular resource. In our case, we'll have EmployeeController class, which will contain all the endpoints for the Employee resource.
The functions will then represent the individual endpoints. For example, we'll have a function called `getEmployees`, which will represent the endpoint `/employees`. We'll have a function called `getEmployee`, which will represent the endpoint `/employees/:id`. We'll have a function called `createEmployee`, which will represent the endpoint `/employees`. We'll have a function called `updateEmployee`, which will represent the endpoint `/employees/:id`. We'll have a function called `deleteEmployee`, which will represent the endpoint `/employees/:id`.
Remember: Keep your functions public. A private function will not be properly mapped to the endpoint, and you will receive a 404 error when you try to access the endpoint.