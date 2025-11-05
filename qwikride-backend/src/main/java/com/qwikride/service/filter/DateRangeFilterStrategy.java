package com.qwikride.service.filter;

import com.qwikride.model.RideHistory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Concrete strategy for filtering by date range.
 * Implements Strategy Pattern.
 */
@Component
public class DateRangeFilterStrategy implements RideHistoryFilterStrategy {
    @Override
    public Specification<RideHistory> buildSpecification(RideHistoryFilterCriteria filter) {
        return (root, query, cb) -> {
            if (filter.getStartDate() == null && filter.getEndDate() == null) {
                return cb.conjunction();
            }

            Predicate predicate = cb.conjunction();

            if (filter.getStartDate() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(
                    root.get("startTime"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(
                    root.get("startTime"), filter.getEndDate()));
            }

            return predicate;
        };
    }
}

