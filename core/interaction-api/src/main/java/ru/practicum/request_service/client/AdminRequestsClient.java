package ru.practicum.request_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.dto.RequestSearchDto;

import java.util.List;

@FeignClient(name = "request-service", contextId = "AdminRequestsClient")
public interface AdminRequestsClient {

    @GetMapping("/participation-requests/count/{eventId}")
    Long countRequests(@PathVariable Long eventId);

    @PostMapping("/participation-requests")
    List<ParticipationRequestDto> searchRequests(@RequestBody RequestSearchDto filter);

}
