package com.classlocator.nitrr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.toJSON;

public interface toJSONRepo extends MongoRepository<toJSON, Integer> {
    
}
