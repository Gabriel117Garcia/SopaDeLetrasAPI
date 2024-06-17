package com.gamg.wordsearch.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Document("WordSearch")
@Data
@Schema(name = "WordSearch", description = "WordSearch model")
public class WordSearchDTO {
    @Schema(description = "ID de la WordSearch", example = "60c72b2f9b1e8a1a4c8d9102")
    private ObjectId id;
    @Schema(description = "Número de filas de la sopa de letras", example = "10")
    private int rows;
    @Schema(description = "Número de columnas de la sopa de letras", example = "10")
    private int cols;
    @Schema(description = "Lista de palabras a buscar en la sopa de letras", example = "[\"word1\", \"word2\", \"word3\"]")
    private List<String> words;
}