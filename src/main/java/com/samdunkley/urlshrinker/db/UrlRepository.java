package com.samdunkley.urlshrinker.db;

import com.samdunkley.urlshrinker.model.UrlEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<UrlEntry, String> {

    public Optional<UrlEntry> findByUrlID(String urlId);

    public Optional<UrlEntry> findByFullURL(String fullUrl);
}
