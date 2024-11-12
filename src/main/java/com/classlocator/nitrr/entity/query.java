package com.classlocator.nitrr.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "queries")
@NoArgsConstructor
@Data
public class query {
    @Id
    private Object id;

    @NonNull
    private Date date;
    @NonNull
    private int Roomid;
    @NonNull
    private String name;
    @NonNull
    private String descrption;
    @NonNull
    private Object raisedBy;
    
    private int votes = 0;
    private boolean superAdmin = false;

}
