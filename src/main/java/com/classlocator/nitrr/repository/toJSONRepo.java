package com.classlocator.nitrr.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.toJSON;

public interface toJSONRepo extends MongoRepository<toJSON, ObjectId> {
    
}
