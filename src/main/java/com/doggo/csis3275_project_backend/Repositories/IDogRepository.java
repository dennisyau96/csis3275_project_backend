package com.doggo.csis3275_project_backend.Repositories;


import com.doggo.csis3275_project_backend.Entities.Dog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDogRepository extends MongoRepository<Dog, String> {
    Dog getDogBy_id(String _id);

    @Query(value = "{ '_id' : ?0, 'owner_id' : ?1 }")
    Dog findBy_idAndOwner_id(String _id, String owner_id);

    @Query(value = "{ 'owner_id' : ?0 }")
    Page<Dog> findAllByOwner_id(String ownerID, Pageable pageable);
}