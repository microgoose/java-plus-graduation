package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.CategoryDto;
import ru.practicum.event_service.dto.NewCategoryDto;
import ru.practicum.service.AdminCategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesController {

    private final AdminCategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Add category: {}", newCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.addCategory(newCategoryDto));
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long catId,
                                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Update category: {}", categoryDto);
        return ResponseEntity.ok(categoryService.updateCategory(catId, categoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        log.info("Delete category: {}", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }
}
