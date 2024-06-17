package com.gamg.wordsearch.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "WordSearch")
@Schema(name = "WordSearch", description = "Modelo de la Sopa de letras")
public class WordSearch {
    @Id
    @Schema(description = "ID de la Sopa de letras", example = "60c72b2f9b1e8a1a4c8d9102")
    private ObjectId id;
    @Schema(description = "Número de filas de la Sopa de letras", example = "10")
    private int rows;
    @Schema(description = "Número de columnas de la Sopa de letras", example = "10")
    private int cols;
    @Schema(description = "Matriz de la Sopa de letras", example = "[['a','b','c'],['d','e','f'],['g','h','i']]")
    private char[][] grid;
    @Schema(description = "Palabras de la Sopa de letras", example = "[\"word1\", \"word2\", \"word3\"]")
    private List<String> words;
}
