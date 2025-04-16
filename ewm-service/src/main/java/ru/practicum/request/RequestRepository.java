package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long userId);

    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findAllByEventId(Long eventId);

}