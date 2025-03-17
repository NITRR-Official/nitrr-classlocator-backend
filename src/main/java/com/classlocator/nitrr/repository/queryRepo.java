package com.classlocator.nitrr.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.classlocator.nitrr.entity.query;

/**  
 * Repository interface for performing CRUD operations on the query collection.  
 *  
 * Extends MongoRepository to provide built-in database interaction methods.  
 */
public interface QueryRepo extends MongoRepository<query, ObjectId> {

    /**  
     * Finds the first query by Room ID and the user who raised it, sorted by `_id`.  
     *  
     * @param Roomid Integer - The ID of the room.  
     * @param raisedBy String - The user who raised the query.  
     * @return Optional<query> - Returns the first matching query, if found.  
     */
    @Query(value = "{ 'Roomid': ?0, 'raisedBy': ?1 }", sort = "{ '_id': 1 }")
    Optional<query> findFirstByRoomidAndRaisedBy(Integer roomId, String raisedBy);

    /**  
     * Finds all queries raised by a specific user and their approval status by the super admin.  
     *  
     * @param raisedBy String - The user who raised the queries.  
     * @param superAdmin boolean - Approval status by the super admin (true/false).  
     * @return List<query> - Returns a list of matching queries.  
     */
    @Query(value = "{ 'raisedBy': ?0, 'superAdmin' : ?1 }")
    List<query> findAllByRollno(String raisedBy, boolean superAdmin);

    /**  
     * Finds all queries except those raised by a specific user and filters them by super admin approval status.  
     *  
     * @param raisedBy String - The user whose queries should be excluded.  
     * @param superAdmin boolean - Approval status by the super admin (true/false).  
     * @return List<query> - Returns a list of matching queries.  
     */
    @Query(value = "{ 'raisedBy': { $ne: ?0 }, 'superAdmin' : ?1 }")
    List<query> findAllByNotRollno(String raisedBy, boolean superAdmin);
}
