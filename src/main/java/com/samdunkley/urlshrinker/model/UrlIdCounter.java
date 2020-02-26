package com.samdunkley.urlshrinker.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@RequiredArgsConstructor
public class UrlIdCounter {

    @Id
    private String id;

    @NonNull
    private long lastId;
}
