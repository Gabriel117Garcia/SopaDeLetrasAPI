package com.gamg.wordsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamg.wordsearch.DTO.WordSearchDTO;
import com.gamg.wordsearch.service.WordSearchService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WordSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WordSearchService wordSearchService;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUpClass() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testAddWordSearch() throws Exception {
        WordSearchDTO wordsearch = new WordSearchDTO();
        wordsearch.setId(new ObjectId("1001"));
        wordsearch.setRows(10);
        wordsearch.setCols(10);
        wordsearch.setWords(Arrays.asList("word1", "word2", "word3"));

        mockMvc.perform(post("/Wordsearch/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wordsearch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(wordsearch.getId()))
                .andExpect(jsonPath("$.rows").value(wordsearch.getRows()))
                .andExpect(jsonPath("$.cols").value(wordsearch.getCols()))
                .andExpect(jsonPath("$.words[0]").value(wordsearch.getWords().get(0)))
                .andDo(print());
    }

    @Test
    void testAddWordSearchInvalid() throws Exception {
        WordSearchDTO wordsearch = new WordSearchDTO();

        mockMvc.perform(post("/Wordsearch/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wordsearch)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testGetWordSearch() throws Exception{
        WordSearchDTO wordsearch1 = new WordSearchDTO();
        wordsearch1.setId(new ObjectId("1002"));
        wordsearch1.setCols(10);
        wordsearch1.setRows(10);
        wordsearch1.setWords(Arrays.asList("word1", "word2", "word3"));

        WordSearchDTO wordsearch2 = new WordSearchDTO();
        wordsearch2.setId(new ObjectId("2001"));
        wordsearch2.setCols(10);
        wordsearch2.setRows(10);
        wordsearch2.setWords(Arrays.asList("Minion", "Paloma", "Diego"));


        mockMvc.perform(get("/Wordsearch/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(wordsearch1.getId()))
                .andExpect(jsonPath("$[0].rows").value(wordsearch1.getRows()))
                .andExpect(jsonPath("$[0].cols").value(wordsearch1.getCols()))
                .andExpect(jsonPath("$[0].words[0]").value(wordsearch1.getWords().get(0)))
                .andExpect(jsonPath("$[1].id").value(wordsearch2.getId()))
                .andExpect(jsonPath("$[1].rows").value(wordsearch2.getRows()))
                .andExpect(jsonPath("$[1].cols").value(wordsearch2.getCols()))
                .andExpect(jsonPath("$[1].words[0]").value(wordsearch2.getWords().get(0)))
                .andDo(print());
    }

    @Test
    void testGetWordSearchById() throws Exception {
        WordSearchDTO wordsearch = new WordSearchDTO();
        wordsearch.setId(new ObjectId("2002"));
        wordsearch.setCols(10);
        wordsearch.setRows(10);
        wordsearch.setWords(Arrays.asList("word1", "word2", "word3"));

        mockMvc.perform(get("/Wordsearch/{id}", wordsearch.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").value(wordsearch.getRows()))
                .andExpect(jsonPath("$.cols").value(wordsearch.getCols()))
                .andExpect(jsonPath("$.words[0]").value(wordsearch.getWords().get(0)))
                .andDo(print());
    }

    @Test
    void testGetWordSearchByIdNotFound() throws Exception {
        ObjectId wordsearch1 = new ObjectId("3001");

        when(wordSearchService.getWordSearchById(String.valueOf(wordsearch1))).thenReturn(Optional.empty());

        mockMvc.perform(get("/Wordsearch/{id}", wordsearch1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }



    @Test
    void testUpdateWordSearch() throws Exception{
        WordSearchDTO wordsearch = new WordSearchDTO();
        wordsearch.setCols(10);
        wordsearch.setRows(10);
        wordsearch.setWords(Arrays.asList("word1", "word2", "word3"));

        mockMvc.perform(put("/Wordsearch/update/{id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wordsearch)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateWordSearchInvalid() throws Exception {
        WordSearchDTO wordsearch = new WordSearchDTO();

        mockMvc.perform(put("/Wordsearch/update/{id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wordsearch)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testDeleteWordSearch() throws Exception{
        ObjectId wordsearch = new ObjectId("3002");

        mockMvc.perform(delete("/Wordsearch/delete/{id}", wordsearch))
                .andExpect(status().isOk())
                .andDo(print());
    }

}