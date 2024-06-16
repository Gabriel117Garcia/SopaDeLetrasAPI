package com.gamg.wordsearch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wordsearches")
public class WordSearch {
    @Id
    private String id;
    private int rows;
    private int cols;
    private char[][] grid;
    private List<String> words;
}