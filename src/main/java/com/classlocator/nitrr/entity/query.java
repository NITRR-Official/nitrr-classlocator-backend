package com.classlocator.nitrr.entity;

import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO (Plain Old Java Object) class representing a Query entity.
 * 
 * This class is mapped to the "queries" collection in MongoDB.
 * It stores details about a query raised by an admin, including the 
 * room ID, name, description, the user who raised it, and voting details.
 */
@Document(collection = "queries")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class query {

    /** Unique identifier for the query (MongoDB ObjectId). */
    @Id
    private ObjectId id;

    /** Date when the query was raised. */
    @NonNull
    private String date;

    /** Room ID related to the query. */
    @NonNull
    @JsonProperty("Roomid")
    private Integer Roomid;

    /** Name/title of the query. */
    @NonNull
    private String name;

    /** Getter method to return the query ID as a string, overriding lombok default's one*/
    public String getId() {
        return id.toHexString();
    }

    /** Description of the issue or request. */
    @NonNull
    private String description;

    /** Roll number of the admin who raised the query. */
    @NonNull
    private String raisedBy;

    /** Map to store votes from admins, where key = roll number and value = vote status. */
    @Builder.Default
    private HashMap<Integer, Boolean> votes = new HashMap<>();

    /** Flag to indicate if the query is approved by a super admin. */
    @Builder.Default
    private boolean superAdmin = false;
}
