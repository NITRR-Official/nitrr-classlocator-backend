package com.classlocator.nitrr.entity;

import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "admin")
@NoArgsConstructor
@Data
public class admin {
    @Id
    private ObjectId Id;

    @NonNull
    @Indexed(unique = true)
    private Integer rollno;

    @NonNull
    private String password;
    private String name;
    private String department;

    //Referencing to other collection will be carried out here
    @DBRef
    private HashSet<query> pendingQueries = new HashSet<>();
    @DBRef
    private HashSet<query> acceptedQueries = new HashSet<>();
    @DBRef
    private HashSet<trash> trashedQueries = new HashSet<>();

    //Roles will be defined here like ADMIN, SUPER_ADMIN etc.
    private List<String> roles;
}
