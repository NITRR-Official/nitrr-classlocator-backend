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

@Document(collection = "sadmin")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class superAdmin {
    @Id
    @Builder.Default
    private Integer id = 21116028;
    
    @NonNull
    private String password;

    @Builder.Default
    private String name = "Super Admin";

    //Referencing to other collection will be carried out here
    @DBRef
    @Builder.Default
    private HashSet<query> pendingQueries = new HashSet<>();
    @DBRef
    @Builder.Default
    private HashSet<query> acceptedQueries = new HashSet<>();
    @DBRef
    @Builder.Default
    private HashSet<trash> trashedQueries = new HashSet<>();

     //Roles will be defined here like ADMIN, SUPER_ADMIN etc.
     private List<String> roles;
}
