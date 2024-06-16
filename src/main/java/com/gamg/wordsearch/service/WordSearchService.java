package com.gamg.wordsearch.service;
import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.MongoRepository.WordSearchRepository;
import com.gamg.wordsearch.config.InvalidRequestException;
import com.gamg.wordsearch.config.ResourceNotFoundException;
import com.gamg.wordsearch.mapper.IWordSearchMapper;
import com.gamg.wordsearch.model.WordSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class WordSearchService {

    @Autowired
    private WordSearchRepository wordSearchRepository;

    @Autowired
    private IWordSearchMapper wordSearchMapper;

    public List<WordSearchDTO> getAllWordSearches() {
        List<WordSearch> wordSearchList = wordSearchRepository.findAll();

        return wordSearchMapper.toDTOs(wordSearchList);
    }

    public Optional<WordSearchDTO> getWordSearchById(String id) {
        Optional<WordSearch> wordSearchOptional = wordSearchRepository.findById(id);
        if (wordSearchOptional.isPresent()) {
            WordSearch wordSearch = wordSearchOptional.get();
            WordSearchDTO wordSearchDTO = wordSearchMapper.toDTO(wordSearch);
            return Optional.of(wordSearchDTO);
        } else {
            return Optional.empty();
        }

    }

    public WordSearchDTO createWordSearch(WordSearchDTO wordSearchDTO) throws InvalidRequestException {
        // Convertir de DTO a Modelo
        WordSearch wordSearch = wordSearchMapper.toModel(wordSearchDTO);
        // Validar filas y columnas
        if (wordSearch.getRows() <= 0 || wordSearch.getCols() <= 0) {
            throw new InvalidRequestException("Rows and columns must be positive numbers.");
        }
        // Generar la sopa de letras
        wordSearch.setGrid(generateGrid(wordSearch.getRows(), wordSearch.getCols(), wordSearch.getWords()));
        // Guardar la entidad en la base de datos
        wordSearch = wordSearchRepository.save(wordSearch);
        // Convertir de Entidad a DTO y retornar
        return wordSearchMapper.toDTO(wordSearch);
    }


    public WordSearchDTO updateWordSearch(String id, WordSearchDTO wordSearchDTO) {
        return wordSearchRepository.findById(id)
                .map(existingWordSearch -> {
                    WordSearch updatedWordSearch = wordSearchMapper.toModel(wordSearchDTO);
                    updatedWordSearch.setId(existingWordSearch.getId());
                    wordSearchRepository.save(updatedWordSearch);
                    return wordSearchMapper.toDTO(updatedWordSearch);
                }).orElseThrow(() -> new ResourceNotFoundException("WordSearch not found with id " + id));
    }


    public void deleteWordSearch(String id) {
        if (!wordSearchRepository.existsById(id)) {
            throw new ResourceNotFoundException("WordSearch not found with id " + id);
        }
        wordSearchRepository.deleteById(id);
    }

    public boolean verifyWord(String id, String word) {
        Optional<WordSearch> wordSearchOpt = wordSearchRepository.findById(id);
        if (wordSearchOpt.isPresent()) {
            WordSearch wordSearch = wordSearchOpt.get();
            return checkWordInGrid(wordSearch.getGrid(), word);
        }
        throw new ResourceNotFoundException("WordSearch not found with id " + id);
    }

    public WordSearch shuffleGrid(String id) {
        Optional<WordSearch> wordSearchOpt = wordSearchRepository.findById(id);
        if (wordSearchOpt.isPresent()) {
            WordSearch wordSearch = wordSearchOpt.get();
            wordSearch.setGrid(fillEmptySpaces(wordSearch.getGrid()));
            return wordSearchRepository.save(wordSearch);
        }
        throw new ResourceNotFoundException("WordSearch not found with id " + id);
    }

    public List<String> solveGrid(String id) {
        Optional<WordSearch> wordSearchOpt = wordSearchRepository.findById(id);
        if (wordSearchOpt.isPresent()) {
            WordSearch wordSearch = wordSearchOpt.get();
            return findAllWords(wordSearch.getGrid(), wordSearch.getWords());
        }
        throw new ResourceNotFoundException("WordSearch not found with id " + id);
    }

    private char[][] generateGrid(int rows, int cols, List<String> words) {
        char[][] grid = new char[rows][cols];
        // Inicializar la cuadrícula con espacios vacíos
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = ' ';
            }
        }
        // Colocar las palabras en la cuadrícula
        for (String word : words) {
            placeWordInGrid(grid, word);
        }
        return grid;
    }

    private void placeWordInGrid(char[][] grid, String word) {
        Random rand = new Random();
        int rows = grid.length;
        int cols = grid[0].length;
        int direction = rand.nextInt(8); // 0: derecha, 1: izquierda, 2: abajo, 3: arriba, 4-7: diagonales

        boolean placed = false;
        while (!placed) {
            int startRow = rand.nextInt(rows);
            int startCol = rand.nextInt(cols);
            placed = canPlaceWord(grid, word, startRow, startCol, direction);
            if (placed) {
                placeWord(grid, word, startRow, startCol, direction);
            }
        }
    }

    private boolean canPlaceWord(char[][] grid, String word, int row, int col, int direction) {
        int rows = grid.length;
        int cols = grid[0].length;
        int len = word.length();

        switch (direction) {
            case 0: // derecha
                if (col + len > cols) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row][col + i] != ' ' && grid[row][col + i] != word.charAt(i)) return false;
                }
                break;
            case 1: // izquierda
                if (col - len < -1) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row][col - i] != ' ' && grid[row][col - i] != word.charAt(i)) return false;
                }
                break;
            case 2: // abajo
                if (row + len > rows) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row + i][col] != ' ' && grid[row + i][col] != word.charAt(i)) return false;
                }
                break;
            case 3: // arriba
                if (row - len < -1) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row - i][col] != ' ' && grid[row - i][col] != word.charAt(i)) return false;
                }
                break;
            case 4: // diagonal abajo derecha
                if (row + len > rows || col + len > cols) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row + i][col + i] != ' ' && grid[row + i][col + i] != word.charAt(i)) return false;
                }
                break;
            case 5: // diagonal arriba izquierda
                if (row - len < -1 || col - len < -1) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row - i][col - i] != ' ' && grid[row - i][col - i] != word.charAt(i)) return false;
                }
                break;
            case 6: // diagonal abajo izquierda
                if (row + len > rows || col - len < -1) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row + i][col - i] != ' ' && grid[row + i][col - i] != word.charAt(i)) return false;
                }
                break;
            case 7: // diagonal arriba derecha
                if (row - len < -1 || col + len > cols) return false;
                for (int i = 0; i < len; i++) {
                    if (grid[row - i][col + i] != ' ' && grid[row - i][col + i] != word.charAt(i)) return false;
                }
                break;
        }
        return true;
    }

    private void placeWord(char[][] grid, String word, int row, int col, int direction) {
        int len = word.length();
        switch (direction) {
            case 0: // derecha
                for (int i = 0; i < len; i++) {
                    grid[row][col + i] = word.charAt(i);
                }
                break;
            case 1: // izquierda
                for (int i = 0; i < len; i++) {
                    grid[row][col - i] = word.charAt(i);
                }
                break;
            case 2: // abajo
                for (int i = 0; i < len; i++) {
                    grid[row + i][col] = word.charAt(i);
                }
                break;
            case 3: // arriba
                for (int i = 0; i < len; i++) {
                    grid[row - i][col] = word.charAt(i);
                }
                break;
            case 4: // diagonal abajo derecha
                for (int i = 0; i < len; i++) {
                    grid[row + i][col + i] = word.charAt(i);
                }
                break;
            case 5: // diagonal arriba izquierda
                for (int i = 0; i < len; i++) {
                    grid[row - i][col - i] = word.charAt(i);
                }
                break;
            case 6: // diagonal abajo izquierda
                for (int i = 0; i < len; i++) {
                    grid[row + i][col - i] = word.charAt(i);
                }
                break;
            case 7: // diagonal arriba derecha
                for (int i = 0; i < len; i++) {
                    grid[row - i][col + i] = word.charAt(i);
                }
                break;
        }
    }

    private char[][] fillEmptySpaces(char[][] grid) {
        Random rand = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == ' ') {
                    grid[i][j] = (char) ('A' + rand.nextInt(26));
                }
            }
        }
        return grid;
    }

    private boolean checkWordInGrid(char[][] grid, String word) {
        // Implementación para verificar si una palabra está en la sopa de letras
        // Aquí se puede agregar la lógica para buscar horizontal, vertical y diagonalmente
        return false;
    }

    private List<String> findAllWords(char[][] grid, List<String> words) {
        // Implementación para encontrar todas las palabras en la sopa de letras
        // Se puede usar la lógica de checkWordInGrid para cada palabra en la lista
        return words.stream()
                .filter(word -> checkWordInGrid(grid, word))
                .toList();
    }
}