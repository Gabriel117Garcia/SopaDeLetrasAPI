package com.gamg.wordsearch;

import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.MongoRepository.WordSearchRepository;
import com.gamg.wordsearch.config.InvalidRequestException;
import com.gamg.wordsearch.model.WordSearch;
import com.gamg.wordsearch.service.WordSearchService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WordSearchControllerTest {

    @Mock
    private WordSearchRepository wordSearchRepository;
    @InjectMocks
    private WordSearchService wordSearchService;
    WordSearch wordsearch;

    @BeforeEach
    public void setUp() {
        wordsearch = new WordSearch();
        wordsearch.setId(new ObjectId("60c72b2f9b1e8a1a4c8d9102"));
        wordsearch.setRows(10);
        wordsearch.setCols(10);
        wordsearch.setWords(Arrays.asList("word1", "word2", "word3"));
    }

    @Test
    void testAddWordSearch() {

        when(wordSearchService.createWordSearch(wordsearch)).thenReturn(wordsearch);
        WordSearch result = wordSearchService.createWordSearch(wordsearch);

        assertNotNull(result);
        assertEquals(wordsearch.getCols(), result.getCols());
        assertEquals(wordsearch.getRows(), result.getRows());
        assertEquals(wordsearch.getWords(), result.getWords());
    }

    @Test
    void testGetWordSearchById() {
        when(wordSearchRepository.findById(wordsearch.getId())).thenReturn(Optional.of(wordsearch));

        Optional<WordSearch> result = wordSearchService.getWordSearchById(wordsearch.getId());

        assertTrue(result.isPresent());
        assertEquals(wordsearch.getCols(), result.get().getCols());
        assertEquals(wordsearch.getRows(), result.get().getRows());
        assertEquals(wordsearch.getWords(), result.get().getWords());

    }

    @Test
    void testUpdateWordSearch() throws Exception{

        WordSearch updatedWordsearch = new WordSearch();
        updatedWordsearch.setId(wordsearch.getId());
        updatedWordsearch.setCols(20);
        updatedWordsearch.setRows(20);
        updatedWordsearch.setWords(Arrays.asList("word4", "word5", "word6"));

        when(wordSearchRepository.findById(wordsearch.getId())).thenReturn(Optional.of(wordsearch));
        when(wordSearchRepository.save(updatedWordsearch)).thenReturn(updatedWordsearch);

        WordSearch result = wordSearchService.updateWordSearch(wordsearch.getId() ,wordsearch);

        assertNotNull(result);
        assertEquals(wordsearch.getCols(), result.getCols());
        assertEquals(wordsearch.getRows(), result.getRows());
        assertEquals(wordsearch.getWords(), result.getWords());
    }

    @Test
    void testDeleteWordSearch() {
        wordSearchService.deleteWordSearch(wordsearch.getId());
        Mockito.verify(wordSearchRepository, times(1)).deleteById(wordsearch.getId());
    }


}