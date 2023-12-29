package com.example.bookstore.service;

import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto createCategoryRequestDto);

    CategoryDto updateCategory(Long id, CreateCategoryRequestDto createCategoryRequestDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    void deleteById(Long id);
}
