package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event_service.dto.CategoryDto;

import java.util.List;

@FeignClient(name = "event-service", contextId = "PublicCategoriesClient")
public interface PublicCategoriesClient {

    @GetMapping("/categories")
    List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size);

    @GetMapping("/categories/{catId}")
    CategoryDto getById(@PathVariable Long catId);
}
