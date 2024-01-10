package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static BookDto book1;
    private static BookDto book2;
    private static BookDto book3;
    private static BookDto book4;
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

        book1 = new BookDto()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("Book1")
                .setCategoryIds(Set.of(6L));

        book2 = new BookDto()
                .setId(2L)
                .setTitle("Book2")
                .setAuthor("Author2")
                .setIsbn("987654321")
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("Book2")
                .setCategoryIds(Set.of(5L));

        book3 = new BookDto()
                .setId(3L)
                .setTitle("Book3")
                .setAuthor("Author3")
                .setIsbn("12345678910")
                .setPrice(BigDecimal.valueOf(2.49))
                .setDescription("Book3")
                .setCategoryIds(Set.of(4L));

        book4 = new BookDto()
                .setId(4L)
                .setTitle("Book4")
                .setAuthor("Author4")
                .setIsbn("12345678")
                .setPrice(BigDecimal.valueOf(2.49))
                .setDescription("Book4")
                .setCategoryIds(Set.of(7L));
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

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by valid id")
    void getBookById_ValidId_Success() throws Exception {
        Long id = 1L;
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{id}", id)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto expected = book1;
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                BookDto.class
        );

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books from db")
    void getAllBooks_ValidRequest_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books")
                )
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> expected = List.of(book1, book2, book3, book4);
        BookDto[] actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                BookDto[].class
        );

        assertEquals(4, actual.length);
        assertEquals(expected, Arrays.stream(actual).collect(Collectors.toList()));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book by valid id")
    void deleteBookById_ValidBookId_NoContent() throws Exception {
        Long id = 3L;

        mockMvc.perform(
                        delete("/books/{id}", id)
                )
                .andExpect(status().isNoContent());
    }
}
