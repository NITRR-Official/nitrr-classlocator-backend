package com.classlocator.nitrr.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "toJSON")
@NoArgsConstructor
@Data
public class toJSON {
    
    @Id
    private int mapVersion;

    @NonNull
    private String searchTool;
    
}