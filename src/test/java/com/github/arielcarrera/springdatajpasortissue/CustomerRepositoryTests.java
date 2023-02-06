package com.github.arielcarrera.springdatajpasortissue;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

/**
 * Test using MySQL 5.7.
 *
 * @author Ariel Carrera
 */
@ContextConfiguration(initializers = CustomerRepositoryTests.Initializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class CustomerRepositoryTests {

  @ClassRule
  public static final MySQLContainer MYSQL = new MySQLContainer("mysql:5.7.37").withDatabaseName("test").withUsername("sa").withPassword("sa");

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CustomerRepository customers;

  public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      MYSQL.start();
      configurableApplicationContext.getEnvironment()
        .getSystemProperties()
        .put("spring.datasource.url", MYSQL.getJdbcUrl() + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
      configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.username", MYSQL.getUsername());
      configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.password", MYSQL.getPassword());
    }
  }

  @BeforeAll
  public void setup() {
    Customer customer = new Customer("first", "last", 1L);
    customers.save(customer);
    Customer customer2 = new Customer("first2", "last", 2L);
    customers.save(customer2);
  }

  //FIND BY QUERY:

  //ISSUE Caused by: org.hibernate.QueryException: Parameter 1 of function lower() has type STRING, but argument is of type java.lang.Long
  @Test
  void findByQueryWithPageRequestIgnoreCaseAndProjection() {
    Page<Customer> result = (Page<Customer>) customers.findByQuery("last", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("value").ignoreCase())), Customer.class);
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }

  //ISSUE Caused by: org.hibernate.QueryException: Parameter 1 of function lower() has type STRING, but argument is of type java.lang.Long
  @Test
  void findByQueryWithPageRequestIgnoreCaseWithoutProjection() {
    Page<Customer> result = customers.findByQuery("last", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("value").ignoreCase())));
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }

  //ISSUE Caused by: org.hibernate.QueryException: Parameter 1 of function lower() has type STRING, but argument is of type java.lang.Long
  @Test
  void findByQueryWithSortIgnoreCaseWithoutProjection() {
    List<Customer> findByLastName = customers.findByQuery("last", Sort.by(Sort.Order.asc("value").ignoreCase()));
    Assertions.assertThat(findByLastName).extracting(Customer::getLastName).containsOnly("last");
  }

  // Success
  @Test
  void findByQueryWithPageRequestAndProjectionWithoutIgnoreCase() {
    Page<Customer> result = (Page<Customer>) customers.findByQuery("last", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("value"))), Customer.class);
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }

  // Success
  @Test
  void findByQueryWithPageRequestWithoutIgnoreCaseAndProjection() {
    Page<Customer> findByLastName = customers.findByQuery("last", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("value"))));
    Assertions.assertThat(findByLastName).extracting(Customer::getLastName).containsOnly("last");
  }

  // Success
  @Test
  void findByQueryWithSortWithoutIgnoreCaseAndProjection() {
    List<Customer> findByLastName = customers.findByQuery("last", Sort.by(Sort.Order.asc("value")));
    Assertions.assertThat(findByLastName).extracting(Customer::getLastName).containsOnly("last");
  }


  // FIND BY LASTNAME

  // Success
  @Test
  void findByLastNameWithSortIgnoreCaseByString() {
    List<Customer> result = customers.findByLastName("last", Sort.by(Sort.Order.asc("lastName").ignoreCase()));
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }

  // Success
  @Test
  void findByLastNameWithSortIgnoreCaseByLong() {
    List<Customer> result = customers.findByLastName("last", Sort.by(Sort.Order.asc("value").ignoreCase()));
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }

  // Success
  @Test
  void findByLastNameWithPageRequestAndSortIgnoreCaseByLong() {
    List<Customer> result = customers.findByLastName("last", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("value").ignoreCase())));
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }

  // Success
  @Test
  void findByLastNameWithPageRequestAndSortIgnoreCaseByLongAndProjection() {
    Page<Customer> result = (Page<Customer>) customers.findByLastName("last", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("value").ignoreCase())), Customer.class);
    Assertions.assertThat(result).extracting(Customer::getLastName).containsOnly("last");
  }



}
