package com.classlocator.nitrr.entity;

import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO (Plain Old Java Object) class representing an Admin entity.
 * 
 * This class is mapped to the "admin" collection in MongoDB.
 * It stores information about an admin, including their roll number, 
 * password, name, department, and associated queries.
 */
@Document(collection = "admin")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class admin {

    /** Unique identifier for the admin (MongoDB ObjectId). */
    @Id
    private ObjectId Id;

    /** Roll number of the admin (must be unique). */
    @NonNull
    @Indexed(unique = true)
    private Integer rollno;

    /** Password of the admin (Non-null). */
    @NonNull
    private String password;

    /** Name of the admin. */
    @Builder.Default
    private String name = "NA";

    /** Department to which the admin belongs. */
    @Builder.Default
    private String department = "NA";

    /** Queries that are pending approval. */
    @DBRef
    @Builder.Default
    private HashSet<query> pendingQueries = new HashSet<>();

    /** Queries that have been accepted. */
    @DBRef
    @Builder.Default
    private HashSet<query> acceptedQueries = new HashSet<>();

    /** Queries that have been moved to trash. */
    @DBRef
    @Builder.Default
    private HashSet<trash> trashedQueries = new HashSet<>();

    /** Roles assigned to the admin (e.g., ADMIN, SUPER_ADMIN). */
    private List<String> roles;
}
