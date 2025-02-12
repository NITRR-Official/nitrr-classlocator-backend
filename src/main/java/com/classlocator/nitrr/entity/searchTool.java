package com.classlocator.nitrr.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.classlocator.nitrr.interfaces.Pair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO (Plain Old Java Object) class representing a Search Tool entity.
 * 
 * This class is mapped to the "searchTool" collection in MongoDB.
 * It stores an identifier and a list of data pairs, where each entry consists of 
 * an ObjectId and a nested pair of strings.
 */
@Document(collection = "searchTool")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class searchTool {

    /** Unique identifier for the search tool entry. */
    @Id
    private Integer id;

    /**
     * List of data entries where each entry consists of:
     * - ObjectId (MongoDB identifier)
     * - A nested pair of two strings (key-value pairs for name and details/description).
     */
    @Builder.Default
    private List<Pair<ObjectId, Pair<String, String>>> data = new ArrayList<>();
}
