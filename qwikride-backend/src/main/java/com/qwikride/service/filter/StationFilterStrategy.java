package com.qwikride.service.filter;

import com.qwikride.model.RideHistory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for filtering by station.
 * Implements Strategy Pattern.
 */
@Component
public class StationFilterStrategy implements RideHistoryFilterStrategy {
    @Override
    public Specification<RideHistory> buildSpecification(RideHistoryFilterCriteria filter) {
        return (root, query, cb) -> {
            if (filter.getStationId() == null) {
                return cb.conjunction();
            }

            Predicate predicate;

            if (Boolean.TRUE.equals(filter.getStartStationOnly())) {
                predicate = cb.equal(root.get("startStationId"), filter.getStationId());
            } else {
                predicate = cb.or(
                    cb.equal(root.get("startStationId"), filter.getStationId()),
                    cb.equal(root.get("endStationId"), filter.getStationId())
                );
            }

            return predicate;
        };
    }
}

