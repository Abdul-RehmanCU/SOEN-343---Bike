package com.qwikride.repository;

import com.qwikride.model.BikeStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeStationRepository extends JpaRepository<BikeStation, Long> {
}
