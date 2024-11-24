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

@Document(collection = "queries")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class query {
    @Id
    private ObjectId id;

    @NonNull
    private String date;
    @NonNull
    @JsonProperty("Roomid")
    private Integer Roomid;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private String raisedBy;
    
    private HashMap<Integer, Boolean> votes;
    @Builder.Default
    private boolean superAdmin = false;

}
