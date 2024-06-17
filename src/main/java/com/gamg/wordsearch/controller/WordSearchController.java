package com.gamg.wordsearch.controller;
import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.config.InvalidRequestException;
import com.gamg.wordsearch.config.ResourceNotFoundException;
import com.gamg.wordsearch.mapper.IWordSearchMapper;
import com.gamg.wordsearch.model.WordSearch;
import com.gamg.wordsearch.service.WordSearchService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Wordsearch")
public class WordSearchController {

    @Autowired
    private WordSearchService wordSearchService;

    @Autowired
    private IWordSearchMapper wordSearchMapper;


    @GetMapping("/Wordsearch/all")
    public ResponseEntity<List<WordSearchDTO>> getWordSearches() {
        List<WordSearch> wordSearches = wordSearchService.getAllWordSearches();
        return new ResponseEntity<>(wordSearchMapper.toDTOs(wordSearches), HttpStatus.OK);
    }

    @GetMapping("/Wordsearch/{id}")
    public ResponseEntity<WordSearchDTO> getWordSearchById(@PathVariable ObjectId id) {
        Optional<WordSearch> wordSearch = wordSearchService.getWordSearchById(id);
        WordSearchDTO wordSearchDTO = wordSearch.map(wordSearchMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException(id.toString()));
        return new ResponseEntity<>(wordSearchDTO, HttpStatus.OK);
    }

    @PostMapping("/Wordsearch/create")
    public ResponseEntity<WordSearchDTO> createWordSearch(@Valid @RequestBody WordSearch wordSearch) {
        WordSearch savedWordsearch = wordSearchService.createWordSearch(wordSearch);
        WordSearchDTO wordSearchDTO = wordSearchMapper.toDTO(savedWordsearch);
        return new ResponseEntity<>(wordSearchDTO, HttpStatus.CREATED);
    }

    @PutMapping("/Wordsearch/update/{id}")
    public ResponseEntity<WordSearchDTO> updateWordSearch(@PathVariable ObjectId id, @Valid @RequestBody WordSearch wordSearch) {
        WordSearch updatedWordsearch = wordSearchService.updateWordSearch(id ,wordSearch);
        WordSearchDTO wordsearchDTO = wordSearchMapper.toDTO(updatedWordsearch);
        return new ResponseEntity<>(wordsearchDTO, HttpStatus.OK);
    }

    @DeleteMapping("/Wordsearch/delete/{id}")
    public ResponseEntity<Void> deleteWordSearch(@PathVariable ObjectId id) {
        wordSearchService.deleteWordSearch(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<Boolean> verifyWord(@PathVariable ObjectId id) {
        wordSearchService.deleteWordSearch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/shuffle")
    public ResponseEntity<WordSearch> shuffleGrid(@PathVariable String id) {
        WordSearch shuffledWordSearch = wordSearchService.shuffleGrid(id);
        return ResponseEntity.ok(shuffledWordSearch);
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<List<String>> solveGrid(@PathVariable String id) {
        List<String> foundWords = wordSearchService.solveGrid(id);
        return ResponseEntity.ok(foundWords);
    }
}