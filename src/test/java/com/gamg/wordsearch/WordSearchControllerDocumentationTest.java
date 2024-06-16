package com.gamg.wordsearch;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class WordSearchControllerDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    private ManualRestDocumentation restDocumentation = new ManualRestDocumentation();
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}/{step}"))
                .build();
    }

    @Test
    void documentCreateWordSearch() throws Exception {
        String newWordSearch = "{ \"rows\": 10, \"cols\": 10, \"words\": [\"example\"] }";
        this.mockMvc.perform(post("/api/wordsearch")
                        .contentType("application/json")
                        .content(newWordSearch))
                .andExpect(status().isCreated())
                .andDo(document("create-wordsearch",
                        requestFields(
                                fieldWithPath("rows").description("The number of rows in the word search grid"),
                                fieldWithPath("cols").description("The number of columns in the word search grid"),
                                fieldWithPath("words").description("The list of words to include in the word search")
                        ),
                        responseFields(
                                fieldWithPath("id").description("The ID of the created word search"),
                                fieldWithPath("rows").description("The number of rows in the word search grid"),
                                fieldWithPath("cols").description("The number of columns in the word search grid"),
                                fieldWithPath("words").description("The list of words in the word search"),
                                fieldWithPath("grid").description("The grid of the word search")
                        )
                ));
    }

    // Otras pruebas de documentación
}