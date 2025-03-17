package com.classlocator.nitrr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.superAdmin;

/**  
 * Repository interface for performing CRUD operations on the query collection.  
 *  
 * Extends MongoRepository to provide built-in database interaction methods.  
 */
public interface SuperAdminRepo extends MongoRepository<superAdmin, Integer> {
    
}
