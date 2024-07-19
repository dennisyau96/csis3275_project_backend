package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.Entities.Booking;
import com.doggo.csis3275_project_backend.Entities.Customer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICustomerRepository extends MongoRepository<Customer, String> {
    // query the DB to get customer using the username
    // to give you idea, this query equivalent to:
    // SELECT * FROM Customer WHERE username = <'username' -> variable from this Java code>
    Customer getCustomerByUsername(String username);
}