`# Spring Data Jpa - Sort Issue
The project is intended to be a reproducer for an issue related to the usage of the Sort using the ignoreCase flag enabled in repository methods annotated with @Query.

## Requirements:
- Java JDK 17
- Docker
- Spring Boot 3.0.2
- Hibernate 6.X
- Testcontainers + Junit + MySQL container

## Issue Description:
The issue occurs when a repository method using @Query annotation and with a given Sort parameter, receives Sort object with a non String column type and the flag isIgnoreCase in true.

## Error Message:
Caused by: org.hibernate.QueryException: Parameter 1 of function lower() has type STRING, but argument is of type java.lang.Long

## Feasible solutions:
- Modify QueryUtils.class to allow the detection of the field type before the generation of the SORT sentence including lower(...) and avoid the usage of lower() for numeric types.
- Alternatively, the QueryUtils.class can be modified adding the function str() like the following example: lower(str(...))
- Other...`