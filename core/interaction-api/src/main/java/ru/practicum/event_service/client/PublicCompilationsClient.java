package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event_service.dto.CompilationDto;

import java.util.List;

@FeignClient(name = "event-service", contextId = "PublicCompilationsClient")
public interface PublicCompilationsClient {

    @GetMapping("/compilations")
    List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size);

    @GetMapping("/compilations/{compId}")
    CompilationDto getById(@PathVariable Long compId);
}
