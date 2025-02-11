package com.classlocator.nitrr.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.toJSON;

/**  
 * Repository interface for performing CRUD operations on the query collection.  
 *  
 * Extends MongoRepository to provide built-in database interaction methods.  
 */
public interface toJSONRepo extends MongoRepository<toJSON, ObjectId> {
    
}
