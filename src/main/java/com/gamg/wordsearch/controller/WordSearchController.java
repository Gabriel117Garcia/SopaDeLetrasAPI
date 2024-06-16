package com.gamg.wordsearch.controller;
import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.config.InvalidRequestException;
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

    @GetMapping("/Wordsearch/all")
    public ResponseEntity<List<WordSearchDTO>> getMangas() {
        return new ResponseEntity<>(wordSearchService.getAllWordSearches(), HttpStatus.OK);
    }

    @GetMapping("/Wordsearch/{id}")
    public ResponseEntity<WordSearchDTO> getWordSearchById(@PathVariable String id) {
        Optional<WordSearchDTO> wordsearch = wordSearchService.getWordSearchById(id);
        ResponseEntity<WordSearchDTO> result = ResponseEntity.notFound().build();

        if (wordsearch.isPresent()) {
            result = ResponseEntity.ok(wordsearch.get());
        }
        return result;
    }

    @PostMapping("/Wordsearch/create")
    public ResponseEntity<WordSearchDTO> createWordSearch(@Valid @RequestBody WordSearchDTO wordSearchDTO) throws InvalidRequestException {
        return new ResponseEntity<>(wordSearchService.createWordSearch(wordSearchDTO), HttpStatus.CREATED);
    }

    @PutMapping("/Wordsearch/update/{id}")
    public ResponseEntity<WordSearchDTO> updateWordSearch(@PathVariable String id, @Valid @RequestBody WordSearchDTO wordSearchDTO) {
        wordSearchService.updateWordSearch(wordSearchDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/Wordsearch/delete/{id}")
    public ResponseEntity<Void> deleteWordSearch(@PathVariable ObjectId id) {
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