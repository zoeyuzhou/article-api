# Article API
## Overview
This repository is to implement a Restful API of article web services. Three endpoints are provided:
* POST /articles
  
  Handle the receipt of some article data in json format, and store it within the service.  
  It returns one of the following five possible response:
    * 201 Created - Create article details in the system 
    * 200 Ok - Update the article details if provided article id has already been in the system.
    * 400 Bad Request - If article details provided is not valid
    * 404 Not Found - Could not get article in the system based on provided article id 
    * 500 Internal Server Error - if an error occurs
* GET /articles/{id}  

  Return the JSON representation of the article.  
  It returns one of the following three possible response:
    * 200 Ok - Response the article details with provided id as below:  
    
    ``` javascript
    {  
        "id": "1",  
        "title": "latest science shows that potato chips are better for you than sugar",
        "date" : "2016-09-22",
        "body" : "some text, potentially containing simple markup about how potato chips are great",
        "tags" : ["health", "fitness", "science"]
    }
    ```
    * 404 Not Found - If no article in the system using provided id
    * 500 Internal Server Error - if an error occurs
    
* GET /tags/{tagName}/{date}  

    Return the list of articles that have that tag name on the given date and some summary data about that tag for that day.  
    It returns one of the following two possible response:
    * 200 Ok - Response the tags details with provided tagName and date as below:  
    
    ``` javascript
    {
        "tag" : "health",
        "count" : 17,
        "articles" :
            [
                "1",
                "7"
            ],
        "related_tags" :
            [
                "science",
                "fitness"
            ]
    }
    ```
    * 500 Internal Server Error - if an error occurs
    
## Installation instructions
### Requirements
* Java 8
* Maven
### Running The Application
The application will start at `http://localhost:8080` by default.
#### Running as a Packaged Application
article-api-0.0.1-SNAPSHOT.jar could be found under target/ directory.
```
java -jar target/article-api-0.0.1-SNAPSHOT.jar
```
#### Using the Maven Plugin
```
mvn spring-boot:run
```

### Run tests

```
mvn test
```

## System Design
### Technical 
* **Java 8**
* **Spring Boot** - Easy to create stand-alone, production-grade Spring based Applications that you can "just run".
* **Spring Web** - Full-Stack web deployment with tomcat and Spring MVC
* **Spring JPA** - Java Persistence API including spring-data-jpa, spring-orm and Hibernate
* **Spring H2** - H2 in memory database
### Assumptions
* Article Id is generated by system.
* **POST /articles** endpoint add or update article details.
     * "title", "body" are mandatory attributes, but it could be empty string.
     * "date", "tags" are not optional attributes.
     * "id" is optional attribute. 
        * No "id" provided - Create article.
        * "id" provided - Update article
* **GET /tags/{tagName}/{date}** 
    * No {tagName} in the system
        * Response 200 OK 
        * "count": 0
        * "article": []
        * "related_tags": [] 
    * {date} format could only be "yyyyMMdd" eg. "20160922"