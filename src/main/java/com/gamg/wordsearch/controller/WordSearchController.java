package com.gamg.wordsearch.controller;
import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.config.InvalidRequestException;
import com.gamg.wordsearch.model.WordSearch;
import com.gamg.wordsearch.service.WordSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/Wordsearch")
public class WordSearchController {

    @Autowired
    private WordSearchService wordSearchService;

    @GetMapping("/Wordsearch/all")
    public List<WordSearch> getAllWordSearches() {
        return wordSearchService.getAllWordSearches();
    }

    @GetMapping("/Wordsearch/{id}")
    public ResponseEntity<WordSearch> getWordSearchById(@PathVariable String id) {
        return wordSearchService.getWordSearchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/Wordsearch/create")
    public WordSearch createWordSearch(@Valid @RequestBody WordSearchDTO wordSearchDTO) throws InvalidRequestException {
        WordSearch wordSearch = WordSearch.builder()
                .rows(wordSearchDTO.getRows())
                .cols(wordSearchDTO.getCols())
                .words(wordSearchDTO.getWords())
                .build();
        return wordSearchService.createWordSearch(wordSearch);
    }

    @PutMapping("/Wordsearch/update/{id}")
    public ResponseEntity<WordSearch> updateWordSearch(@PathVariable String id, @Valid @RequestBody WordSearchDTO wordSearchDTO) {
        WordSearch wordSearchDetails = WordSearch.builder()
                .rows(wordSearchDTO.getRows())
                .cols(wordSearchDTO.getCols())
                .words(wordSearchDTO.getWords())
                .build();
        return ResponseEntity.ok(wordSearchService.updateWordSearch(id, wordSearchDetails));
    }

    @DeleteMapping("/Wordsearch/delete/{id}")
    public ResponseEntity<Void> deleteWordSearch(@PathVariable String id) {
        wordSearchService.deleteWordSearch(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<Boolean> verifyWord(@PathVariable String id, @RequestParam String word) {
        boolean result = wordSearchService.verifyWord(id, word);
        return ResponseEntity.ok(result);
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