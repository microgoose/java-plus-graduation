package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.dto.CompilationDto;
import ru.practicum.event_service.dto.NewCompilationDto;
import ru.practicum.event_service.dto.UpdateCompilationRequest;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationServiceMapper compilationMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto dto) {
        Compilation compilation = compilationMapper.toEntity(dto);
        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest dto) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found with id: " + id));

        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getEvents() != null) {
            compilation.setEvents(new HashSet<>(dto.getEvents()));
        }

        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Compilation not found with id: " + id);
        }
        compilationRepository.deleteById(id);
    }
}
