package com.qwikride.service.filter;

import com.qwikride.model.RideHistory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for filtering by ride status.
 * Implements Strategy Pattern.
 */
@Component
public class StatusFilterStrategy implements RideHistoryFilterStrategy {
    @Override
    public Specification<RideHistory> buildSpecification(RideHistoryFilterCriteria filter) {
        return (root, query, cb) -> {
            if (filter.getStatus() == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("status"), filter.getStatus());
        };
    }
}

