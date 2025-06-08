package ru.practicum.service;

import ru.practicum.event_service.dto.CategoryDto;
import ru.practicum.event_service.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto addCategory(NewCategoryDto dto);

    CategoryDto updateCategory(Long id, CategoryDto dto);

    void deleteCategory(Long id);
}