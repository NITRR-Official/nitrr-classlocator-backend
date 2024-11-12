package com.classlocator.nitrr.entity;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Document(collection = "trash")
public class trash {
    @Id
    private Object id;

    @NonNull
    private Object query;

    private boolean isSuperAdmin = false;
    private boolean admin = false;
    @NonNull
    private Date date;
}
