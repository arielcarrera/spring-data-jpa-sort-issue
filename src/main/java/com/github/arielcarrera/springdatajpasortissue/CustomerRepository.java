package com.github.arielcarrera.springdatajpasortissue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Customer repository.
 *
 * @author Ariel Carrera
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

  List<Customer> findByLastName(String lastName, Sort sort);

  List<Customer> findByLastName(String lastName, Pageable pageable);

  Page<? super Customer> findByLastName(String lastName, Pageable pageable, Class<? super Customer> projection);

  Customer findById(long id);

  @Query("SELECT cus FROM Customer AS cus WHERE cus.lastName = :lastName")
  Page<? super Customer> findByQuery(String lastName, Pageable productPage, Class<? super Customer> projection);

  @Query("SELECT cus FROM Customer AS cus WHERE cus.lastName = :lastName")
  Page<Customer> findByQuery(String lastName, Pageable productPage);

  @Query("SELECT cus FROM Customer AS cus WHERE cus.lastName = :lastName")
  List<Customer> findByQuery(String lastName, Sort sort);
}
