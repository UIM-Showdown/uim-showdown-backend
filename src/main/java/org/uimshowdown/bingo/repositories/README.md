# Repositories

## Getting Started
Most of the time, we'll want to create a new repository interface that extends `CrudRepository`. However, there are other pre-defined repository types that may better suit your needs like the `PagingAndSortingRepository` or `ReactiveCrudRepository`.

Read about these repository types and for more information on defining repositories in the following documentation: https://docs.spring.io/spring-data/jpa/reference/repositories/definition.html

## Adding New Query Methods
The following documentation will help you understand how to create queries outside of the CRUD operations provided by the `CrudRepository` class.

### JPA Query Methods
Query creation requires the use of a specific naming convention that is detailed in the following documentation: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation

There is also a more detailed guide to [query keywords](https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html) that can be used when during query creation.

### Manual Query Declaration
The [`@Query` annotation](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.at-query) will help you achieve this.