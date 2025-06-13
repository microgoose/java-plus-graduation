package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.CompilationDto;
import ru.practicum.service.PublicCompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationsController {

    private final PublicCompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam(defaultValue = "0") int from,
                                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Getting compilations");
        return ResponseEntity.ok(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {
        log.info("Getting compilation with id {}", compId);
        return ResponseEntity.ok(compilationService.getCompilationById(compId));
    }
}
