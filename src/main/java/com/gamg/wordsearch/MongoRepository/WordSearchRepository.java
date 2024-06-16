package com.gamg.wordsearch.MongoRepository;

import com.gamg.wordsearch.model.WordSearch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordSearchRepository extends MongoRepository<WordSearch, String> {
}