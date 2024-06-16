package com.gamg.wordsearch.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Document("WordSearch")
@Data
public class WordSearchDTO {
    @NonNull
    @Size(min = 1, max = 20, message = "Las filas deben estar entre 1 y 20")
    private int rows;
    @NonNull
    @Size(min = 1, max = 20, message = "Las columnas deben estar entre 1 y 20")
    private int cols;
    @NonNull
    @Size(min = 1, message = "Debe haber al menos una palabra")
    private List<String> words;
}