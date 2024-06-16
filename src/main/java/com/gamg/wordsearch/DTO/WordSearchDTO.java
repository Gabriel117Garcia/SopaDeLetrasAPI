package com.gamg.wordsearch.DTO;

import lombok.Data;

import java.util.List;

@Data
public class WordSearchDTO {
    private int rows;
    private int cols;
    private List<String> words;
}