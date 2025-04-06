package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.StatsDto(app, uri, COUNT(ip))" +
            "from EndpointHit " +
            "where timestamp between :start and :end " +
            "  and (:uris is null OR uri in :uris)  " +
            "group by app, uri " +
            "order by count(id) desc"
    )
    List<StatsDto> findStat(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("select new ru.practicum.StatsDto(app, uri, COUNT(distinct ip)) " +
            "from EndpointHit " +
            "where timestamp between :start and :end" +
            "  and (:uris is null OR uri in :uris)  " +
            "group by app, uri " +
            "order by count(distinct ip) desc")
    List<StatsDto> findStatUnique(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

}