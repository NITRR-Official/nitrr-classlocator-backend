package com.classlocator.nitrr.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO (Plain Old Java Object) class representing a JSON data storage entity.
 * 
 * This class is mapped to the "toJSON" collection in MongoDB.
 * It holds details related to map versions and associated search tools.
 */
@Document(collection = "toJSON")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class toJSON {
    
    /** Unique identifier for the document in MongoDB. */
    @Id
    private ObjectId id;

    /** Version number of the map, initialized to 0 by default. */
    @NonNull
    @Builder.Default
    private int mapVersion = 0;

    /** JSON string representation of the search tool data. */
    @NonNull
    private String searchTool;
}