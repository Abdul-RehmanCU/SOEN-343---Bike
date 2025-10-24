package com.qwikride.repository;

import com.qwikride.model.Bike;
import com.qwikride.model.BikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BikeRepository extends JpaRepository<Bike, UUID> {
    List<Bike> findByStationIdAndStatus(Long stationId, BikeStatus status);
    List<Bike> findByStatus(BikeStatus status);
    Optional<Bike> findByReservedByUserId(Long userId);
    List<Bike> findByCurrentUserId(Long userId);
    List<Bike> findByStationId(Long stationId);
}
