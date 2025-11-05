package com.qwikride.service.filter;

import com.qwikride.model.RideHistory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for filtering by bike type.
 * Implements Strategy Pattern.
 */
@Component
public class BikeTypeFilterStrategy implements RideHistoryFilterStrategy {
    @Override
    public Specification<RideHistory> buildSpecification(RideHistoryFilterCriteria filter) {
        return (root, query, cb) -> {
            if (filter.getBikeType() == null || filter.getBikeType().isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("bikeType"), filter.getBikeType());
        };
    }
}

