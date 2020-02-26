package com.samdunkley.urlshrinker.db;

import com.samdunkley.urlshrinker.model.UrlIdCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlIdRepository extends MongoRepository<UrlIdCounter, String> {
}
