package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static CategoryDto category1;
    private static CategoryDto category2;
    private static CategoryDto category3;
    private static CategoryDto category4;
    private static CategoryDto updatedDrama;
    private static CategoryDto categoryDto;
    private static CreateCategoryRequestDto category;
    private static CreateCategoryRequestDto validUpdateRequest;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-books-categories.sql")
            );
        }

        category1 = new CategoryDto()
                .setId(4L)
                .setName("Romance")
                .setDescription("Romance");

        category2 = new CategoryDto()
                .setId(5L)
                .setName("Comedy")
                .setDescription("Comedy");

        category3 = new CategoryDto()
                .setId(6L)
                .setName("Science")
                .setDescription("Science");

        category4 = new CategoryDto()
                .setId(7L)
                .setName("Drama")
                .setDescription("Drama");

        updatedDrama = new CategoryDto()
                .setId(4L)
                .setName("Updated")
                .setDescription("Updated");

        categoryDto = new CategoryDto()
                .setId(8L)
                .setName("Western")
                .setDescription("Western");

        category = new CreateCategoryRequestDto()
                .setName("Western")
                .setDescription("Western");

        validUpdateRequest = new CreateCategoryRequestDto()
                .setName("Updated")
                .setDescription("Updated");
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-books-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-books.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create category with valid request")
    void createCategory_ValidRequest_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/categories")
                                .content(objectMapper.writeValueAsString(category))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto expected = categoryDto;
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                CategoryDto.class
        );

        assertEquals(expected,actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all categories")
    void getAll_ValidRequest_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories")
                )
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> expected = List.of(category1, category2, category3, category4);
        CategoryDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                CategoryDto[].class
        );

        assertEquals(4, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category with valid id and valid request")
    void updateCategory_ValidCategoryIdAndValidRequest_Success() throws Exception {
        Long id = 4L;

        MvcResult mvcResult = mockMvc.perform(
                        put("/api/categories/{id}", id)
                                .content(objectMapper.writeValueAsString(validUpdateRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto expected = updatedDrama;
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                CategoryDto.class
        );
        assertEquals(expected, actual);
    }
}
