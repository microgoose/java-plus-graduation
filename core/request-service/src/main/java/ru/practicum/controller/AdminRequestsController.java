package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.dto.RequestSearchDto;
import ru.practicum.service.ParticipationRequestsService;

import java.util.List;

@RestController
@RequestMapping("/participation-requests")
@RequiredArgsConstructor
@Slf4j
public class AdminRequestsController {

    private final ParticipationRequestsService participationRequestsService;

    @PostMapping
    public List<ParticipationRequestDto> searchRequests(@RequestBody @Valid RequestSearchDto filter) {
        log.info("Search requests for {}", filter);
        return participationRequestsService.searchRequests(filter);
    }

    @GetMapping("/count/{eventId}")
    public Long countRequests(@PathVariable Long eventId) {
        log.info("Count requests for {}", eventId);
        return participationRequestsService.countRequests(eventId);
    }

}
