# Online shop 

This project is a Java Spring web application and is an online store. It provides functionality for creating, viewing and managing products, and also allows you to manage users using the admin console

## Project descsription

The online store application is designed as an analogue of “OLX” in Ukraine or Craigslist in the USA. It allows users to easily create, manage and view products. The application is built using the Java Spring framework, which provides a scalable framework.

### Key features:

Administrator dashboard for managing all users (deleting, viewing information, bans). Authenticate and authorize users for secure access. User registration process. Search for products based on names (titles). Adding new products.

## Technology Stack

- Backend:
   - Java - A general-purpose programming language that is class-based, object-oriented, and designed to have as few implementation dependencies as possible.
   - Spring Framework - An application framework and inversion of control container for the Java platform.
   - Spring Boot - An extension of the Spring framework that simplifies the process of building production-ready applications.
   - Spring Web - Provides key web-related features, including multipart file upload functionality and initialization of the IoC container.
   - Spring Data JPA - Provides a simple and consistent programming model for data access.
   - Spring Security - Provides comprehensive security services for Java EE-based enterprise software applications.
   - Spring MVC - The web module of the Spring framework that simplifies the work needed to develop web applications.
- PostgreSQL - An open-source relational database system.
- Testing:
   - JUnit 5 - A programming and testing model for Java applications.
   - Testcontainers - Provides throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.
   - Flyway - Database migration tool.
 - Frontend:
   - Thymeleaf - A Java-based templating engine for server-side rendering of web pages.
 - Containerization and Deployment:
   - Docker - A platform for developing, shipping, and running applications.

# How to Install and Run the Project
## Prerequisites:
 - Installed Docker
## Installing
 - Clone the repo
```
git clone git@github.com:NikitaLapanik/Online-Shop.git
```
## Running
 - Run out the box by Docker Compose
```
docker-compose up
````
Have fun! The application will be accessible at
- http://localhost:8080
# Credits
Lapanyk Nikita - developer
