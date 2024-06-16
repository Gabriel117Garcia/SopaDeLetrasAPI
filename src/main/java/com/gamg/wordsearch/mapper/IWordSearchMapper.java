package com.gamg.wordsearch.mapper;

import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.model.WordSearch;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IWordSearchMapper {
    WordSearchDTO toDTO(WordSearch model);
    WordSearch toModel(WordSearchDTO dto);
    List<WordSearchDTO> toDTOs(List<WordSearch> models);
    List<WordSearch> toModels(List<WordSearchDTO> dtos);
}
