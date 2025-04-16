package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;

    public List<ParticipationRequestDto> findById(Long userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    public ParticipationRequestDto create(Long userId, Long eventId) {

        log.info("Проверка существования пользователя");
        User requester = userService.findById(userId);

        log.info("Проверка существования события");
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие " + eventId + " не существует"));

        log.info("Проверка существования запроса");
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }

        log.info("Проверка попытки добавить запрос на участие в собственном событии");
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Нельзя добавить запрос на участие в своем событии");
        }

        log.info("Проверка попытки участия в неопубликованном событии");
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        log.info("Проверка лимита запросов: event.getParticipantLimit() = {}, event.getConfirmedRequests() = {}",
                event.getParticipantLimit(), event.getConfirmedRequests());
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит запросов на участие");
        }

        Request request = new Request();

        if (event.getRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(RequestStatus.PENDING);
            log.info("Установлен статус PENDING");
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            log.info("Установлен статус CONFIRMED");
        }

        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(requester);

        Request savedRequest = requestRepository.save(request);

        if (request.getStatus() == RequestStatus.CONFIRMED) {
            log.info("Число запросов события до увеличения: {}", event.getConfirmedRequests());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            log.info("Число запросов события после увеличения: {}", event.getConfirmedRequests());
            eventRepository.save(event);
        }

        return requestMapper.toDto(savedRequest);
    }

    public ParticipationRequestDto cancel(Long userId, Long requestId) {

        userService.findById(userId);

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос " + requestId + " не существует"));

        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new ValidationException("Пользователь " + userId + " не создавал запрос " + requestId);
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestMapper.toDto(requestRepository.save(request));
    }

}