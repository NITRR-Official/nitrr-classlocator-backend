package com.classlocator.nitrr.entity;

import java.util.ArrayList;
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
    private List<query> pendingQueries = new ArrayList<>();
    @DBRef
    private List<query> acceptedQueries = new ArrayList<>();
    @DBRef
    private List<trash> trashedQueries = new ArrayList<>();

}
