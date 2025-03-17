package com.classlocator.nitrr.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.classlocator.nitrr.entity.admin;

/**  
 * Repository interface for performing CRUD operations on the admin collection.  
 *  
 * Extends MongoRepository to provide built-in database interaction methods.  
 */
public interface AdminRepo extends MongoRepository<admin, ObjectId> {

    /**  
     * Finds an admin by their roll number.  
     *  
     * @param rollno Integer - The roll number of the admin.  
     * @return admin - Returns the admin entity if found, otherwise null.  
     */
    admin findByrollno(Integer rollno);
}
