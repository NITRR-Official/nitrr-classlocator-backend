package com.classlocator.nitrr.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.searchTool;

public interface searchToolRepo extends MongoRepository<searchTool, ObjectId> {
    
}
