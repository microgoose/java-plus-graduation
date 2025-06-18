package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.CompilationDto;
import ru.practicum.event_service.dto.NewCompilationDto;
import ru.practicum.event_service.dto.UpdateCompilationRequest;
import ru.practicum.service.AdminCompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsController {

    private final AdminCompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody @Valid NewCompilationDto dto) {
        log.info("Add compilation: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(compilationService.addCompilation(dto));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId,
                                                            @RequestBody @Valid UpdateCompilationRequest request) {
        log.info("Update compilation: {}", request);
        return ResponseEntity.ok(compilationService.updateCompilation(compId, request));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation: {}", compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity.noContent().build();
    }
}
