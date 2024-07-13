package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.Entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageRepository extends MongoRepository<Message, String> {
    @Query(value = "{'receiver_id': ?0}")
    Page<Message> findAllByReceiver_id( String receiver_id,Pageable pageable);
}
