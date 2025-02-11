package com.classlocator.nitrr.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.searchTool;

/**  
 * Repository interface for performing CRUD operations on the query collection.  
 *  
 * Extends MongoRepository to provide built-in database interaction methods.  
 */
public interface searchToolRepo extends MongoRepository<searchTool, Integer> {

}
