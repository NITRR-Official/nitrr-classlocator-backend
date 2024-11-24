package com.classlocator.nitrr.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.classlocator.nitrr.interfaces.Pair;


import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "searchTool")
@NoArgsConstructor
@Data
public class searchTool {
    @Id
    private Integer id;

    private List<Pair<String, Pair<String, String>>> data;
}
