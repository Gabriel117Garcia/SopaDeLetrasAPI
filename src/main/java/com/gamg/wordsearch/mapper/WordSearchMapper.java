package com.gamg.wordsearch.mapper;

import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.model.WordSearch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WordSearchMapper {
    WordSearchDTO toDTO(WordSearch model);
    WordSearch toModel(WordSearchDTO dto);
    List<WordSearchDTO> toDTOs(List<WordSearch> models);
    List<WordSearch> toModels(List<WordSearchDTO> dtos);
}
