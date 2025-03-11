package com.classlocator.nitrr.entity;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO (Plain Old Java Object) class representing a Super Admin entity.
 * 
 * This class is mapped to the "sadmin" collection in MongoDB.
 * It defines the super admin's details, including credentials, queries they manage, 
 * and their assigned roles.
 */
@Document(collection = "sadmin")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class superAdmin {

    /** Unique identifier for the super admin, initialized with a default ID. */
    @Id
    @Builder.Default
    private Integer id = 21116028;
    
    /** Password for authentication (must be provided). */
    @NonNull
    private String password;

    @Builder.Default
    private boolean active = true;

    /** Name of the super admin, defaulting to "Super Admin". */
    @Builder.Default
    private String name = "Super Admin";

    @NonNull
    private Integer phone;

    @NonNull 
    private String email;

    /**
     * Queries that are currently pending review by the super admin.
     * This is referenced from another collection in MongoDB.
     */
    @DBRef
    @Builder.Default
    private HashSet<query> pendingQueries = new HashSet<>();

    /**
     * Queries that have been accepted by the super admin.
     * This is referenced from another collection in MongoDB.
     */
    @DBRef
    @Builder.Default
    private HashSet<query> acceptedQueries = new HashSet<>();

    /**
     * Queries that have been moved to trash by the super admin.
     * This is referenced from another collection in MongoDB.
     */
    @DBRef
    @Builder.Default
    private HashSet<trash> trashedQueries = new HashSet<>();

    /** List of roles assigned to the super admin, such as ADMIN, SUPER_ADMIN, etc. */
    private List<String> roles;
}