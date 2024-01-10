package com.example.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static Book book;
    private static Book updatedBook;
    private static Category category;
    private static BookDto bookDto;
    private static BookDto updatedBookDto;
    private static CreateBookRequestDto createBookRequestDto;
    private static CreateBookRequestDto updateBookRequestDto;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeAll
    static void beforeAll() {
        category = new Category()
                .setId(1L)
                .setName("Romance")
                .setDescription("Romance");

        book = new Book()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("Book1")
                .setCoverImage("cover image")
                .setCategories(Set.of(category));

        bookDto = new BookDto()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("Book1")
                .setCoverImage("cover image")
                .setCategoryIds(Set.of(4L));

        updatedBook = new Book()
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(15.67))
                .setDescription("Description")
                .setCoverImage("Cover")
                .setCategories(Set.of(category));

        updatedBookDto = new BookDto()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(15.67))
                .setDescription("Description")
                .setCoverImage("Cover")
                .setCategoryIds(Set.of(1L));

        createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("Book1")
                .setCoverImage("cover image")
                .setCategoryIds(Set.of(1L));

        updateBookRequestDto = new CreateBookRequestDto()
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(15.67))
                .setDescription("Description")
                .setCoverImage("Cover")
                .setCategoryIds(Set.of(1L));
    }

    @Test
    @DisplayName("Save book to DB and return DTO for saved book")
    void saveBook_ValidRequestDto_ReturnBookDto() {
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookMapper.toDto(bookRepository.save(book))).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookServiceImpl.createBook(createBookRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update book with valid id")
    void updateById_ValidBookId_ReturnBookDto() {
        Long id = 1L;

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toModel(updateBookRequestDto)).thenReturn(updatedBook);
        Mockito.when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto expected = updatedBookDto;
        BookDto actual = bookServiceImpl.updateBook(id, updateBookRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get all books from DB")
    void getAll_ValidPageable_GetListWithOneBook() {
        List<Book> list = List.of(book);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Mockito.when(bookRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
        Mockito.when(bookMapper.toDto(list.get(0))).thenReturn(bookDto);

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookServiceImpl.getAll(pageRequest);

        assertEquals(1, actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }
}
