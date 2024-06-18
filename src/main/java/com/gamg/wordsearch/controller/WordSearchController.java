package com.gamg.wordsearch.controller;

import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.config.ResourceNotFoundException;
import com.gamg.wordsearch.mapper.WordSearchMapper;
import com.gamg.wordsearch.model.WordSearch;
import com.gamg.wordsearch.service.WordSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Wordsearch")
@Tag(name = "WordSearch", description = "API para operaciones con WordSearch")
public class WordSearchController {

    @Autowired
    private WordSearchService wordSearchService;

    @Autowired
    private WordSearchMapper wordSearchMapper;

    @Operation(summary = "Obtener todas las Sopas de letras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sopas de letras encontradas",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WordSearchDTO.class)) })
    })
    @GetMapping("/all")
    public ResponseEntity<List<WordSearchDTO>> getWordSearches() {
        List<WordSearch> wordSearches = wordSearchService.getAllWordSearches();
        return new ResponseEntity<>(wordSearchMapper.toDTOs(wordSearches), HttpStatus.OK);
    }

    @Operation(summary = "Obtener cualquier Sopa de letras por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sopa de letras encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WordSearchDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sopa de letras no encontrada",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<WordSearchDTO> getWordSearchById(@Parameter(description = "ID de la WordSearch") @PathVariable ObjectId id) {
        Optional<WordSearch> wordSearch = wordSearchService.getWordSearchById(id);
        WordSearchDTO wordSearchDTO = wordSearch.map(wordSearchMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));
        return new ResponseEntity<>(wordSearchDTO, HttpStatus.OK);
    }

    @Operation(summary = "Crear una nueva Sopa de letras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sopa de letras creada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WordSearchDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content) })
    @PostMapping("/create")
    public ResponseEntity<WordSearchDTO> createWordSearch(@RequestBody WordSearch wordSearch) {
        WordSearch savedWordsearch = wordSearchService.createWordSearch(wordSearch);
        WordSearchDTO wordSearchDTO = wordSearchMapper.toDTO(savedWordsearch);
        return new ResponseEntity<>(wordSearchDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar los parametros de una sopa de letra existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sopa de letras actualizada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WordSearchDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sopa de letras no encontrada",
                    content = @Content) })
    @PutMapping("/update/{id}")
    public ResponseEntity<WordSearchDTO> updateWordSearch(@Parameter(description = "ID de la WordSearch") @PathVariable ObjectId id, @RequestBody WordSearch wordSearch) {
        WordSearch updatedWordSearch = wordSearchService.updateWordSearch(id, wordSearch);
        WordSearchDTO wordSearchDTO = wordSearchMapper.toDTO(updatedWordSearch);
        return new ResponseEntity<>(wordSearchDTO, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una Sopa de letras por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sopa de letras eliminada",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sopa de letras no encontrada",
                    content = @Content) })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWordSearch(@Parameter(description = "ID de la WordSearch") @PathVariable ObjectId id) {
        wordSearchService.deleteWordSearch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Verificar si una palabra fue encontrada en la Sopa de letras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Palabra encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Palabra no encontrada",
                    content = @Content) })
    @PostMapping("/{id}/verify")
    public ResponseEntity<Boolean> verifyWord(@Parameter(description = "ID de la WordSearch") @PathVariable ObjectId id, @RequestBody String word) {
        wordSearchService.verifyWord(id, word);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Mezclar la cuadrícula de la sopa de letras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuadrícula mezclada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WordSearch.class)) }),
            @ApiResponse(responseCode = "404", description = "Sopa de letras no encontrada",
                    content = @Content) })
    @PostMapping("/{id}/shuffle")
    public ResponseEntity<WordSearch> shuffleGrid(@Parameter(description = "ID de la WordSearch") @PathVariable ObjectId id) {
        WordSearch shuffledWordSearch = wordSearchService.shuffleGrid(id);
        return ResponseEntity.ok(shuffledWordSearch);
    }

    @Operation(summary = "Resolver la cuadrícula de la sopa de letras y encontrar las palabras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Palabras encontradas",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WordSearch.class)) }),
            @ApiResponse(responseCode = "404", description = "Sopa de letras no encontrada",
                    content = @Content) })
    @PostMapping("/{id}/solve")
    public ResponseEntity<List<String>> solveGrid(@Parameter(description = "ID de la WordSearch") @PathVariable ObjectId id) {
        List<String> foundWords = wordSearchService.solveGrid(id);
        return ResponseEntity.ok(foundWords);
    }
}