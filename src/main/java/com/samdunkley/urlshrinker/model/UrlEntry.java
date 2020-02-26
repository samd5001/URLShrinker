package com.samdunkley.urlshrinker.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@RequiredArgsConstructor
public class UrlEntry {

    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String urlID;

    @NonNull
    private String fullURL;
}
