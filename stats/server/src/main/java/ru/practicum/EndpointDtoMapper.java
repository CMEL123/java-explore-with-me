package ru.practicum;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EndpointDtoMapper {

    public EndpointHit toEntity(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

}