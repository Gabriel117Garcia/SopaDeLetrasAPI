package com.gamg.wordsearch.service;

import com.gamg.wordsearch.MongoRepository.WordSearchRepository;
import com.gamg.wordsearch.config.ResourceNotFoundException;
import com.gamg.wordsearch.model.WordSearch;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class WordSearchService {

    @Autowired
    private WordSearchRepository wordSearchRepository;


    public List<WordSearch> getAllWordSearches() {
        return wordSearchRepository.findAll();
    }

    public Optional<WordSearch> getWordSearchById(ObjectId id) {
        return wordSearchRepository.findById(id);
    }

    public WordSearch createWordSearch(WordSearch wordSearch) {
        Optional<WordSearch> existingAlbum = wordSearchRepository.findById(wordSearch.getId());
        if (existingAlbum.isPresent()) {
            throw new ResourceNotFoundException("WordSearch already exists with id " + wordSearch.getId());
        }
        return wordSearchRepository.save(wordSearch);
    }


    public WordSearch updateWordSearch(ObjectId id,WordSearch wordSearch) {
        Optional<WordSearch> existingWordsearch = wordSearchRepository.findById(id);
        if (existingWordsearch.isPresent()) {
            wordSearch.setId(id);
            return wordSearchRepository.save(wordSearch);
        } else {
            throw new ResourceNotFoundException("WordSearch not found with id " + id);
        }
    }


    public void deleteWordSearch(ObjectId id) {
        wordSearchRepository.deleteById(id);
    }

    public boolean verifyWord(ObjectId id, String word) {
        Optional<WordSearch> wordSearchOpt = wordSearchRepository.findById(id);
        if (wordSearchOpt.isPresent()) {
            WordSearch wordSearch = wordSearchOpt.get();
            return checkWordInGrid(wordSearch.getGrid(), word);
        }
        throw new ResourceNotFoundException("WordSearch not found with id " + id);
    }

    public WordSearch shuffleGrid(ObjectId id) {
        Optional<WordSearch> wordSearchOpt = wordSearchRepository.findById(id);
        if (wordSearchOpt.isPresent()) {
            WordSearch wordSearch = wordSearchOpt.get();
            wordSearch.setGrid(fillEmptySpaces(wordSearch.getGrid()));
            return wordSearchRepository.save(wordSearch);
        }
        throw new ResourceNotFoundException("WordSearch not found with id " + id);
    }

    public List<String> solveGrid(ObjectId id) {
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

    // Helper method to check if the word exists starting from (row, col) in a given direction
    private boolean checkDirection(char[][] grid, String word, int row, int col, int deltaRow, int deltaCol) {
        int wordLength = word.length();
        int rows = grid.length;
        int cols = grid[0].length;
        char[] wordChars = word.toCharArray();

        for (int i = 0; i < wordLength; i++) {
            int currentRow = row + i * deltaRow;
            int currentCol = col + i * deltaCol;

            if (currentRow < 0 || currentRow >= rows || currentCol < 0 || currentCol >= cols) {
                return false;
            }
            if (grid[currentRow][currentCol] != wordChars[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkWordInGrid(char[][] grid, String word) {
        int rows = grid.length;
        int cols = grid[0].length;
        int wordLength = word.length();
        char[] wordChars = word.toCharArray();

        // Iterate through each cell in the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Check all 8 possible directions
                if (checkDirection(grid,word,row, col, 0, 1)) return true; // Horizontal right
                if (checkDirection(grid,word,row, col, 0, -1)) return true; // Horizontal left
                if (checkDirection(grid,word,row, col, 1, 0)) return true; // Vertical down
                if (checkDirection(grid,word,row, col, -1, 0)) return true; // Vertical up
                if (checkDirection(grid,word,row, col, 1, 1)) return true; // Diagonal down-right
                if (checkDirection(grid,word,row, col, 1, -1)) return true; // Diagonal down-left
                if (checkDirection(grid,word,row, col, -1, 1)) return true; // Diagonal up-right
                if (checkDirection(grid,word,row, col, -1, -1)) return true; // Diagonal up-left
            }
        }

        // If the word was not found in any direction
        return false;
    }

    private List<String> findAllWords(char[][] grid, List<String> words) {
        return words.stream()
                .filter(word -> checkWordInGrid(grid, word))
                .toList();
    }
}