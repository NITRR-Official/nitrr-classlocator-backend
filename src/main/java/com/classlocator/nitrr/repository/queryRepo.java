package com.classlocator.nitrr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.query;

public interface queryRepo extends MongoRepository<query, String> {
    
}
