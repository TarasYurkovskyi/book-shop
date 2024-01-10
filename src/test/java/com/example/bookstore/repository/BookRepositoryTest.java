package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find books by category id with existing category")
    @Sql(scripts = {
            "classpath:database/add-books.sql",
            "classpath:database/add-categories.sql",
            "classpath:database/add-books-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-books-categories.sql",
            "classpath:database/delete-categories.sql",
            "classpath:database/delete-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_CategoriesWithIdSix_ReturnOneBook() {
        Category category = new Category();
        category.setId(6L);
        category.setName("Science");
        category.setDescription("Science");

        Book book = new Book();
        book.setId(1L);
        book.setDescription("Book1");
        book.setTitle("Book1");
        book.setAuthor("Author1");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(1.25));

        List<Book> expected = List.of(book);
        List<Book> actual = bookRepository.findAllByCategoryId(6L);

        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }
}
