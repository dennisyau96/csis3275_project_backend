package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends MongoRepository<Transaction, String> {

}
