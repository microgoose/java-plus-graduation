package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event_service.dto.CompilationDto;
import ru.practicum.event_service.dto.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceMapper {

    private final EventRepository eventRepository;
    private final EventMappingService eventMappingService;

    public CompilationDto toDto(Compilation compilation) {
        List<Event> events = eventRepository.findByIdIn(compilation.getEvents());

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventMappingService.toShortDtoList(events))
                .build();
    }

    public Compilation toEntity(NewCompilationDto dto) {
        Compilation c = new Compilation();
        c.setId(null);
        c.setTitle(dto.getTitle());
        c.setPinned(dto.getPinned() != null && dto.getPinned());
        c.setEvents(dto.getEvents() == null ? null : new HashSet<>(dto.getEvents()));
        return c;
    }

    public List<CompilationDto> toDtoList(List<Compilation> compilations) {
        return compilations.stream()
                .map(this::toDto)
                .toList();
    }
}
