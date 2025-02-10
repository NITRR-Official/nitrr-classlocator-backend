package com.classlocator.nitrr.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.classlocator.nitrr.entity.query;


public interface queryRepo extends MongoRepository<query, ObjectId> {
    @Query(value = "{ 'Roomid': ?0, 'raisedBy': ?1 }", sort = "{ '_id': 1 }")
    Optional<query> findFirstByRoomidAndRaisedBy(Integer Roomid, String raisedBy);

    @Query(value = "{ 'raisedBy': ?0, 'superAdmin' : ?1 }") //Find all by roll number and pending/accepted
    List<query> findAllByRollno(String raisedBy, boolean superAdmin);

    @Query(value = "{ 'raisedBy': { $ne: ?0 }, 'superAdmin' : ?1 }") //Find all except roll id and pending/accepted
    List<query> findAllByNotRollno(String raisedBy, boolean superAdmin);
}
