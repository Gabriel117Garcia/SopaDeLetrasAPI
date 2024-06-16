package com.gamg.wordsearch;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.MongoRepository.WordSearchRepository;
import com.gamg.wordsearch.config.InvalidRequestException;
import com.gamg.wordsearch.model.WordSearch;
import com.gamg.wordsearch.service.WordSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class WordSearchServiceTest {

	@Mock
	private WordSearchRepository wordSearchRepository;

	@InjectMocks
	private WordSearchService wordSearchService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllWordSearches() {
		when(wordSearchRepository.findAll()).thenReturn(List.of(new WordSearch()));
		assertFalse(wordSearchService.getAllWordSearches().isEmpty());
		verify(wordSearchRepository, times(1)).findAll();
	}

	@Test
	void testGetWordSearchById() {
		String id = "1";
		WordSearch wordSearch = new WordSearch();
		when(wordSearchRepository.findById(id)).thenReturn(Optional.of(wordSearch));
		assertTrue(wordSearchService.getWordSearchById(id).isPresent());
		verify(wordSearchRepository, times(1)).findById(id);
	}

	@Test
	void testCreateWordSearch() {
		WordSearchDTO dto = new WordSearchDTO();
		dto.setRows(10);
		dto.setCols(10);
		dto.setWords(Collections.singletonList("example"));

		WordSearch wordSearch = WordSearch.builder()
				.rows(dto.getRows())
				.cols(dto.getCols())
				.words(dto.getWords())
				.grid(new char[10][10])
				.build();

		when(wordSearchRepository.save(any(WordSearch.class))).thenReturn(wordSearch);

		WordSearch created = null;
		try {
			created = wordSearchService.createWordSearch(wordSearch);
		} catch (InvalidRequestException e) {
			throw new RuntimeException(e);
		}
		assertNotNull(created);
		assertEquals(10, created.getRows());
		verify(wordSearchRepository, times(1)).save(any(WordSearch.class));
	}

	@Test
	void testUpdateWordSearch() {
		String id = "1";
		WordSearch dto = new WordSearch();
		dto.setRows(10);
		dto.setCols(10);
		dto.setWords(Collections.singletonList("example"));

		WordSearch existingWordSearch = new WordSearch();
		existingWordSearch.setId(id);

		when(wordSearchRepository.findById(id)).thenReturn(Optional.of(existingWordSearch));
		when(wordSearchRepository.save(any(WordSearch.class))).thenReturn(existingWordSearch);

		WordSearch updated = wordSearchService.updateWordSearch(id, dto);
		assertNotNull(updated);
		verify(wordSearchRepository, times(1)).findById(id);
		verify(wordSearchRepository, times(1)).save(any(WordSearch.class));
	}

	@Test
	void testDeleteWordSearch() {
		String id = "1";
		when(wordSearchRepository.existsById(id)).thenReturn(true);
		doNothing().when(wordSearchRepository).deleteById(id);

		wordSearchService.deleteWordSearch(id);
		verify(wordSearchRepository, times(1)).existsById(id);
		verify(wordSearchRepository, times(1)).deleteById(id);
	}
}