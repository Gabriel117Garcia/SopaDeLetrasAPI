package com.gamg.wordsearch.MongoRepository;

import com.gamg.wordsearch.model.WordSearch;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface WordSearchRepository extends MongoRepository<WordSearch, String> {
    Optional<WordSearch> findById(String id);
}