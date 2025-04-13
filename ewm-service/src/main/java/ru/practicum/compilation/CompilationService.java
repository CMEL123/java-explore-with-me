package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> result = compilationRepository.findAllByPinned(pinned, pageable).getContent();
        return result.stream()
                .map(compilationMapper::toDto)
                .toList();
    }

    public CompilationDto findById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка " + compId + " не найдена"));

        return compilationMapper.toDto(compilation);
    }

    public CompilationDto create(NewCompilationDto dto) {
        if (dto.getEvents() == null) {
            dto.setEvents(new ArrayList<>());
        }
        List<Long> eventIds = dto.getEvents();
        List<Event> events = eventRepository.findAllById(eventIds);
        Compilation compilation = compilationRepository.save(compilationMapper.toEntity(dto, events));
        return compilationMapper.toDto(compilation);
    }

    public CompilationDto update(Long compId, UpdateCompilationDto dto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка " + compId + " не найдена"));

        if (dto.getEvents() == null) {
            dto.setEvents(new ArrayList<>());
        }

        compilation.setPinned(dto.getPinned());

        if (!dto.getEvents().isEmpty()) {
            List<Long> eventIds = dto.getEvents();
            List<Event> events = eventRepository.findAllById(eventIds);
            compilation.setEvents(events);
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            compilation.setTitle(dto.getTitle());
        }

        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }
}