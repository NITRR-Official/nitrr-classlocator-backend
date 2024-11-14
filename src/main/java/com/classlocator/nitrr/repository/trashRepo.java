package com.classlocator.nitrr.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.trash;

public interface trashRepo extends MongoRepository<trash, ObjectId> {
    
}
