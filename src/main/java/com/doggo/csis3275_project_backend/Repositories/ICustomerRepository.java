package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.Entities.Customer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICustomerRepository extends MongoRepository<Customer, String> {
    // query the DB to get customer using the username
    // to give you idea, this query equivalent to:
    // SELECT * FROM Customer WHERE username = <'username' -> variable from this Java code>
    Customer getCustomerByUsername(String username);
}