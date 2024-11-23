package com.classlocator.nitrr.entity;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "searchTool")
@NoArgsConstructor
@Data
public class searchTool {
    @Id
    private int id;

    private HashMap<String, Pair<String, String>> data;
}
