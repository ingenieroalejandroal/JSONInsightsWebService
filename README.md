## JSONInsightsWebService - RESTful web service using Spring Boot 1.4

This project provides insights in where the diffs are between two JSON base64 encoded binary data and it is made up of 4 endpoints:

#### /v1/diff/{id}/left
POST request endpoint that accepts JSON Base64 encoded binary data. This request creates or updates a new JsonTracking Object 
into the H2 database. The attribute 'side' from the object JsonTracking 
will have the value 'left'. 
- Path Variables: id
- Request Parameter: base64EncodedJson

#### /v1/diff/{id}/right 
POST request endpoint that accepts JSON Base64 encoded binary data. This request creates or updates a new JsonTracking Object 
into the H2 database. The attribute 'side' from the object JsonTracking 
will have the value 'right'. 
- Path Variables: id
- Request Parameter: base64EncodedJson

#### /v1/diff/{id} 
GET request that returns the insights between the two objects found in database (left and right) 
corresponding to the path variable {id}. It produces a JSON response according to the following logic:

* If 'json left' is equals 'json right', returns the JSON: 
{"isEqual": "true"}

* If 'json left' is NOT equals 'json right' AND not equal size, returns the JSON: {"isEqual":"false","isEqualSize":"false"}
* If 'json left' is NOT equals 'json right' AND equal size, it returns the property 'diff' highlighting where the diffs are: {"isEqual":"false","isEqualSize":"true","diffs":"[Diff(EQUAL,"equalText"), Diff(DELETE,"textThatShouldBeDeleted"),Diff(INSERT,"textThatShouldBeInserted"), Diff(EQUAL,"equalText")]"}


#### /v1/diff/
GET request to fetch all JsonTracking Objects stored in the H2 database, it returns an JSONArray. 

### Tools and technologies
* spring-boot-starter-parent 1.4.3.RELEASE
* H2
* javax.json.bind-api 1
* util-diff 1.28
* jackson-datatype-jsr310 2.9.8
* JDK 1.8
* JUnit 4
* Maven
* Eclipse

### Project Details
* Service/Controller
* Repository using features from CrudRepository
* Model class
* Spring MVC
* Unit Testing
* Integration Testing
