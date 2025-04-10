
package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    public void create(EndpointHitDto dto) {
        statsRepository.save(EndpointDtoMapper.toEntity(dto));
    }

    public List<StatsDto> findStats(LocalDateTime start, LocalDateTime end,
                                               List<String> uris, String unique) {

        if (unique.equals("true")) {
            return statsRepository.findStatUnique(start, end, uris);
        } else {
            return statsRepository.findStat(start, end, uris);

        }

    }
}
