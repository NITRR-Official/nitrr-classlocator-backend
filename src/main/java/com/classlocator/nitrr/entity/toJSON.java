package com.classlocator.nitrr.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "toJSON")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class toJSON {
    
    @Id
    private ObjectId id;

    @NonNull
    @Builder.Default
    private int mapVersion = 0;

    @NonNull
    private String searchTool;
    
}