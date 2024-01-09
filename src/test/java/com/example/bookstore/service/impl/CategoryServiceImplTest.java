package com.example.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
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
class CategoryServiceImplTest {
    private static Category category;
    private static Category updatedCategory;
    private static CategoryDto categoryDto;
    private static CategoryDto updatedCategoryDto;
    private static CreateCategoryRequestDto createCategoryRequestDto;
    private static CreateCategoryRequestDto updateCategoryRequestDto;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @BeforeAll
    static void beforeAll() {
        category = new Category()
                .setId(1L)
                .setName("Romance")
                .setDescription("Romance");

        updatedCategory = new Category()
                .setId(1L)
                .setName("Thriller")
                .setDescription("Thriller");

        categoryDto = new CategoryDto()
                .setId(1L)
                .setName("Romance")
                .setDescription("Romance");

        updatedCategoryDto = new CategoryDto()
                .setId(1L)
                .setName("Thriller")
                .setDescription("Thriller");

        createCategoryRequestDto = new CreateCategoryRequestDto()
                .setName("Romance")
                .setDescription("Romance");

        updateCategoryRequestDto = new CreateCategoryRequestDto()
                .setName("Thriller")
                .setDescription("Thriller");
    }

    @Test
    @DisplayName("Save category to DB and return DTO for saved category")
    void save_ValidRequestDto_ReturnCategoryDto() {
        Mockito.when(categoryMapper.toEntity(createCategoryRequestDto))
                .thenReturn(category);
        Mockito.when(categoryMapper.toDto(categoryRepository.save(category)))
                .thenReturn(categoryDto);

        CategoryDto expected = categoryDto;
        CategoryDto actual = categoryServiceImpl.save(createCategoryRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category with valid id")
    void update_ValidCategoryId_ReturnCategoryDto() {
        Long id = 1L;

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toEntity(updateCategoryRequestDto))
                .thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);

        CategoryDto expected = updatedCategoryDto;
        CategoryDto actual = categoryServiceImpl.updateCategory(id, updateCategoryRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get all categories from DB")
    void getAll_ValidPageable_GetListWithOneCategory() {
        List<Category> list = List.of(category);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Mockito.when(categoryRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
        Mockito.when(categoryMapper.toDto(list.get(0))).thenReturn(categoryDto);

        List<CategoryDto> expected = List.of(categoryDto);
        List<CategoryDto> actual = categoryServiceImpl.findAll(pageRequest);

        assertEquals(1, actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }
}
