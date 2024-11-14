package com.classlocator.nitrr.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.admin;

public interface adminRepo extends MongoRepository<admin, ObjectId> {
    admin findByrollno(Integer rollno);
    
}