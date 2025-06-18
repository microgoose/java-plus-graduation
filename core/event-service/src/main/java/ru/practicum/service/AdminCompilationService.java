package ru.practicum.service;

import ru.practicum.event_service.dto.CompilationDto;
import ru.practicum.event_service.dto.NewCompilationDto;
import ru.practicum.event_service.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto dto);

    CompilationDto updateCompilation(Long id, UpdateCompilationRequest dto);

    void deleteCompilation(Long id);
}