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

@Document(collection = "searchTool")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class searchTool {
    @Id
    private Integer id;

    @Builder.Default
    private List<Pair<ObjectId, Pair<String, String>>> data = new ArrayList<Pair<ObjectId, Pair<String, String>>>();
}
