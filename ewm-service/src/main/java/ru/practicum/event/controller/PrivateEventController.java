package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.EventService;
import ru.practicum.request.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findEventsPrivate(@PathVariable Long userId, @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.findEventsPrivate(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventPrivate(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto dto
    ) {
        log.info("Попытка создать новое событие контроллер {}", dto.getDescription());
        return eventService.createEventPrivate(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventByUserPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.findEventByUserPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventPrivate(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateEventUserRequest dto
    ) {
        return eventService.updateEventPrivate(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findEventRequestsPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.findEventRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestPrivate(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest dto
    ) {
        return eventService.updateEventRequestPrivate(userId, eventId, dto);
    }

}
