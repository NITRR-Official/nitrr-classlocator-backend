package com.classlocator.nitrr.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "sadmin")
@NoArgsConstructor
@Data
public class superAdmin {
    @Id
    private ObjectId id;
    
    @NonNull
    private String password;
    private String name;

    //Referencing to other collection will be carried out here
    @DBRef
    private List<query> pendingQueries = new ArrayList<>();
    @DBRef
    private List<query> acceptedQueries = new ArrayList<>();
    @DBRef
    private List<trash> trashedQueries = new ArrayList<>();
}
