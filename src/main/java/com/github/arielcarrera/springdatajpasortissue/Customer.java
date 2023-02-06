package com.github.arielcarrera.springdatajpasortissue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;

/**
 * Customer entity.
 */
@Entity
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  private Long value;

  protected Customer() {
  }

  public Customer(String firstName, String lastName, Long value) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Long getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.format("Customer[id=%d, firstName='%s', lastName='%s, value=%d]", id, firstName, lastName, value);
  }
}
