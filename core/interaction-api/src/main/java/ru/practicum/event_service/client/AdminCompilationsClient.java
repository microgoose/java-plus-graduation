package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.CompilationDto;
import ru.practicum.event_service.dto.NewCompilationDto;
import ru.practicum.event_service.dto.UpdateCompilationRequest;

@FeignClient(name = "event-service", contextId = "AdminCompilationsClient")
public interface AdminCompilationsClient {

    @PostMapping("/admin/compilations")
    CompilationDto create(@RequestBody NewCompilationDto dto);

    @PatchMapping("/admin/compilations/{compId}")
    CompilationDto update(@PathVariable Long compId, @RequestBody UpdateCompilationRequest dto);

    @DeleteMapping("/admin/compilations/{compId}")
    void delete(@PathVariable Long compId);
}
