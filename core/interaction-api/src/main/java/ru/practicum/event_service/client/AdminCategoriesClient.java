package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.CategoryDto;
import ru.practicum.event_service.dto.NewCategoryDto;

@FeignClient(name = "event-service", contextId = "AdminCategoriesClient")
public interface AdminCategoriesClient {

    @PostMapping("/admin/categories")
    CategoryDto addCategory(@RequestBody NewCategoryDto dto);

    @PatchMapping("/admin/categories/{catId}")
    CategoryDto updateCategory(@PathVariable Long catId, @RequestBody CategoryDto dto);

    @DeleteMapping("/admin/categories/{catId}")
    void deleteCategory(@PathVariable Long catId);
}
