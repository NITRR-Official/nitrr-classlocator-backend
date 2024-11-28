package com.classlocator.nitrr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.superAdmin;

public interface superAdminRepo extends MongoRepository<superAdmin, Integer> {
    
}
